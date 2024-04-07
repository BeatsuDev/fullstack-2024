package no.ntnu.fullstack.backend.attempt.model;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import no.ntnu.fullstack.backend.question.model.Question;

@Entity
@Getter
@Setter
public class QuestionAttempt {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne private Question question;
  @ManyToOne private QuizAttempt quizAttempt;
  private String answer;

  public boolean isCorrect() {
    return question.getAnswer().equalsIgnoreCase(answer);
  }
}
