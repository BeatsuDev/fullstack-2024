package no.ntnu.fullstack.backend.question;

import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import no.ntnu.fullstack.backend.collaborator.exceptions.NotCollaboratorException;
import no.ntnu.fullstack.backend.image.exception.ImageNotFound;
import no.ntnu.fullstack.backend.question.dto.QuestionCreateDTO;
import no.ntnu.fullstack.backend.question.dto.QuestionDTO;
import no.ntnu.fullstack.backend.question.exception.NoCorrectOptionException;
import no.ntnu.fullstack.backend.question.exception.QuestionNotFoundException;
import no.ntnu.fullstack.backend.question.model.Question;
import no.ntnu.fullstack.backend.quiz.exception.QuizNotFoundException;
import no.ntnu.fullstack.backend.revision.RevisionService;
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
  public ResponseEntity<QuestionDTO> updateQuestion(
      @PathVariable("id") UUID questionId, @Valid @RequestBody QuestionCreateDTO update)
      throws QuizNotFoundException,
          QuestionNotFoundException,
          NoCorrectOptionException,
          NotCollaboratorException,
          ImageNotFound {
    Question newQuestion = questionMapper.fromDTO(update);
    newQuestion.setQuestionId(questionId);
    Question question =
        revisionService.updateQuestion(update.getQuizId(), newQuestion, update.getImageId());
    return ResponseEntity.ok(questionMapper.toDTO(question));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteQuestion(@PathVariable("id") UUID id)
      throws QuestionNotFoundException, QuizNotFoundException, NotCollaboratorException {
    revisionService.deleteQuestion(id);
    return ResponseEntity.noContent().build();
  }

  @PostMapping
  public ResponseEntity<QuestionDTO> createQuestion(@Valid @RequestBody QuestionCreateDTO create)
      throws QuizNotFoundException,
          NoCorrectOptionException,
          NotCollaboratorException,
          ImageNotFound {
    Question question =
        revisionService.createQuestion(
            create.getQuizId(), questionMapper.fromDTO(create), create.getImageId());
    return ResponseEntity.status(HttpStatus.CREATED).body(questionMapper.toDTO(question));
  }
}
