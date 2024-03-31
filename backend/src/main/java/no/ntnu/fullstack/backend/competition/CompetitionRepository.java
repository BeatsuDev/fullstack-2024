package no.ntnu.fullstack.backend.competition;

import java.util.Optional;
import java.util.UUID;
import no.ntnu.fullstack.backend.competition.model.Competition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CompetitionRepository extends JpaRepository<Competition, UUID> {
  @Query("SELECT c FROM Competition c WHERE c.joinCode = :joinCode AND c.started = false")
  Optional<Competition> findByJoinCode(@Param("joinCode") int joinCode);
}
