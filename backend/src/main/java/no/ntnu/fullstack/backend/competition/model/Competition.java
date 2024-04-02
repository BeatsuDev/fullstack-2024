package no.ntnu.fullstack.backend.competition.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.ntnu.fullstack.backend.attempt.model.QuizAttempt;
import no.ntnu.fullstack.backend.revision.model.Revision;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Competition {
  private int joinCode;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @OneToMany(fetch = FetchType.EAGER)
  private List<QuizAttempt> quizAttempts;

  @CreationTimestamp private Date created;
  @ManyToOne private Revision revision;
  private Boolean started = false;

  public Competition(Revision latestRevision) {
    randomizeJoinCode();
    this.quizAttempts = List.of();
    this.revision = latestRevision;
  }

  public void randomizeJoinCode() {
    Random random = new Random();
    this.joinCode = random.nextInt(999999);
  }
}
