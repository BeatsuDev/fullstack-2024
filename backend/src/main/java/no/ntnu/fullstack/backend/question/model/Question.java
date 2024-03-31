package no.ntnu.fullstack.backend.question.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import lombok.*;
import no.ntnu.fullstack.backend.image.model.Image;
import no.ntnu.fullstack.backend.revision.model.Revision;

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

  @ManyToOne private Image image;

  @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<QuestionOption> options;

  private int sequenceNumber;
  private String question;
  private String answer;

  public Question copy() {
    Question question = new Question();
    question.setQuestionId(this.questionId);
    question.setOptions(
        this.options.stream()
            .map(QuestionOption::copy)
            .peek(o -> o.setQuestion(question))
            .toList());
    question.setSequenceNumber(this.sequenceNumber);
    question.setQuestion(this.question);
    question.setAnswer(this.answer);
    question.setImage(this.image);
    return question;
  }
}
