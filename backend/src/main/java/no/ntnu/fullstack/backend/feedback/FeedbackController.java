package no.ntnu.fullstack.backend.feedback;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import no.ntnu.fullstack.backend.feedback.dto.FeedbackCreate;
import no.ntnu.fullstack.backend.feedback.dto.FeedbackDTO;
import no.ntnu.fullstack.backend.quiz.QuizService;
import no.ntnu.fullstack.backend.quiz.exception.QuizNotFoundException;
import no.ntnu.fullstack.backend.quiz.model.Quiz;
import no.ntnu.fullstack.backend.user.model.User;
import org.mapstruct.factory.Mappers;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/quiz/{quizId}/feedback")
public class FeedbackController {
  private final FeedbackService feedbackService;
  private final QuizService quizService;
  private final FeedbackMapper feedbackMapper = Mappers.getMapper(FeedbackMapper.class);

  @PostMapping
  public ResponseEntity<FeedbackDTO> addFeedbackOnQuiz(
      @PathVariable UUID quizId,
      @Valid @RequestBody FeedbackCreate feedbackCreate,
      Authentication authentication)
      throws QuizNotFoundException {
    Quiz quiz = quizService.getQuizById(quizId);

    Feedback feedback = feedbackMapper.toFeedback(feedbackCreate);
    User user = (User) authentication.getPrincipal();
    FeedbackDTO feedbackDTO =
        feedbackMapper.toFeedbackDTO(feedbackService.createFeedback(feedback, quiz, user));

    return ResponseEntity.ok(feedbackDTO);
  }

  @GetMapping
  public ResponseEntity<List<FeedbackDTO>> getFeedbacksOnQuiz(@PathVariable UUID quizId) {
    List<Feedback> feedbacks = feedbackService.getFeedbacksByQuizId(quizId);

    return ResponseEntity.ok(feedbacks.stream().map(feedbackMapper::toFeedbackDTO).toList());
  }
}
