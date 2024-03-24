package no.ntnu.fullstack.backend.quiz.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class QuizNotFoundException extends Exception {
  public QuizNotFoundException() {
    super("Quiz not found");
  }
}
