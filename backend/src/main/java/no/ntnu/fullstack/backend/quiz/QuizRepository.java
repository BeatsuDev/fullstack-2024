package no.ntnu.fullstack.backend.quiz;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import no.ntnu.fullstack.backend.user.model.User;

public interface QuizRepository extends JpaRepository<User, UUID> {}
