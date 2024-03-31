package no.ntnu.fullstack.backend.attempt.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerDTO {
  @NotNull private UUID questionId;
  @NotBlank private String answer;
}
