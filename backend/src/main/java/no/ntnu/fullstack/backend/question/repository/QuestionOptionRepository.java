package no.ntnu.fullstack.backend.question.repository;

import java.util.UUID;
import no.ntnu.fullstack.backend.question.model.QuestionOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionOptionRepository extends JpaRepository<QuestionOption, UUID> {}
