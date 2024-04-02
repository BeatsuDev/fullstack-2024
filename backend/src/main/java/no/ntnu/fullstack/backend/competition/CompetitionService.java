package no.ntnu.fullstack.backend.competition;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.Comparator;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import no.ntnu.fullstack.backend.attempt.AttemptService;
import no.ntnu.fullstack.backend.attempt.model.QuestionAttempt;
import no.ntnu.fullstack.backend.attempt.model.QuizAttempt;
import no.ntnu.fullstack.backend.competition.event.Event;
import no.ntnu.fullstack.backend.competition.event.Finish;
import no.ntnu.fullstack.backend.competition.event.Join;
import no.ntnu.fullstack.backend.competition.event.Proceed;
import no.ntnu.fullstack.backend.competition.exception.CompetitionAlreadyStartedException;
import no.ntnu.fullstack.backend.competition.exception.CompetitionNotFoundException;
import no.ntnu.fullstack.backend.competition.model.Competition;
import no.ntnu.fullstack.backend.question.model.Question;
import no.ntnu.fullstack.backend.quiz.QuizService;
import no.ntnu.fullstack.backend.quiz.exception.QuizNotFoundException;
import no.ntnu.fullstack.backend.quiz.model.QuizWithRevision;
import no.ntnu.fullstack.backend.user.model.User;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompetitionService {
  private final CompetitionRepository competitionRepository;
  private final AttemptService attemptService;
  private final QuizService quizService;
  private final SimpMessagingTemplate messagingTemplate;
  private final EntityManager entityManager;

  private void sendMessage(Competition competition, Event event) {
    String message = competition.getId() + ":" + event.toMessage();
    messagingTemplate.convertAndSend("/competition", message);
  }

  private void asyncExecution(Runnable runnable, int msDelay) {
    Thread thread =
        new Thread(
            () -> {
              try {
                Thread.sleep(msDelay);
                runnable.run();
              } catch (InterruptedException ignored) {
              }
            });
    thread.start();
  }

  private boolean userIsInCompetition(Competition competition, User user) {
    return competition.getQuizAttempts().stream()
        .anyMatch(qa -> qa.getAttemptedBy().getId().equals(user.getId()));
  }

  private void checkUserIsInCompetition(Competition competition, User user)
      throws CompetitionNotFoundException {
    if (!userIsInCompetition(competition, user)) throw new CompetitionNotFoundException();
  }

  public Competition createCompetition(UUID quizId) throws QuizNotFoundException {
    QuizWithRevision quizWithRevision = quizService.getLatestQuiz(quizId);
    Competition competition = new Competition(quizWithRevision.getLatestRevision());

    while (competitionRepository.findByJoinCode(competition.getJoinCode()).isPresent())
      competition.randomizeJoinCode();
    return competitionRepository.saveAndFlush(competition);
  }

  public Competition joinCompetition(Integer joinCode, User user)
      throws CompetitionNotFoundException, CompetitionAlreadyStartedException {
    Competition competition =
        competitionRepository
            .findByJoinCode(joinCode)
            .orElseThrow(CompetitionNotFoundException::new);
    if (competition.getStarted()) throw new CompetitionAlreadyStartedException();

    asyncExecution(() -> sendMessage(competition, new Join()), 1000);
    if (userIsInCompetition(competition, user)) return competition;

    QuizAttempt attempt = attemptService.createAttempt(competition.getRevision(), user);
    competition.getQuizAttempts().add(attempt);
    return competitionRepository.saveAndFlush(competition);
  }

  public Competition getCompetition(Integer joinCode, User user)
      throws CompetitionNotFoundException {
    Competition competition =
        competitionRepository
            .findByJoinCode(joinCode)
            .orElseThrow(CompetitionNotFoundException::new);
    checkUserIsInCompetition(competition, user);
    return competition;
  }

  private Optional<Question> findFirstQuestion(Competition competition) {
    return competition.getRevision().getQuestions().stream()
        .min(Comparator.comparingInt(Question::getSequenceNumber));
  }

  @Transactional
  public void startCompetition(Integer joinCode, User loggedInUser)
      throws CompetitionNotFoundException, CompetitionAlreadyStartedException {
    entityManager.clear();
    Competition competition =
        competitionRepository
            .findByJoinCode(joinCode)
            .orElseThrow(CompetitionNotFoundException::new);
    checkUserIsInCompetition(competition, loggedInUser);

    if (competition.getStarted()) throw new CompetitionAlreadyStartedException();

    Optional<UUID> firstQuestion = findFirstQuestion(competition).map(Question::getQuestionId);
    asyncProceedToQuestion(competition, firstQuestion, 1000);

    competition.setStarted(true);
    competitionRepository.saveAndFlush(competition);
  }

  private Optional<Question> nextQuestion(Competition competition, UUID questionId) {
    Question currentQuestion =
        competition.getRevision().getQuestions().stream()
            .filter(q -> q.getQuestionId().equals(questionId))
            .findFirst()
            .orElseThrow();
    return competition.getRevision().getQuestions().stream()
        .filter(q -> q.getSequenceNumber() > currentQuestion.getSequenceNumber())
        .min(Comparator.comparingInt(Question::getSequenceNumber));
  }

  private void asyncProceedToNextQuestion(
      Competition competition, UUID currentQuestion, int msDelay) {
    asyncExecution(() -> proceedToNextQuestion(competition, currentQuestion), msDelay);
  }

  private void proceedToNextQuestion(Competition competition, UUID currentQuestion) {
    Optional<Question> nextQuestion = nextQuestion(competition, currentQuestion);
    proceedToQuestion(competition, nextQuestion.map(Question::getQuestionId));
  }

  private void asyncProceedToQuestion(
      Competition competition, Optional<UUID> questionId, int msDelay) {
    asyncExecution(() -> proceedToQuestion(competition, questionId), msDelay);
  }

  private void proceedToQuestion(Competition competition, Optional<UUID> questionId) {
    Event event = questionId.map(Proceed::new).map(e -> (Event) e).orElseGet(Finish::new);
    sendMessage(competition, event);

    questionId.ifPresent(uuid -> timeoutContinue(competition.getId(), uuid));
  }

  private boolean allParticipantsAnswered(Competition competition, UUID questionId) {
    return competition.getQuizAttempts().stream()
        .allMatch(qa -> quizAttemptContainsQuestion(qa, questionId));
  }

  @Transactional
  public void proceedToNextQuestionIfAnyUnanswered(UUID competitionId, UUID currentQuestion)
      throws CompetitionNotFoundException {
    entityManager.clear();
    Competition competition =
        competitionRepository
            .findById(competitionId)
            .orElseThrow(CompetitionNotFoundException::new);

    if (!allParticipantsAnswered(competition, currentQuestion))
      proceedToNextQuestion(competition, currentQuestion);
  }

  public void timeoutContinue(UUID competitionId, UUID questionId) {
    asyncExecution(
        () -> {
          try {
            proceedToNextQuestionIfAnyUnanswered(competitionId, questionId);
          } catch (CompetitionNotFoundException e) {
            throw new RuntimeException(e);
          }
        },
        15000);
  }

  private boolean quizAttemptContainsQuestion(QuizAttempt quizAttempt, UUID questionId) {
    return quizAttempt.getQuestionAttempts().stream()
        .map(QuestionAttempt::getQuestion)
        .map(Question::getQuestionId)
        .anyMatch(questionId::equals);
  }

  @Transactional
  public void submitQuestion(UUID competitionId, UUID questionId, User loggedInUser)
      throws CompetitionNotFoundException {
    entityManager.clear();
    Competition competition =
        competitionRepository
            .findById(competitionId)
            .orElseThrow(CompetitionNotFoundException::new);
    checkUserIsInCompetition(competition, loggedInUser);

    if (allParticipantsAnswered(competition, questionId))
      asyncProceedToNextQuestion(competition, questionId, 1000);
  }
}
