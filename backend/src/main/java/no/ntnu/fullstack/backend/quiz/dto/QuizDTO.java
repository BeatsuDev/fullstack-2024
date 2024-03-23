package no.ntnu.fullstack.backend.quiz.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.ntnu.fullstack.backend.user.dto.UserDTO;
import no.ntnu.fullstack.backend.user.model.User;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class QuizDTO {
  private String title;
  private String description;
  private String difficulty;

  private UserDTO creator;

  private Date createdAt;
}
