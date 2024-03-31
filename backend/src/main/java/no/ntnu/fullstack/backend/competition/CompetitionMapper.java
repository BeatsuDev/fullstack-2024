package no.ntnu.fullstack.backend.competition;

import no.ntnu.fullstack.backend.attempt.AttemptMapper;
import no.ntnu.fullstack.backend.competition.dto.CompetitionDTO;
import no.ntnu.fullstack.backend.competition.dto.PreCompetitionInfoDTO;
import no.ntnu.fullstack.backend.competition.model.Competition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(uses = {AttemptMapper.class})
public abstract class CompetitionMapper {
  public abstract CompetitionDTO toDTO(Competition competition);

  public String getWebSocketUrl() {
    return "ws://localhost:8080";
  }

  @Mappings({
    @Mapping(target = "webSocketUrl", expression = "java(getWebSocketUrl())"),
    @Mapping(target = "competition", source = "competition")
  })
  public abstract PreCompetitionInfoDTO toPreCompetitionInfoDTO(Competition competition);
}
