package no.ntnu.fullstack.backend.revision;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import no.ntnu.fullstack.backend.colleborator.exceptions.NotCollaboratorException;
import no.ntnu.fullstack.backend.quiz.dto.QuizDTO;
import no.ntnu.fullstack.backend.quiz.exception.QuizNotFoundException;
import no.ntnu.fullstack.backend.quiz.mapper.QuizMapper;
import no.ntnu.fullstack.backend.quiz.model.Quiz;
import no.ntnu.fullstack.backend.quiz.model.QuizWithRevision;
import no.ntnu.fullstack.backend.revision.dto.RevisionDTO;
import no.ntnu.fullstack.backend.revision.exception.RevisionNotFound;
import no.ntnu.fullstack.backend.revision.model.Revision;
import org.mapstruct.factory.Mappers;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("quiz/{quizId}/revision")
@RequiredArgsConstructor
public class RevisionController {
  private final RevisionService revisionService;
  private final QuizMapper quizMapper = Mappers.getMapper(QuizMapper.class);
  private final RevisionMapper revisionMapper = Mappers.getMapper(RevisionMapper.class);

  @GetMapping
  public ResponseEntity<List<RevisionDTO>> getRevisions(@PathVariable("quizId") UUID quizId)
      throws QuizNotFoundException {
    Pair<Quiz, List<Revision>> revisions = revisionService.getAllRevisions(quizId);
    return ResponseEntity.ok(revisionMapper.toRevisionDTOs(revisions));
  }

  @PutMapping("/{revisionId}")
  public ResponseEntity<QuizDTO> revertToRevision(
      @PathVariable("quizId") UUID quizId, @PathVariable("revisionId") UUID revisionId)
      throws QuizNotFoundException, NotCollaboratorException, RevisionNotFound {
    QuizWithRevision quizWithRevision = revisionService.revertToRevision(quizId, revisionId);
    return ResponseEntity.ok(
        quizMapper.toQuizDTO(quizWithRevision.getQuiz(), quizWithRevision.getLatestRevision()));
  }
}
