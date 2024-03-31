package no.ntnu.fullstack.backend.attempt;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import no.ntnu.fullstack.backend.attempt.exception.AttemptNotFoundException;
import no.ntnu.fullstack.backend.attempt.exception.QuestionAlreadyAnswered;
import no.ntnu.fullstack.backend.attempt.model.QuestionAttempt;
import no.ntnu.fullstack.backend.attempt.model.QuizAttempt;
import no.ntnu.fullstack.backend.attempt.repository.QuestionAttemptRepository;
import no.ntnu.fullstack.backend.attempt.repository.QuizAttemptRepository;
import no.ntnu.fullstack.backend.question.exception.QuestionNotFoundException;
import no.ntnu.fullstack.backend.question.model.Question;
import no.ntnu.fullstack.backend.quiz.QuizService;
import no.ntnu.fullstack.backend.quiz.exception.QuizNotFoundException;
import no.ntnu.fullstack.backend.quiz.model.QuizWithRevision;
import no.ntnu.fullstack.backend.revision.model.Revision;
import no.ntnu.fullstack.backend.user.model.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AttemptService {
  private final QuizService quizService;
  private final QuizAttemptRepository quizAttemptRepository;
  private final QuestionAttemptRepository questionAttemptRepository;

  public QuizAttempt createAttempt(UUID quizId, User user) throws QuizNotFoundException {
    QuizWithRevision quizWithRevision = quizService.getLatestQuiz(quizId);
    return createAttempt(quizWithRevision.getLatestRevision(), user);
  }

  public QuizAttempt createAttempt(Revision revision, User user) {
    QuizAttempt attempt = new QuizAttempt();
    attempt.setAttemptedBy(user);
    attempt.setRevision(revision);
    return quizAttemptRepository.saveAndFlush(attempt);
  }

  public QuizAttempt getAttempt(UUID attemptId) throws AttemptNotFoundException {
    return quizAttemptRepository
        .getQuizAttemptById(attemptId)
        .orElseThrow(AttemptNotFoundException::new);
  }

  public QuizAttempt getAttempt(UUID quizId, UUID attemptId)
      throws AttemptNotFoundException, QuizNotFoundException {
    QuizAttempt attempt = getAttempt(attemptId);
    if (!attempt.getRevision().getQuiz().getId().equals(quizId)) throw new QuizNotFoundException();

    return attempt;
  }

  public List<QuizAttempt> getAttempts(UUID quizId, User loggedInUser) {
    return quizAttemptRepository.getQuizAttemptsByAttemptedByAndQuiz(loggedInUser.getId(), quizId);
  }

  public QuestionAttempt submitAttempt(
      UUID quizId,
      UUID attemptId,
      UUID questionId,
      QuestionAttempt questionAttempt,
      User loggedInUser)
      throws AttemptNotFoundException,
          QuizNotFoundException,
          QuestionAlreadyAnswered,
          QuestionNotFoundException {
    QuizAttempt quizAttempt = getAttempt(quizId, attemptId);
    if (!quizAttempt.getAttemptedBy().getId().equals(loggedInUser.getId()))
      throw new AttemptNotFoundException();

    Question question =
        quizAttempt.getRevision().getQuestions().stream()
            .filter(q -> q.getQuestionId().equals(questionId))
            .findFirst()
            .orElseThrow(QuestionNotFoundException::new);
    questionAttempt.setQuestion(question);

    if (quizAttempt.getQuestionAttempts().stream()
        .anyMatch(
            qa ->
                qa.getQuestion()
                    .getQuestionId()
                    .equals(questionAttempt.getQuestion().getQuestionId()))) {
      throw new QuestionAlreadyAnswered();
    }

    questionAttempt.setQuizAttempt(quizAttempt);
    return questionAttemptRepository.saveAndFlush(questionAttempt);
  }
}
