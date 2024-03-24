package no.ntnu.fullstack.backend.quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.ntnu.fullstack.backend.category.model.Category;

import java.util.List;

/** The QuizCreateDTO class represents a quiz creation request. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizCreateDTO {
  private String title;
  private String description;
  private String difficulty;
  private List<Category> categories;
}
