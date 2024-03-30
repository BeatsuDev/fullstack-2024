package no.ntnu.fullstack.backend.revision;

import java.util.List;
import java.util.UUID;
import no.ntnu.fullstack.backend.revision.model.Revision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RevisionRepository extends JpaRepository<Revision, UUID> {
  @Query("SELECT r FROM Revision r WHERE r.quiz.id = :quizId order by r.createdAt desc")
  List<Revision> findByQuizId(@Param("quizId") UUID quizId);
}
