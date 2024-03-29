package no.ntnu.fullstack.backend.question.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "At least one option must be correct")
public class NoCorrectOptionException extends Exception {
  public NoCorrectOptionException() {
    super("At least one option must be correct");
  }
}
