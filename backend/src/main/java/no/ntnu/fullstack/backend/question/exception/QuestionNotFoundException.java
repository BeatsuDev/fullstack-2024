package no.ntnu.fullstack.backend.question.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class QuestionNotFoundException extends Exception {
  public QuestionNotFoundException() {
    super("Question not found");
  }
}
