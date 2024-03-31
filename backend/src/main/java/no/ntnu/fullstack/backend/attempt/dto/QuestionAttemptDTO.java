package no.ntnu.fullstack.backend.attempt.dto;

import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import no.ntnu.fullstack.backend.question.dto.QuestionDTO;

@Getter
@RequiredArgsConstructor
public class QuestionAttemptDTO {
  private final UUID id;
  private final QuestionDTO question;
  private final String answer;
  private final boolean correct;
}
