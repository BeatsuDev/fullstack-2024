package no.ntnu.fullstack.backend.question.model;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class QuestionOption {
  @Id
  @GeneratedValue(generator = "UUID")
  private UUID id;

  @ManyToOne private Question question;

  @Column(name = "question_option")
  private String option;

  public QuestionOption(String option) {
    this.option = option;
  }

  public QuestionOption copy() {
    return new QuestionOption(this.option);
  }
}
