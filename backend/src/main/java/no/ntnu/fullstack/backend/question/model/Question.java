package no.ntnu.fullstack.backend.question.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import lombok.*;
import no.ntnu.fullstack.backend.quiz.model.Revision;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Question {
  @Id
  @GeneratedValue(generator = "UUID")
  private UUID id;

  /** The id of the question, regardless of revision. */
  @NotNull private UUID questionId;

  @ManyToOne private Revision revision;

  @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<QuestionOption> options;

  private int sequenceNumber;
  private String question;
  private String answer;
}
