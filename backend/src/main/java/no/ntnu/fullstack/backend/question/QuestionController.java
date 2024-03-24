package no.ntnu.fullstack.backend.question;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import no.ntnu.fullstack.backend.question.dto.QuestionCreateDTO;
import no.ntnu.fullstack.backend.question.exception.QuestionNotFoundException;
import no.ntnu.fullstack.backend.question.model.Question;
import no.ntnu.fullstack.backend.quiz.RevisionService;
import no.ntnu.fullstack.backend.quiz.exception.QuizNotFoundException;
import org.mapstruct.factory.Mappers;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller("/question")
@RequiredArgsConstructor
public class QuestionController {
  private final RevisionService revisionService;
  private final QuestionMapper questionMapper = Mappers.getMapper(QuestionMapper.class);

  @PutMapping("/{id}")
  public ResponseEntity<?> updateQuestion(
      @PathVariable("id") UUID id, @RequestBody QuestionCreateDTO update)
      throws QuizNotFoundException, QuestionNotFoundException {
    Question newQuestion = questionMapper.fromDTO(update);
    newQuestion.setId(id);
    revisionService.updateQuestion(update.getQuizId(), newQuestion);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteQuestion(@PathVariable("id") UUID id)
      throws QuestionNotFoundException {
    revisionService.deleteQuestion(id);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/")
  public ResponseEntity<?> createQuestion(
      @PathVariable("id") UUID quizId, @RequestBody QuestionCreateDTO create)
      throws QuizNotFoundException {
    revisionService.createQuestion(quizId, questionMapper.fromDTO(create));
    return ResponseEntity.ok().build();
  }
}
