package no.ntnu.fullstack.backend.attempt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Attempt not found")
public class AttemptNotFoundException extends Exception {
  public AttemptNotFoundException() {
    super("Attempt not found");
  }
}
