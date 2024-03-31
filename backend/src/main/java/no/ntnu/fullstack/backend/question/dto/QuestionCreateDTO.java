package no.ntnu.fullstack.backend.question.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class QuestionCreateDTO {
  @NotBlank private String question;
  @NotNull private UUID quizId;
  @NotBlank private String answer;
  @NotNull private List<String> options;
  private UUID imageId;
}
