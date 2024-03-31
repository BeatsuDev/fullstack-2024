package no.ntnu.fullstack.backend.attempt.dto;

import java.util.UUID;
import lombok.Getter;

@Getter
public class AnswerDTO {
  private UUID questionId;
  private String answer;
}
