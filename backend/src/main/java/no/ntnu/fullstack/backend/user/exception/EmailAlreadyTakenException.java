package no.ntnu.fullstack.backend.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Email already taken")
public class EmailAlreadyTakenException extends Exception {
  public EmailAlreadyTakenException() {
    super("Email already taken");
  }
}
