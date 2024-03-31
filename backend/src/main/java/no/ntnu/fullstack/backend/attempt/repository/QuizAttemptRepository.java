package no.ntnu.fullstack.backend.attempt.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import no.ntnu.fullstack.backend.attempt.model.QuizAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, UUID> {
  Optional<QuizAttempt> getQuizAttemptById(UUID id);

  @Query(
      "SELECT qa FROM QuizAttempt qa WHERE qa.attemptedBy.id = :userId AND qa.revision.quiz.id = :quizId order by qa.createdAt desc")
  List<QuizAttempt> getQuizAttemptsByAttemptedByAndQuiz(
      @Param("userId") UUID userId, @Param("userId") UUID quizId);
}
