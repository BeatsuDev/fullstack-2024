package no.ntnu.fullstack.backend.image;

import java.util.UUID;
import no.ntnu.fullstack.backend.image.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, UUID> {}
