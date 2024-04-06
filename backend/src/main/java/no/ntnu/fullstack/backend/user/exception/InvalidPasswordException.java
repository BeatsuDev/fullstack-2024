package no.ntnu.fullstack.backend.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Invalid password")
public class InvalidPasswordException extends Exception {
  public InvalidPasswordException() {
    super("Invalid password");
  }
}
