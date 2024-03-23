package no.ntnu.fullstack.backend.quiz.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** The LatestQuiz entity represents a quiz with its latest revision. */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LatestQuiz {
  private Quiz quiz;
  private Revision latestRevision;
}
