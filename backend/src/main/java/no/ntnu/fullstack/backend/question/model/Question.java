package no.ntnu.fullstack.backend.question.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import no.ntnu.fullstack.backend.quiz.model.Revision;

@Entity
@Getter
@Setter
public class Question {
  @Id
  @GeneratedValue(generator = "UUID")
  private UUID id;

  @ManyToOne private Revision revision;

  @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<QuestionOption> options;

  private int sequenceNumber;
  private String question;
  private String answer;
}
