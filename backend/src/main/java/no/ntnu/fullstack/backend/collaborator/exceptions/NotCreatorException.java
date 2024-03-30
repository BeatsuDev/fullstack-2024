package no.ntnu.fullstack.backend.collaborator.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "User is not the creator of the quiz")
public class NotCreatorException extends Exception {
  public NotCreatorException() {
    super("User is not the creator of the quiz");
  }
}
