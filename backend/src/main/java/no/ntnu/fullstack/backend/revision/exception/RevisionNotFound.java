package no.ntnu.fullstack.backend.revision.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Revision not found")
public class RevisionNotFound extends Exception {
  public RevisionNotFound() {
    super("Revision not found");
  }
}
