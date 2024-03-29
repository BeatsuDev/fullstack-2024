package no.ntnu.fullstack.backend.quiz.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import no.ntnu.fullstack.backend.quiz.model.Quiz;
import no.ntnu.fullstack.backend.quiz.model.QuizWithRevision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuizRepository extends JpaRepository<Quiz, UUID> {

  // TODO: Only return the latest revision for each quiz
  @Query(
      " SELECT new no.ntnu.fullstack.backend.quiz.model.QuizWithRevision(q, r) FROM Quiz q JOIN Revision r ON q.id = r.quiz.id WHERE r.createdAt = ( SELECT MAX(r2.createdAt) FROM Revision r2 WHERE r2.quiz.id = q.id )")
  List<QuizWithRevision> findWithFirstRevision();

  @Query(
      " SELECT new no.ntnu.fullstack.backend.quiz.model.QuizWithRevision(q, r) FROM Quiz q JOIN Revision r ON q.id = r.quiz.id WHERE q.id = :quizId AND r.createdAt = ( SELECT MAX(r2.createdAt) FROM Revision r2 WHERE r2.quiz.id = q.id )")
  Optional<QuizWithRevision> findWithFirstRevision(@Param("quizId") UUID quizId);
}
