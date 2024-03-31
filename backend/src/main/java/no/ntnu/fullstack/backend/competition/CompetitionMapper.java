package no.ntnu.fullstack.backend.competition;

import no.ntnu.fullstack.backend.attempt.AttemptMapper;
import no.ntnu.fullstack.backend.competition.dto.CompetitionDTO;
import no.ntnu.fullstack.backend.competition.model.Competition;
import org.mapstruct.Mapper;

@Mapper(uses = {AttemptMapper.class})
public abstract class CompetitionMapper {
  public abstract CompetitionDTO toDTO(Competition competition);
}
