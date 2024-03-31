package no.ntnu.fullstack.backend.image;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import no.ntnu.fullstack.backend.image.exception.ImageNotFound;
import no.ntnu.fullstack.backend.image.exception.MalformedImageException;
import no.ntnu.fullstack.backend.image.model.Image;
import no.ntnu.fullstack.backend.user.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageService {
  private final ImageRepository imageRepository;

  @Value("${application.image.upload.dir}")
  private String UPLOAD_DIR;

  public Image uploadImage(MultipartFile image, User uploader)
      throws MalformedImageException, IOException {
    if (image.getContentType() == null || !image.getContentType().startsWith("image/")) {
      throw new MalformedImageException();
    }
    String filename = image.getOriginalFilename();
    if (filename == null || filename.isEmpty()) {
      throw new MalformedImageException();
    }

    String extension = filename.substring(filename.lastIndexOf('.'));
    if (Stream.of(".jpg", ".jpeg", ".png").noneMatch(extension::equalsIgnoreCase)) {
      throw new MalformedImageException();
    }

    Image newImage = new Image();
    newImage.setUploadedBy(uploader);
    newImage.setExtension(extension);
    newImage = imageRepository.saveAndFlush(newImage);

    Path imagePath = Paths.get(UPLOAD_DIR + "/" + newImage.getId() + extension);
    image.transferTo(imagePath);
    return newImage;
  }

  public Image getImage(UUID id) throws ImageNotFound {
    return imageRepository.findById(id).orElseThrow(ImageNotFound::new);
  }
}
