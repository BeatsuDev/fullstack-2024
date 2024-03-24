package no.ntnu.fullstack.backend.quiz;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import no.ntnu.fullstack.backend.question.QuestionService;
import no.ntnu.fullstack.backend.question.exception.QuestionNotFoundException;
import no.ntnu.fullstack.backend.question.model.Question;
import no.ntnu.fullstack.backend.quiz.exception.QuizNotFoundException;
import no.ntnu.fullstack.backend.quiz.model.QuizWithRevision;
import no.ntnu.fullstack.backend.quiz.model.Revision;
import no.ntnu.fullstack.backend.quiz.repository.RevisionRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RevisionService {
  private final QuizService quizService;
  private final QuestionService questionService;
  private final RevisionRepository revisionRepository;

  private void newRevision(UUID quizId, Revision revision) {
    var latestQuiz = quizService.getLatestQuiz(quizId);
    if (latestQuiz.isEmpty()) {
      return;
    }

    Revision newRevision = new Revision();
    newRevision.setTitle(revision.getTitle());
    newRevision.setDescription(revision.getDescription());
    newRevision.setDifficulty(revision.getDifficulty());
    newRevision.setCreator(revision.getCreator());
    newRevision.setQuiz(latestQuiz.get().getQuiz());

    newRevision.setQuestions(
        revision.getQuestions().stream()
            .map(
                question -> {
                  var newQuestion = questionService.makeQuestionCopy(question);
                  newQuestion.setRevision(newRevision);
                  return newQuestion;
                })
            .toList());

    revisionRepository.saveAndFlush(newRevision);
  }

  public void updateQuestion(UUID quizId, Question update)
      throws QuizNotFoundException, QuestionNotFoundException {
    Revision latestRevision =
        quizService.getLatestQuiz(quizId).map(QuizWithRevision::getLatestRevision).orElse(null);
    if (latestRevision == null) {
      throw new QuizNotFoundException();
    }

    var question =
        latestRevision.getQuestions().stream()
            .filter(q -> q.getId().equals(update.getId()))
            .findFirst()
            .orElseThrow(QuestionNotFoundException::new);
    question.setQuestion(update.getQuestion());
    question.setOptions(update.getOptions());
    question.setAnswer(update.getAnswer());

    newRevision(quizId, latestRevision);
  }

  public void deleteQuestion(UUID questionId) throws QuestionNotFoundException {
    var question =
        questionService.getQuestion(questionId).orElseThrow(QuestionNotFoundException::new);
    question.getRevision().getQuestions().remove(question);
    newRevision(question.getRevision().getQuiz().getId(), question.getRevision());
  }

  public void createQuestion(UUID quizId, Question question) throws QuizNotFoundException {
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
    newRevision(quizId, latestRevision);
  }
}
