package no.ntnu.fullstack.backend.attempt.repository;

import java.util.UUID;
import no.ntnu.fullstack.backend.attempt.model.QuestionAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionAttemptRepository extends JpaRepository<QuestionAttempt, UUID> {}
