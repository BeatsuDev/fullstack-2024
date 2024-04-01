package no.ntnu.fullstack.backend.competition.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompetitionDTO {
  List<CompetitorDTO> competitors;
  int joinCode;
}
