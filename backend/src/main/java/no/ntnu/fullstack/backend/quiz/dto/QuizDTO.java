package no.ntnu.fullstack.backend.quiz.dto;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.ntnu.fullstack.backend.category.model.Category;
import no.ntnu.fullstack.backend.question.dto.QuestionDTO;
import no.ntnu.fullstack.backend.user.dto.UserDTO;

/** The QuizDTO class represents a quiz with a revision */
@Getter
@Setter
@NoArgsConstructor
public class QuizDTO {
  private UUID id;
  private String title;
  private String description;
  private int difficulty;
  private UserDTO creator;
  private Date createdAt;
  private List<Category> categories;
  private List<QuestionDTO> questions;
}
