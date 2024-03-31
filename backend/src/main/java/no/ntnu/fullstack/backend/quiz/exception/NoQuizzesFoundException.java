package no.ntnu.fullstack.backend.quiz.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "No quizzes found")
public class NoQuizzesFoundException extends Exception {
  public NoQuizzesFoundException() {
    super("No quizzes found");
  }
}
