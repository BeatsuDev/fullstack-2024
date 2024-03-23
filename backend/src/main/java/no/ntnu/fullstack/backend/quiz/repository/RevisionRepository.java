package no.ntnu.fullstack.backend.quiz.repository;

import java.util.UUID;
import no.ntnu.fullstack.backend.quiz.model.Revision;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RevisionRepository extends JpaRepository<Revision, UUID> {}
