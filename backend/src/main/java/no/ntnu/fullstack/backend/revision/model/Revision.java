package no.ntnu.fullstack.backend.revision.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import no.ntnu.fullstack.backend.category.model.Category;
import no.ntnu.fullstack.backend.question.model.Question;
import no.ntnu.fullstack.backend.quiz.model.Quiz;
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

  @OneToMany
  @JoinColumn(name = "category_id")
  private List<Category> categories;

  @OneToMany(mappedBy = "revision", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private List<Question> questions;

  public Revision copy() {
    Revision revision = new Revision();
    revision.setTitle(this.title);
    revision.setDescription(this.description);
    revision.setDifficulty(this.difficulty);
    revision.setQuiz(this.quiz);
    revision.setCategories(this.categories.stream().toList());
    revision.setQuestions(
        this.questions.stream().map(Question::copy).peek(q -> q.setRevision(revision)).toList());
    return revision;
  }
}
