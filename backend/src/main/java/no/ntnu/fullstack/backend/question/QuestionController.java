package no.ntnu.fullstack.backend.question;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import no.ntnu.fullstack.backend.question.dto.QuestionCreateDTO;
import no.ntnu.fullstack.backend.question.dto.QuestionWithAnswerDTO;
import no.ntnu.fullstack.backend.question.exception.QuestionNotFoundException;
import no.ntnu.fullstack.backend.question.model.Question;
import no.ntnu.fullstack.backend.quiz.RevisionService;
import no.ntnu.fullstack.backend.quiz.exception.QuizNotFoundException;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/question")
@RequiredArgsConstructor
public class QuestionController {
  private final RevisionService revisionService;
  private final QuestionMapper questionMapper = Mappers.getMapper(QuestionMapper.class);

  @PutMapping("/{id}")
  public ResponseEntity<QuestionWithAnswerDTO> updateQuestion(
      @PathVariable("id") UUID id, @RequestBody QuestionCreateDTO update)
      throws QuizNotFoundException, QuestionNotFoundException {
    Question newQuestion = questionMapper.fromDTO(update);
    newQuestion.setId(id);
    Question question = revisionService.updateQuestion(update.getQuizId(), newQuestion);
    return ResponseEntity.ok(questionMapper.toDTOWithAnswer(question));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteQuestion(@PathVariable("id") UUID id)
      throws QuestionNotFoundException, QuizNotFoundException {
    revisionService.deleteQuestion(id);
    return ResponseEntity.noContent().build();
  }

  @PostMapping
  public ResponseEntity<QuestionWithAnswerDTO> createQuestion(@RequestBody QuestionCreateDTO create)
      throws QuizNotFoundException {
    Question question =
        revisionService.createQuestion(create.getQuizId(), questionMapper.fromDTO(create));

    return ResponseEntity.status(HttpStatus.CREATED).body(questionMapper.toDTOWithAnswer(question));
  }
}
