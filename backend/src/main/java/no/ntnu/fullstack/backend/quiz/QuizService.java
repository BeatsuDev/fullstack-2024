package no.ntnu.fullstack.backend.quiz;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import no.ntnu.fullstack.backend.category.CategoryService;
import no.ntnu.fullstack.backend.category.model.Category;
import no.ntnu.fullstack.backend.collaborator.CollaboratorService;
import no.ntnu.fullstack.backend.collaborator.exceptions.NotCollaboratorException;
import no.ntnu.fullstack.backend.quiz.dto.QuizFilters;
import no.ntnu.fullstack.backend.quiz.exception.NoQuizzesFoundException;
import no.ntnu.fullstack.backend.quiz.exception.QuizNotFoundException;
import no.ntnu.fullstack.backend.quiz.model.Quiz;
import no.ntnu.fullstack.backend.quiz.model.QuizWithRevision;
import no.ntnu.fullstack.backend.quiz.repository.QuizRepository;
import no.ntnu.fullstack.backend.revision.RevisionRepository;
import no.ntnu.fullstack.backend.revision.model.Revision;
import no.ntnu.fullstack.backend.user.model.User;
import org.springframework.data.domain.Page;
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
  private final CategoryService categoryService;
  private final CollaboratorService collaboratorService;

  /**
   * Creates a new quiz with a given revision. The revision is the first version of the quiz.
   *
   * @param quiz The quiz to create.
   * @param revision The revision to create.
   * @return The created quiz.
   */
  @Transactional
  public QuizWithRevision createQuiz(Quiz quiz, Revision revision) {
    Quiz createdQuiz = quizRepository.saveAndFlush(quiz);
    revision.setQuiz(createdQuiz);
    revision.setCategories(
        categoryService.getCategoriesById(
            revision.getCategories().stream().map(Category::getId).toList()));
    revisionRepository.saveAndFlush(revision);
    return new QuizWithRevision(createdQuiz, revision);
  }

  public List<QuizWithRevision> retrieveQuizzes(QuizFilters filters)
      throws NoQuizzesFoundException {
    Page<QuizWithRevision> quizzes = quizRepository.findByFilter(filters, filters.toPageable());
    if (quizzes == null || quizzes.isEmpty()) throw new NoQuizzesFoundException();
    return quizzes.toList();
  }

  public QuizWithRevision getLatestQuiz(UUID quizId) throws QuizNotFoundException {
    return quizRepository.findWithFirstRevision(quizId).orElseThrow(QuizNotFoundException::new);
  }

  public User addCollaborator(UUID quizId, User user)
      throws QuizNotFoundException, NotCollaboratorException {
    Quiz quiz = quizRepository.findById(quizId).orElseThrow(QuizNotFoundException::new);
    collaboratorService.loggedInUserIsCollaboratorOrThrow(quiz);
    quiz.getCollaborators().add(user);
    quizRepository.save(quiz);
    return user;
  }

  public Quiz getQuizById(UUID quizId) throws QuizNotFoundException {
    return quizRepository.findById(quizId).orElseThrow(QuizNotFoundException::new);
  }

  public void removeCollaborator(UUID quizId, UUID userId)
      throws QuizNotFoundException, NotCollaboratorException {
    Quiz quiz = quizRepository.findById(quizId).orElseThrow(QuizNotFoundException::new);
    collaboratorService.loggedInUserIsCollaboratorOrThrow(quiz);
    quiz.getCollaborators().removeIf(user -> user.getId().equals(userId));
    quizRepository.save(quiz);
  }
}
