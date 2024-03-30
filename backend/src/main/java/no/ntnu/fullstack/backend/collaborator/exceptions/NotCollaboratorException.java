package no.ntnu.fullstack.backend.collaborator.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "User is not a collaborator of the quiz")
public class NotCollaboratorException extends Exception {
  public NotCollaboratorException() {
    super("User is not a collaborator of the quiz");
  }
}
