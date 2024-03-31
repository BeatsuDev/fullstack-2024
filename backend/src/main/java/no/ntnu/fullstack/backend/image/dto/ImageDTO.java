package no.ntnu.fullstack.backend.image.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class ImageDTO {
  private final UUID id;
  private final String path;
}
