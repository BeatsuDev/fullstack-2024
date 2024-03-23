package no.ntnu.fullstack.backend.quiz.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import no.ntnu.fullstack.backend.user.model.User;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Setter
@Entity
public class Quiz {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne(cascade = CascadeType.DETACH)
  private User creator;

  @CreationTimestamp private Date createdAt;

  @OneToMany(cascade = CascadeType.REMOVE)
  private List<Revision> revisions;
}
