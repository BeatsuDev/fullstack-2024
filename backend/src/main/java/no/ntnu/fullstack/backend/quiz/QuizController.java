package no.ntnu.fullstack.backend.quiz;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import no.ntnu.fullstack.backend.quiz.dto.QuizCreateDTO;
import no.ntnu.fullstack.backend.quiz.dto.QuizDTO;
import no.ntnu.fullstack.backend.quiz.model.Quiz;
import no.ntnu.fullstack.backend.quiz.model.Revision;
import no.ntnu.fullstack.backend.user.model.User;
import org.mapstruct.factory.Mappers;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/quiz")
@RequiredArgsConstructor
public class QuizController {
  private final QuizService quizService;
  private final QuizMapper quizMapper = Mappers.getMapper(QuizMapper.class);
  private final RevisionMapper revisionMapper = Mappers.getMapper(RevisionMapper.class);

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
}
