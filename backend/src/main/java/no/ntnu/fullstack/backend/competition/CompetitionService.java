package no.ntnu.fullstack.backend.competition;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import no.ntnu.fullstack.backend.attempt.AttemptService;
import no.ntnu.fullstack.backend.attempt.model.QuizAttempt;
import no.ntnu.fullstack.backend.competition.exception.CompetitionAlreadyStartedException;
import no.ntnu.fullstack.backend.competition.exception.CompetitionNotFoundException;
import no.ntnu.fullstack.backend.competition.model.Competition;
import no.ntnu.fullstack.backend.quiz.QuizService;
import no.ntnu.fullstack.backend.quiz.exception.QuizNotFoundException;
import no.ntnu.fullstack.backend.quiz.model.QuizWithRevision;
import no.ntnu.fullstack.backend.user.model.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompetitionService {
  private final CompetitionRepository competitionRepository;
  private final AttemptService attemptService;
  private final QuizService quizService;

  public Competition createCompetition(UUID quizId) throws QuizNotFoundException {
    QuizWithRevision quizWithRevision = quizService.getLatestQuiz(quizId);
    Competition competition = new Competition(quizWithRevision.getLatestRevision());

    while (competitionRepository.findByJoinCode(competition.getJoinCode()).isPresent())
      competition.randomizeJoinCode();
    return competitionRepository.saveAndFlush(competition);
  }

  public Competition joinCompetition(Integer joinCode, User user)
      throws CompetitionNotFoundException, CompetitionAlreadyStartedException {
    Competition competition =
        competitionRepository
            .findByJoinCode(joinCode)
            .orElseThrow(CompetitionNotFoundException::new);
    if (competition.getQuizAttempts().stream()
        .anyMatch(attempt -> attempt.getAttemptedBy().getId().equals(user.getId())))
      throw new CompetitionNotFoundException();

    if (competition.getStarted()) throw new CompetitionAlreadyStartedException();

    if (competition.getQuizAttempts().stream()
        .anyMatch(qa -> qa.getAttemptedBy().getId().equals(user.getId()))) {
      return competition;
    }

    QuizAttempt attempt = attemptService.createAttempt(competition.getRevision(), user);
    competition.getQuizAttempts().add(attempt);
    return competitionRepository.saveAndFlush(competition);
  }

  public Competition getCompetition(Integer joinCode) throws CompetitionNotFoundException {
    return competitionRepository
        .findByJoinCode(joinCode)
        .orElseThrow(CompetitionNotFoundException::new);
  }
}
