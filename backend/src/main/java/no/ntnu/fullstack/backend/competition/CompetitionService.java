package no.ntnu.fullstack.backend.competition;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import no.ntnu.fullstack.backend.attempt.AttemptService;
import no.ntnu.fullstack.backend.attempt.model.QuestionAttempt;
import no.ntnu.fullstack.backend.attempt.model.QuizAttempt;
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

  private void sendMessage(Competition competition, Event event) {
    String message = competition.getId() + ":" + event;
    messagingTemplate.convertAndSend("/competition", message);
  }

  private void sendMessage(Competition competition, Event event, int msDelay) {
    Thread thread =
        new Thread(
            () -> {
              try {
                Thread.sleep(msDelay);
                sendMessage(competition, event);
              } catch (InterruptedException ignored) {
              }
            });
    thread.start();
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

    sendMessage(competition, Event.JOIN, 1000);
    if (competition.getQuizAttempts().stream()
        .anyMatch(qa -> qa.getAttemptedBy().getId().equals(user.getId()))) {
      return competition;
    }

    QuizAttempt attempt = attemptService.createAttempt(competition.getRevision(), user);
    competition.getQuizAttempts().add(attempt);
    return competitionRepository.saveAndFlush(competition);
  }

  public Competition getCompetition(Integer joinCode) throws CompetitionNotFoundException {
    return competitionRepository
        .findByJoinCode(joinCode)
        .orElseThrow(CompetitionNotFoundException::new);
  }

  public Competition startCompetition(Integer joinCode, User loggedInUser)
      throws CompetitionNotFoundException, CompetitionAlreadyStartedException {
    Competition competition =
        competitionRepository
            .findByJoinCode(joinCode)
            .orElseThrow(CompetitionNotFoundException::new);
    if (competition.getQuizAttempts().stream()
        .noneMatch(qa -> qa.getAttemptedBy().getId().equals(loggedInUser.getId())))
      throw new CompetitionNotFoundException();

    if (competition.getStarted()) throw new CompetitionAlreadyStartedException();

    sendMessage(competition, Event.PROCEED, 1000);
    competition.setStarted(true);
    return competitionRepository.saveAndFlush(competition);
  }

  private boolean quizAttemptContainsQuestion(QuizAttempt quizAttempt, UUID questionId) {
    return quizAttempt.getQuestionAttempts().stream()
        .map(QuestionAttempt::getQuestion)
        .map(Question::getQuestionId)
        .anyMatch(questionId::equals);
  }

  public void submitQuestion(UUID competitionId, UUID questionId, User loggedInUser)
      throws CompetitionNotFoundException {
    Competition competition =
        competitionRepository
            .findById(competitionId)
            .orElseThrow(CompetitionNotFoundException::new);
    if (competition.getQuizAttempts().stream()
        .noneMatch(qa -> qa.getAttemptedBy().getId().equals(loggedInUser.getId())))
      throw new CompetitionNotFoundException();

    if (competition.getQuizAttempts().stream()
        .allMatch(qa -> quizAttemptContainsQuestion(qa, questionId))) {
      sendMessage(competition, Event.PROCEED, 1000);
    }
  }
}
