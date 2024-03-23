package no.ntnu.fullstack.backend.quiz;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import no.ntnu.fullstack.backend.quiz.model.Quiz;
import no.ntnu.fullstack.backend.quiz.model.Revision;
import no.ntnu.fullstack.backend.quiz.repository.QuizRepository;
import no.ntnu.fullstack.backend.quiz.repository.RevisionRepository;
import org.springframework.stereotype.Service;

/**
 * The QuizService class provides methods for creating and managing quizzes. It also manages the
 * relationship between quizzes and revisions, and hides the complexity of revisions.
 */
@Service
@RequiredArgsConstructor
public class QuizService {
  private final QuizRepository quizRepository;
  private final RevisionRepository revisionRepository;

  /**
   * Creates a new quiz with a given revision. The revision is the first version of the quiz.
   *
   * @param quiz The quiz to create.
   * @param revision The revision to create.
   * @return The created quiz.
   */
  @Transactional
  public Quiz createQuiz(Quiz quiz, Revision revision) {
    Quiz createdQuiz = quizRepository.saveAndFlush(quiz);
    revision.setQuiz(createdQuiz);
    revisionRepository.saveAndFlush(revision);
    return createdQuiz;
  }
}
