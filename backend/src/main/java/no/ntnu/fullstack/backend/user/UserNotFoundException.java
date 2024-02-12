package no.ntnu.fullstack.backend.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends Exception {
  public UserNotFoundException() {
    super("User not found");
  }
}
