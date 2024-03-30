package no.ntnu.fullstack.backend.quiz.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.ntnu.fullstack.backend.revision.model.Revision;

/** The QuizWithRevision entity represents a quiz with its latest revision. */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuizWithRevision {
  private Quiz quiz;
  private Revision latestRevision;
}
