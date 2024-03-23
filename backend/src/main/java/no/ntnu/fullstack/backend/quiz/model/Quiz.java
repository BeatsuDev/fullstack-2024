package no.ntnu.fullstack.backend.quiz.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "_user")
public class Quiz {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

}
