package no.ntnu.fullstack.backend.competition;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import no.ntnu.fullstack.backend.competition.dto.CompetitionDTO;
import no.ntnu.fullstack.backend.competition.dto.PreCompetitionInfoDTO;
import no.ntnu.fullstack.backend.competition.exception.CompetitionAlreadyStartedException;
import no.ntnu.fullstack.backend.competition.exception.CompetitionNotFoundException;
import no.ntnu.fullstack.backend.competition.model.Competition;
import no.ntnu.fullstack.backend.quiz.exception.QuizNotFoundException;
import no.ntnu.fullstack.backend.user.model.User;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CompetitionController {
  private final CompetitionService competitionService;
  private final CompetitionMapper competitionMapper = Mappers.getMapper(CompetitionMapper.class);

  @PostMapping("/quiz/{quizId}/competition")
  public ResponseEntity<CompetitionDTO> createCompetition(@PathVariable UUID quizId)
      throws QuizNotFoundException {
    Competition competition = competitionService.createCompetition(quizId);
    return ResponseEntity.ok(competitionMapper.toDTO(competition));
  }

  @PostMapping("/competition/{joinCode}")
  public ResponseEntity<PreCompetitionInfoDTO> joinCompetition(
      Authentication authentication, @PathVariable("joinCode") Integer joinCode)
      throws CompetitionNotFoundException, CompetitionAlreadyStartedException {
    User loggedInUser = (User) authentication.getPrincipal();
    Competition competition = competitionService.joinCompetition(joinCode, loggedInUser);
    return ResponseEntity.ok(competitionMapper.toPreCompetitionInfoDTO(competition));
  }

  @GetMapping("/competition/{joinCode}")
  public ResponseEntity<CompetitionDTO> getCompetition(
      Authentication authentication, @PathVariable Integer joinCode)
      throws CompetitionNotFoundException {
    User loggedInUser = (User) authentication.getPrincipal();
    Competition competition = competitionService.getCompetition(joinCode, loggedInUser);
    return ResponseEntity.ok(competitionMapper.toDTO(competition));
  }

  @PutMapping("/competition/{joinCode}")
  public ResponseEntity<?> startCompetition(
      Authentication authentication, @PathVariable Integer joinCode)
      throws CompetitionNotFoundException, CompetitionAlreadyStartedException {
    User loggedInUser = (User) authentication.getPrincipal();
    competitionService.startCompetition(joinCode, loggedInUser);
    return ResponseEntity.status(HttpStatus.OK).build();
  }
}
