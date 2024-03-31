package no.ntnu.fullstack.backend.attempt.dto;

import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import no.ntnu.fullstack.backend.quiz.dto.QuizDTO;

@Getter
@RequiredArgsConstructor
public class QuizAttemptDTO {
  private final UUID id;
  private final QuizDTO quiz;
  private final List<QuestionAttemptDTO> questionAttempts;
  private final boolean complete;
}
