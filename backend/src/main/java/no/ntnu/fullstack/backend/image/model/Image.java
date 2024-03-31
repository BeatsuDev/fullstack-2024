package no.ntnu.fullstack.backend.image.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import no.ntnu.fullstack.backend.user.model.User;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Setter
@Getter
public class Image {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne private User uploadedBy;
  @CreationTimestamp private Date uploadedAt;
  private String extension;
}
