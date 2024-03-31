package no.ntnu.fullstack.backend.question.dto;

import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import no.ntnu.fullstack.backend.image.dto.ImageDTO;

@Getter
@Setter
@RequiredArgsConstructor
public class QuestionDTO {
  private final UUID id;
  private final String question;
  private final String answer;
  private final List<String> options;
  private final ImageDTO image;
}
