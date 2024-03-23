package no.ntnu.fullstack.backend.quiz;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import no.ntnu.fullstack.backend.quiz.dto.QuizCreateDTO;
import no.ntnu.fullstack.backend.quiz.dto.QuizDTO;
import no.ntnu.fullstack.backend.quiz.mapper.QuizMapper;
import no.ntnu.fullstack.backend.quiz.mapper.RevisionMapper;
import no.ntnu.fullstack.backend.quiz.model.Quiz;
import no.ntnu.fullstack.backend.quiz.model.LatestQuiz;
import no.ntnu.fullstack.backend.quiz.model.Revision;
import no.ntnu.fullstack.backend.user.model.User;
import org.mapstruct.factory.Mappers;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/quiz")
@RequiredArgsConstructor
public class QuizController {
  private final QuizService quizService;
  private final QuizMapper quizMapper = Mappers.getMapper(QuizMapper.class);
  private final RevisionMapper revisionMapper = Mappers.getMapper(RevisionMapper.class);

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
    Quiz createdQuiz = quizService.createQuiz(quiz, revision);
    return ResponseEntity.ok(quizMapper.toDTO(createdQuiz, revision));
  }

  @GetMapping
  @ResponseBody
  public ResponseEntity<List<QuizDTO>> listQuiz() {
    List<LatestQuiz> quizzes = quizService.retrieveQuizzes();
    return ResponseEntity.ok( quizzes.stream().map(quiz -> quizMapper.toDTO(quiz.getQuiz(), quiz.getLatestRevision())).toList() );
  }
}
