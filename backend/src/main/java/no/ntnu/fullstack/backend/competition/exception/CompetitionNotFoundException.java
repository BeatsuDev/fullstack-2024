package no.ntnu.fullstack.backend.competition.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Competition not found")
public class CompetitionNotFoundException extends Exception {
  public CompetitionNotFoundException() {
    super("Competition not found");
  }
}
