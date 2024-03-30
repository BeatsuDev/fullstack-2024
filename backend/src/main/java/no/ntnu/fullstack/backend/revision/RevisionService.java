package no.ntnu.fullstack.backend.revision;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import no.ntnu.fullstack.backend.colleborator.CollaboratorService;
import no.ntnu.fullstack.backend.colleborator.exceptions.NotCollaboratorException;
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
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RevisionService {
  private final QuizService quizService;
  private final QuestionService questionService;
  private final RevisionRepository revisionRepository;
  private final CollaboratorService collaboratorService;

  private Revision newRevision(UUID quizId, Revision revision)
      throws QuizNotFoundException, NotCollaboratorException {
    var latestQuiz =
        quizService
            .getLatestQuiz(quizId)
            .orElseThrow(QuizNotFoundException::new)
            .getLatestRevision();
    collaboratorService.loggedInUserIsCollaboratorOrThrow(latestQuiz.getQuiz());

    Revision newRevision = new Revision();
    newRevision.setTitle(revision.getTitle());
    newRevision.setDescription(revision.getDescription());
    newRevision.setDifficulty(revision.getDifficulty());
    newRevision.setCreator(revision.getCreator());
    newRevision.setQuiz(latestQuiz.getQuiz());

    newRevision.setQuestions(
        revision.getQuestions().stream()
            .map(
                question -> {
                  var newQuestion = questionService.makeQuestionCopy(question);
                  newQuestion.setRevision(newRevision);
                  return newQuestion;
                })
            .toList());

    return revisionRepository.saveAndFlush(newRevision);
  }

  public Question updateQuestion(UUID quizId, Question update)
      throws QuizNotFoundException,
          QuestionNotFoundException,
          NoCorrectOptionException,
          NotCollaboratorException {
    questionService.validateQuestion(update);
    Revision latestRevision =
        quizService.getLatestQuiz(quizId).map(QuizWithRevision::getLatestRevision).orElse(null);
    if (latestRevision == null) {
      throw new QuizNotFoundException();
    }

    var question =
        latestRevision.getQuestions().stream()
            .filter(q -> q.getQuestionId().equals(update.getQuestionId()))
            .findFirst()
            .orElseThrow(QuestionNotFoundException::new);
    question.setQuestion(update.getQuestion());
    question.setOptions(update.getOptions());
    question.setAnswer(update.getAnswer());

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

  public Question createQuestion(UUID quizId, Question question)
      throws QuizNotFoundException, NoCorrectOptionException, NotCollaboratorException {
    questionService.validateQuestion(question);
    question.setQuestionId(UUID.randomUUID());

    Revision latestRevision =
        quizService
            .getLatestQuiz(quizId)
            .map(QuizWithRevision::getLatestRevision)
            .orElseThrow(QuizNotFoundException::new);

    question.setSequenceNumber(
        latestRevision.getQuestions().stream()
                .map(Question::getSequenceNumber)
                .reduce(0, Integer::max)
            + 1);
    latestRevision.getQuestions().add(question);

    var revision = newRevision(quizId, latestRevision);
    question.setRevision(revision);
    return question;
  }

  public QuizWithRevision updateQuizInfo(UUID quizId, Revision revision)
      throws QuizNotFoundException, NotCollaboratorException {
    QuizWithRevision latestQuiz =
        quizService.getLatestQuiz(quizId).orElseThrow(QuizNotFoundException::new);

    latestQuiz.getLatestRevision().setTitle(revision.getTitle());
    latestQuiz.getLatestRevision().setDescription(revision.getDescription());
    latestQuiz.getLatestRevision().setDifficulty(revision.getDifficulty());
    latestQuiz.getLatestRevision().setCategories(revision.getCategories());

    Revision newRevision = newRevision(quizId, latestQuiz.getLatestRevision());
    latestQuiz.setLatestRevision(newRevision);
    return latestQuiz;
  }

  public Pair<Quiz, List<Revision>> getAllRevisions(UUID quizId) throws QuizNotFoundException {
    Quiz quiz = quizService.getLatestQuiz(quizId).orElseThrow(QuizNotFoundException::new).getQuiz();
    List<Revision> revisions = revisionRepository.findByQuizId(quizId);
    return Pair.of(quiz, revisions);
  }

  public QuizWithRevision revertToRevision(UUID quizId, UUID revisionId)
      throws QuizNotFoundException, RevisionNotFound, NotCollaboratorException {
    Revision revision = revisionRepository.findById(revisionId).orElseThrow(RevisionNotFound::new);
    newRevision(quizId, revision);
    return quizService.getLatestQuiz(quizId).orElseThrow(QuizNotFoundException::new);
  }
}