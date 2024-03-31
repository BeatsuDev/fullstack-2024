package no.ntnu.fullstack.backend.image;

import no.ntnu.fullstack.backend.image.dto.ImageDTO;
import no.ntnu.fullstack.backend.image.model.Image;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Value;

@Mapper
public abstract class ImageMapper {
  private String UPLOAD_DIR = "uploads/images/";

  public ImageDTO fromImage(Image image) {
    if (image == null) {
      return null;
    }

    String uploadDir = null;
    if (image.getId() != null && image.getExtension() != null) {
      uploadDir = UPLOAD_DIR + image.getId() + image.getExtension();
    }

    return new ImageDTO(image.getId(), uploadDir);
  }
}
