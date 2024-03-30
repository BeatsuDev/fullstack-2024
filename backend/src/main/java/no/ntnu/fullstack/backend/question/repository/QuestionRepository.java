package no.ntnu.fullstack.backend.question.repository;

import java.util.Optional;
import java.util.UUID;
import no.ntnu.fullstack.backend.question.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, UUID> {
  /**
   * Select latest revision of question by questionId
   *
   * @param questionId The question id of the question
   * @return The question with the given questionId
   */
  @Query(
      "SELECT q FROM Question q WHERE q.questionId = :questionId ORDER BY q.revision.createdAt DESC LIMIT 1")
  Optional<Question> findLatestByQuestionId(@Param("questionId") UUID questionId);
}
