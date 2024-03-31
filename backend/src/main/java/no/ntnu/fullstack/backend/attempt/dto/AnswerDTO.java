package no.ntnu.fullstack.backend.attempt.dto;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerDTO {
  private UUID questionId;
  private String answer;
}
