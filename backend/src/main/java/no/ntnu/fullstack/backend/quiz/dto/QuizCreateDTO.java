package no.ntnu.fullstack.backend.quiz.dto;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/** The QuizCreateDTO class represents a quiz creation request. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizCreateDTO {
  @NotBlank(message = "Title cannot be blank")
  @NonNull
  private String title;

  @NonNull
  @NotBlank(message = "Description cannot be blank")
  private String description;

  @Min(value = 1, message = "Difficulty must be at least 1")
  private int difficulty;

  private List<UUID> categories;
}
