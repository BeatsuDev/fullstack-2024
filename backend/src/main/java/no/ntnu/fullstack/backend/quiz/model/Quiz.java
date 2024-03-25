package no.ntnu.fullstack.backend.quiz.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.ntnu.fullstack.backend.user.model.User;
import org.hibernate.annotations.CreationTimestamp;

/**
 * The Quiz entity represents a quiz that can be created by a user. A quiz consists of a list of
 * revisions, where each revision represents a version of the quiz. The latest revision is the one
 * that is currently active.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Quiz {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne(cascade = CascadeType.DETACH)
  private User creator;

  @CreationTimestamp private Date createdAt;

  @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
  private List<Revision> revisions;
}
