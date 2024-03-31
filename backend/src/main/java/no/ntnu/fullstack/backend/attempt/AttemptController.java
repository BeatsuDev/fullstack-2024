package no.ntnu.fullstack.backend.attempt;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import no.ntnu.fullstack.backend.attempt.dto.AnswerDTO;
import no.ntnu.fullstack.backend.attempt.dto.QuestionAttemptDTO;
import no.ntnu.fullstack.backend.attempt.dto.QuizAttemptDTO;
import no.ntnu.fullstack.backend.attempt.exception.AttemptNotFoundException;
import no.ntnu.fullstack.backend.attempt.exception.QuestionAlreadyAnswered;
import no.ntnu.fullstack.backend.attempt.model.QuestionAttempt;
import no.ntnu.fullstack.backend.attempt.model.QuizAttempt;
import no.ntnu.fullstack.backend.question.exception.QuestionNotFoundException;
import no.ntnu.fullstack.backend.quiz.exception.QuizNotFoundException;
import no.ntnu.fullstack.backend.user.model.User;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quiz/{quizId}/attempt")
@RequiredArgsConstructor
public class AttemptController {
  private final AttemptService attemptService;
  private final AttemptMapper attemptMapper = Mappers.getMapper(AttemptMapper.class);

  @PostMapping
  public ResponseEntity<QuizAttemptDTO> createAttempt(
      Authentication auth, @PathVariable("quizId") UUID quizId) throws QuizNotFoundException {
    User loggedInUser = (User) auth.getPrincipal();
    QuizAttempt quizAttempt = attemptService.createAttempt(quizId, loggedInUser);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(attemptMapper.toQuizAttemptDTO(quizAttempt));
  }

  @GetMapping
  public ResponseEntity<List<QuizAttemptDTO>> getAttempts(
      Authentication auth, @PathVariable("quizId") UUID quizId) {
    User loggedInUser = (User) auth.getPrincipal();
    List<QuizAttempt> quizAttempts = attemptService.getAttempts(quizId, loggedInUser);

    return ResponseEntity.ok(attemptMapper.toQuizAttemptDTOs(quizAttempts));
  }

  @GetMapping("/{attemptId}")
  public ResponseEntity<QuizAttemptDTO> getAttempt(
      Authentication auth,
      @PathVariable("quizId") UUID quizId,
      @PathVariable("attemptId") UUID attemptId)
      throws AttemptNotFoundException, QuizNotFoundException {
    User loggedInUser = (User) auth.getPrincipal();
    QuizAttempt quizAttempt = attemptService.getAttempt(quizId, attemptId);
    if (!quizAttempt.getAttemptedBy().getId().equals(loggedInUser.getId()))
      throw new AttemptNotFoundException();

    return ResponseEntity.ok(attemptMapper.toQuizAttemptDTO(quizAttempt));
  }

  @PostMapping("/{attemptId}")
  public ResponseEntity<QuestionAttemptDTO> submitAttempt(
      Authentication auth,
      @PathVariable("quizId") UUID quizId,
      @PathVariable("attemptId") UUID attemptId,
      @Valid @RequestBody AnswerDTO answerDTO)
      throws AttemptNotFoundException,
          QuizNotFoundException,
          QuestionNotFoundException,
          QuestionAlreadyAnswered {
    User loggedInUser = (User) auth.getPrincipal();
    QuestionAttempt questionAttempt = attemptMapper.toQuestionAttempt(answerDTO);

    questionAttempt =
        attemptService.submitAttempt(
            quizId, attemptId, answerDTO.getQuestionId(), questionAttempt, loggedInUser);
    return ResponseEntity.ok(attemptMapper.toQuestionAttemptDTO(questionAttempt));
  }
}
