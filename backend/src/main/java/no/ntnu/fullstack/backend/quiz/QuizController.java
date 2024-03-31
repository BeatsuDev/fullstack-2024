package no.ntnu.fullstack.backend.quiz;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import no.ntnu.fullstack.backend.collaborator.CollaboratorService;
import no.ntnu.fullstack.backend.collaborator.exceptions.NotCollaboratorException;
import no.ntnu.fullstack.backend.quiz.dto.QuizCreateDTO;
import no.ntnu.fullstack.backend.quiz.dto.QuizDTO;
import no.ntnu.fullstack.backend.quiz.dto.QuizFilters;
import no.ntnu.fullstack.backend.quiz.dto.QuizOverviewDTO;
import no.ntnu.fullstack.backend.quiz.exception.NoQuizzesFoundException;
import no.ntnu.fullstack.backend.quiz.exception.QuizNotFoundException;
import no.ntnu.fullstack.backend.quiz.mapper.QuizMapper;
import no.ntnu.fullstack.backend.quiz.model.Quiz;
import no.ntnu.fullstack.backend.quiz.model.QuizWithRevision;
import no.ntnu.fullstack.backend.revision.RevisionMapper;
import no.ntnu.fullstack.backend.revision.RevisionService;
import no.ntnu.fullstack.backend.revision.model.Revision;
import no.ntnu.fullstack.backend.user.model.User;
import org.mapstruct.factory.Mappers;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/quiz")
@RequiredArgsConstructor
public class QuizController {
  private final QuizService quizService;
  private final QuizMapper quizMapper = Mappers.getMapper(QuizMapper.class);
  private final RevisionService revisionService;
  private final RevisionMapper revisionMapper = Mappers.getMapper(RevisionMapper.class);
  private final CollaboratorService collaboratorService;

  /**
   * Create a new quiz with the given quiz data that becomes the first revision.
   *
   * @param authentication The authentication object.
   * @param createQuiz The quiz data to create.
   * @return The created quiz.
   */
  @PostMapping
  @ResponseBody
  public ResponseEntity<QuizDTO> createQuiz(
      Authentication authentication, @Valid @RequestBody QuizCreateDTO createQuiz) {
    User user = (User) authentication.getPrincipal();
    Quiz quiz = quizMapper.fromCreateQuiz(user);
    Revision revision = revisionMapper.fromQuizCreate(createQuiz, user);
    QuizWithRevision createdQuiz = quizService.createQuiz(quiz, revision);
    return ResponseEntity.ok(
        quizMapper.toQuizDTO(createdQuiz.getQuiz(), createdQuiz.getLatestRevision()));
  }

  @GetMapping
  @ResponseBody
  public ResponseEntity<List<QuizOverviewDTO>> listQuiz(
      @RequestParam(required = false) Integer page,
      @RequestParam(required = false) Integer pageSize,
      @RequestParam(required = false) String textSearch,
      @RequestParam(required = false) Integer minDifficulty,
      @RequestParam(required = false) Integer maxDifficulty,
      @RequestParam(required = false) UUID collaborator,
      @RequestParam(required = false) UUID creator,
      @RequestParam(required = false) List<UUID> category)
      throws NoQuizzesFoundException {
    QuizFilters filters =
        new QuizFilters(
            page,
            pageSize,
            textSearch,
            minDifficulty,
            maxDifficulty,
            category,
            creator,
            collaborator);
    List<QuizWithRevision> quizzes = quizService.retrieveQuizzes(filters);
    return ResponseEntity.ok(
        quizzes.stream()
            .map(quiz -> quizMapper.toQuizOverviewDTO(quiz.getQuiz(), quiz.getLatestRevision()))
            .toList());
  }

  @GetMapping("/{id}")
  @ResponseBody
  public ResponseEntity<QuizDTO> getQuiz(@PathVariable("id") UUID id) throws QuizNotFoundException {
    QuizWithRevision quiz = quizService.getLatestQuiz(id);
    return ResponseEntity.ok(quizMapper.toQuizDTO(quiz.getQuiz(), quiz.getLatestRevision()));
  }

  @PutMapping("/{id}")
  @ResponseBody
  public ResponseEntity<QuizDTO> updateQuiz(
      Authentication authentication,
      @PathVariable("id") UUID id,
      @Valid @RequestBody QuizCreateDTO updateQuiz)
      throws QuizNotFoundException, NotCollaboratorException {
    Revision newRevision =
        revisionMapper.fromQuizCreate(updateQuiz, (User) authentication.getPrincipal());
    QuizWithRevision quiz = revisionService.updateQuizInfo(id, newRevision);

    if (!collaboratorService.loggedInUserIsCollaborator(quiz.getQuiz()))
      quiz.getLatestRevision().getQuestions().forEach(q -> q.setAnswer(null));

    return ResponseEntity.ok(quizMapper.toQuizDTO(quiz.getQuiz(), quiz.getLatestRevision()));
  }
}
