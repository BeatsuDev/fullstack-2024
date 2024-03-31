package no.ntnu.fullstack.backend.revision;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import no.ntnu.fullstack.backend.collaborator.CollaboratorService;
import no.ntnu.fullstack.backend.collaborator.exceptions.NotCollaboratorException;
import no.ntnu.fullstack.backend.image.ImageService;
import no.ntnu.fullstack.backend.image.exception.ImageNotFound;
import no.ntnu.fullstack.backend.image.model.Image;
import no.ntnu.fullstack.backend.question.QuestionService;
import no.ntnu.fullstack.backend.question.exception.NoCorrectOptionException;
import no.ntnu.fullstack.backend.question.exception.QuestionNotFoundException;
import no.ntnu.fullstack.backend.question.model.Question;
import no.ntnu.fullstack.backend.quiz.QuizService;
import no.ntnu.fullstack.backend.quiz.exception.QuizNotFoundException;
import no.ntnu.fullstack.backend.quiz.model.Quiz;
import no.ntnu.fullstack.backend.quiz.model.QuizWithRevision;
import no.ntnu.fullstack.backend.revision.exception.RevisionNotFound;
import no.ntnu.fullstack.backend.revision.model.Revision;
import no.ntnu.fullstack.backend.user.model.User;
import org.springframework.data.util.Pair;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RevisionService {
  private final QuizService quizService;
  private final QuestionService questionService;
  private final RevisionRepository revisionRepository;
  private final CollaboratorService collaboratorService;
  private final ImageService imageService;

  private Revision newRevision(UUID quizId, Revision revision)
      throws QuizNotFoundException, NotCollaboratorException {
    var quiz = quizService.getQuizById(quizId);
    collaboratorService.loggedInUserIsCollaboratorOrThrow(quiz);
    User loggedInUser =
        (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    Revision newRevision = revision.copy();
    newRevision.setQuiz(quiz);
    newRevision.setCreator(loggedInUser);

    return revisionRepository.save(newRevision);
  }

  public Question updateQuestion(UUID quizId, Question update, UUID imageId)
      throws QuizNotFoundException,
          QuestionNotFoundException,
          NoCorrectOptionException,
          NotCollaboratorException,
          ImageNotFound {
    Image image = null;
    if (imageId != null) {
      image = imageService.getImage(imageId);
    }

    questionService.validateQuestion(update);
    Revision latestRevision = quizService.getLatestQuiz(quizId).getLatestRevision();

    var question =
        latestRevision.getQuestions().stream()
            .filter(q -> q.getQuestionId().equals(update.getQuestionId()))
            .findFirst()
            .orElseThrow(QuestionNotFoundException::new);
    question.setQuestion(update.getQuestion());
    question.setOptions(update.getOptions());
    question.setAnswer(update.getAnswer());
    question.setImage(image);

    Revision revision = newRevision(quizId, latestRevision);
    question.setRevision(revision);
    return question;
  }

  public void deleteQuestion(UUID questionId)
      throws QuestionNotFoundException, QuizNotFoundException, NotCollaboratorException {
    var question =
        questionService
            .getLatestQuestionByQuestionId(questionId)
            .orElseThrow(QuestionNotFoundException::new);
    question.getRevision().getQuestions().remove(question);
    newRevision(question.getRevision().getQuiz().getId(), question.getRevision());
  }

  public Question createQuestion(UUID quizId, Question question, UUID imageId)
      throws QuizNotFoundException,
          NoCorrectOptionException,
          NotCollaboratorException,
          ImageNotFound {
    Image image = null;
    if (imageId != null) {
      image = imageService.getImage(imageId);
    }

    questionService.validateQuestion(question);
    question.setQuestionId(UUID.randomUUID());
    question.setImage(image);

    Revision latestRevision = quizService.getLatestQuiz(quizId).getLatestRevision();

    question.setSequenceNumber(
        latestRevision.getQuestions().stream()
                .map(Question::getSequenceNumber)
                .reduce(0, Integer::max)
            + 1);
    latestRevision.getQuestions().add(question);

    var revision = newRevision(quizId, latestRevision);
    UUID questionId = question.getQuestionId();
    question =
        revision.getQuestions().stream()
            .filter(q -> q.getQuestionId().equals(questionId))
            .findFirst()
            .orElseThrow();
    return question;
  }

  public QuizWithRevision updateQuizInfo(UUID quizId, Revision revision)
      throws QuizNotFoundException, NotCollaboratorException {
    Revision latestRevision = quizService.getLatestQuiz(quizId).getLatestRevision().copy();

    latestRevision.setTitle(revision.getTitle());
    latestRevision.setDescription(revision.getDescription());
    latestRevision.setDifficulty(revision.getDifficulty());
    latestRevision.setCategories(revision.getCategories());

    newRevision(quizId, latestRevision);
    return quizService.getLatestQuiz(quizId);
  }

  public Pair<Quiz, List<Revision>> getAllRevisions(UUID quizId) throws QuizNotFoundException {
    Quiz quiz = quizService.getQuizById(quizId);
    List<Revision> revisions = revisionRepository.findByQuizId(quizId);
    return Pair.of(quiz, revisions);
  }

  public QuizWithRevision revertToRevision(UUID quizId, UUID revisionId)
      throws QuizNotFoundException, RevisionNotFound, NotCollaboratorException {
    Revision revision = revisionRepository.findById(revisionId).orElseThrow(RevisionNotFound::new);
    newRevision(quizId, revision);
    return quizService.getLatestQuiz(quizId);
  }

  public QuizWithRevision getRevision(UUID quizId, UUID revisionId)
      throws RevisionNotFound, QuizNotFoundException {
    Quiz quiz = quizService.getQuizById(quizId);
    Revision revision = revisionRepository.findById(revisionId).orElseThrow(RevisionNotFound::new);
    return new QuizWithRevision(quiz, revision);
  }
}
