package no.ntnu.fullstack.backend.competition.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Competition already started")
public class CompetitionAlreadyStartedException extends Exception {
  public CompetitionAlreadyStartedException() {
    super("Competition already started");
  }
}
