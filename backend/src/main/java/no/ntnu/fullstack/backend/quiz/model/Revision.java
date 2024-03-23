package no.ntnu.fullstack.backend.quiz.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.Date;
import java.util.UUID;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import no.ntnu.fullstack.backend.user.model.User;
import org.hibernate.annotations.CreationTimestamp;

/**
 * The Revision entity represents a version of a quiz. The latest revision is the one that is
 * currently active.
 */
@Getter
@Setter
@Entity
public class Revision {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @NotBlank @NonNull private String title;

  @NotBlank @NonNull private String description;

  @Min(1)
  private int difficulty;

  @ManyToOne(cascade = CascadeType.DETACH)
  private User creator;

  @CreationTimestamp private Date createdAt;

  @ManyToOne
  @JoinColumn(name = "quiz_id")
  private Quiz quiz;
}
