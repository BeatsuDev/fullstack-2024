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

  public List<QuizWithRevision> retrieveQuizzes() {
    return quizRepository.findWithFirstRevision();
  }

  public List<QuizWithRevision> retrieveQuizzes(QuizFilters filters)
      throws NoQuizzesFoundException {
    List<QuizWithRevision> quizzes = retrieveQuizzes();
    if (filters.getCategory() != null && !filters.getCategory().isEmpty()) {
      quizzes.removeIf(
          quizWithRevision ->
              quizWithRevision.getLatestRevision().getCategories().stream()
                  .map(Category::getId)
                  .noneMatch(
                      category -> filters.getCategory().stream().anyMatch(category::equals)));
    }
    if (filters.getMinDifficulty() != null) {
      quizzes.removeIf(
          quizWithRevision ->
              quizWithRevision.getLatestRevision().getDifficulty() < filters.getMinDifficulty());
    }
    if (filters.getMaxDifficulty() != null) {
      quizzes.removeIf(
          quizWithRevision ->
              quizWithRevision.getLatestRevision().getDifficulty() > filters.getMaxDifficulty());
    }
    if (filters.getTextSearch() != null && !filters.getTextSearch().isEmpty()) {
      quizzes.removeIf(
          quizWithRevision ->
              !quizWithRevision.getLatestRevision().getTitle().contains(filters.getTextSearch())
                  && !quizWithRevision
                      .getLatestRevision()
                      .getDescription()
                      .contains(filters.getTextSearch()));
    }
    if (filters.getCreator() != null) {
      quizzes.removeIf(
          quizWithRevision ->
              !quizWithRevision.getQuiz().getCreator().getId().equals(filters.getCreator()));
    }
    if (filters.getCollaborator() != null) {
      quizzes.removeIf(
          quizWithRevision ->
              quizWithRevision.getQuiz().getCollaborators().stream()
                  .noneMatch(user -> user.getId().equals(filters.getCollaborator())));
    }

    int start = filters.getPage() * filters.getPageSize();
    int end = Math.min(start + filters.getPageSize(), quizzes.size());
    try {
      return quizzes.subList(start, end);
    } catch (IndexOutOfBoundsException e) {
      throw new NoQuizzesFoundException();
    }
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
