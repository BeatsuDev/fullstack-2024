package no.ntnu.fullstack.backend.category.repository;

import java.util.UUID;
import no.ntnu.fullstack.backend.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, UUID> {}
