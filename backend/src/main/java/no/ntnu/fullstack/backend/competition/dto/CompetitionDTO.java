package no.ntnu.fullstack.backend.competition.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import no.ntnu.fullstack.backend.attempt.dto.QuizAttemptDTO;

@Getter
@Setter
public class CompetitionDTO {
  List<QuizAttemptDTO> attempts;
  int joinCode;
}
