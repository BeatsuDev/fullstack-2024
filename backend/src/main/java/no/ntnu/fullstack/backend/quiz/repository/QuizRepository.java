package no.ntnu.fullstack.backend.quiz.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import no.ntnu.fullstack.backend.quiz.dto.QuizFilters;
import no.ntnu.fullstack.backend.quiz.model.Quiz;
import no.ntnu.fullstack.backend.quiz.model.QuizWithRevision;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

  @Query(
      """
SELECT new no.ntnu.fullstack.backend.quiz.model.QuizWithRevision(q, r)
  FROM Quiz q JOIN Revision r ON q.id = r.quiz.id
  WHERE
    r.createdAt = ( SELECT MAX(r2.createdAt) FROM Revision r2 WHERE q.id = r2.quiz.id )
    AND (:#{#filter.textSearch} IS NULL OR :#{#filter.textSearch.length()}=0 OR r.title LIKE %:#{#filter.textSearch}% OR r.description LIKE %:#{#filter.textSearch}%)
    AND (:#{#filter.minDifficulty} IS NULL OR r.difficulty >= :#{#filter.minDifficulty})
    AND (:#{#filter.maxDifficulty} IS NULL OR r.difficulty <= :#{#filter.maxDifficulty})
    AND (:#{#filter.category} IS NULL OR :#{#filter.category.size()}=0 OR (SELECT COUNT(c.id) FROM  r.categories c WHERE c.id IN :#{#filter.category}) > 0)
    AND (:#{#filter.creator} IS NULL OR q.creator.id = :#{#filter.creator})
    AND (:#{#filter.collaborator} IS NULL OR :#{#filter.collaborator} IN (SELECT c.id FROM q.collaborators c))
""")
  Page<QuizWithRevision> findByFilter(@Param("filter") QuizFilters filter, Pageable pageable);
}
