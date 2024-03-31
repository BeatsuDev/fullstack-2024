package no.ntnu.fullstack.backend.image.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Malformed image")
public class MalformedImageException extends Exception {
  public MalformedImageException() {
    super("Malformed image");
  }
}
