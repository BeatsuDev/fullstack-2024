package no.ntnu.fullstack.backend.attempt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Question already answered")
public class QuestionAlreadyAnswered extends Exception {
  public QuestionAlreadyAnswered() {
    super("Question already answered");
  }
}
