package no.ntnu.fullstack.backend.competition;

import no.ntnu.fullstack.backend.attempt.AttemptMapper;
import no.ntnu.fullstack.backend.attempt.model.QuizAttempt;
import no.ntnu.fullstack.backend.competition.dto.CompetitionDTO;
import no.ntnu.fullstack.backend.competition.dto.CompetitorDTO;
import no.ntnu.fullstack.backend.competition.dto.PreCompetitionInfoDTO;
import no.ntnu.fullstack.backend.competition.model.Competition;
import no.ntnu.fullstack.backend.user.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(uses = {AttemptMapper.class, UserMapper.class})
public abstract class CompetitionMapper {

  @Mappings({
    @Mapping(target = "user", source = "quizAttempt.attemptedBy"),
    @Mapping(target = "attempt", source = "quizAttempt")
  })
  public abstract CompetitorDTO toCompetitorDTO(QuizAttempt quizAttempt);

  @Mappings({
    @Mapping(target = "competitors", source = "competition.quizAttempts"),
    @Mapping(target = "joinCode", source = "competition.joinCode")
  })
  public abstract CompetitionDTO toDTO(Competition competition);

  @Mappings({
    @Mapping(target = "competitionId", source = "competition.id"),
    @Mapping(target = "competition", source = "competition")
  })
  public abstract PreCompetitionInfoDTO toPreCompetitionInfoDTO(Competition competition);
}
