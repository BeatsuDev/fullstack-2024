package no.ntnu.fullstack.backend.revision.dto;

import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import no.ntnu.fullstack.backend.quiz.dto.QuizDTO;

@Getter
@RequiredArgsConstructor
public class RevisionDTO {
  private final UUID revisionId;
  private final QuizDTO quiz;
}
