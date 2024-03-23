package no.ntnu.fullstack.backend.quiz;

import java.util.UUID;
import no.ntnu.fullstack.backend.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<User, UUID> {}
