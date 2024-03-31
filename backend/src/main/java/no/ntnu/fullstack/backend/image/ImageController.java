package no.ntnu.fullstack.backend.image;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import no.ntnu.fullstack.backend.image.dto.ImageDTO;
import no.ntnu.fullstack.backend.image.exception.MalformedImageException;
import no.ntnu.fullstack.backend.user.model.User;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageController {
  private final ImageService imageService;
  private final ImageMapper imageMapper = Mappers.getMapper(ImageMapper.class);

  @PostMapping
  public ResponseEntity<ImageDTO> uploadImage(
      Authentication authentication, @RequestParam("image") MultipartFile image)
      throws MalformedImageException, IOException {
    User loggedInUser = (User) authentication.getPrincipal();
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(imageMapper.fromImage(imageService.uploadImage(image, loggedInUser)));
  }
}
