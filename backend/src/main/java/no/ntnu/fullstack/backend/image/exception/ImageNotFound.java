package no.ntnu.fullstack.backend.image.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Image not found")
public class ImageNotFound extends Exception {
  public ImageNotFound() {
    super("Image not found");
  }
}
