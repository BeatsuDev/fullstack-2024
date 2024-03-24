package no.ntnu.fullstack.backend.question.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
  private String option;
  public QuestionOption(String option) {
    this.option = option;
  }
}
