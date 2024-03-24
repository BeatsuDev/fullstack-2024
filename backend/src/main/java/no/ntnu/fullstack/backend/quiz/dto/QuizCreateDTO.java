package no.ntnu.fullstack.backend.quiz.dto;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** The QuizCreateDTO class represents a quiz creation request. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizCreateDTO {
  private String title;
  private String description;
  private String difficulty;
  private List<UUID> categories;
}
