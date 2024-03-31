package no.ntnu.fullstack.backend.competition.dto;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PreCompetitionInfoDTO {
  private UUID competitionId;
  private CompetitionDTO competition;
}
