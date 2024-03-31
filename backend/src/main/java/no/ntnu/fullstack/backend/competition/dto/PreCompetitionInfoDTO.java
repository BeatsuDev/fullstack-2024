package no.ntnu.fullstack.backend.competition.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PreCompetitionInfoDTO {
  private String webSocketUrl;
  private CompetitionDTO competition;
}
