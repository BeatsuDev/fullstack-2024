package no.ntnu.fullstack.backend.quiz.dto;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import no.ntnu.fullstack.backend.category.model.Category;
import no.ntnu.fullstack.backend.user.dto.UserDTO;

@Getter
@Setter
public class QuizOverviewDTO {
  private UUID id;
  private String title;
  private String description;
  private int difficulty;
  private UserDTO creator;
  private Date createdAt;
  private List<Category> categories;
}
