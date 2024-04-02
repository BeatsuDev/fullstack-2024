package no.ntnu.fullstack.backend.attempt.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import no.ntnu.fullstack.backend.revision.model.Revision;
import no.ntnu.fullstack.backend.user.model.User;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Getter
@Setter
public class QuizAttempt {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @OneToMany(mappedBy = "quizAttempt", fetch = FetchType.EAGER)
  private List<QuestionAttempt> questionAttempts;

  @CreationTimestamp private Date createdAt;

  @ManyToOne private User attemptedBy;

  @ManyToOne private Revision revision;

  public boolean isComplete() {
    if (questionAttempts == null) return false;
    return questionAttempts.size() == revision.getQuestions().size();
  }
}
