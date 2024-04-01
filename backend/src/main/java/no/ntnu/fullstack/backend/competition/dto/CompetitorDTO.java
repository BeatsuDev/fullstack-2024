package no.ntnu.fullstack.backend.competition.dto;

import lombok.Getter;
import lombok.Setter;
import no.ntnu.fullstack.backend.attempt.dto.QuizAttemptDTO;
import no.ntnu.fullstack.backend.user.dto.UserDTO;

@Getter
@Setter
public class CompetitorDTO {
  private UserDTO user;
  private QuizAttemptDTO attempt;
}
