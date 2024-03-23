package no.ntnu.fullstack.backend.quiz;

import java.util.UUID;

import no.ntnu.fullstack.backend.quiz.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, UUID> {}
