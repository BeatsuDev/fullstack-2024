package no.ntnu.fullstack.backend.colleborator;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import no.ntnu.fullstack.backend.colleborator.dto.AddCollaboratorDTO;
import no.ntnu.fullstack.backend.colleborator.exceptions.NotCollaboratorException;
import no.ntnu.fullstack.backend.quiz.QuizService;
import no.ntnu.fullstack.backend.quiz.exception.QuizNotFoundException;
import no.ntnu.fullstack.backend.quiz.model.Quiz;
import no.ntnu.fullstack.backend.user.UserMapper;
import no.ntnu.fullstack.backend.user.UserNotFoundException;
import no.ntnu.fullstack.backend.user.UserService;
import no.ntnu.fullstack.backend.user.dto.UserDTO;
import no.ntnu.fullstack.backend.user.model.User;
import org.mapstruct.factory.Mappers;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/quiz/{quizId}/collaborator")
@RequiredArgsConstructor
public class CollaboratorController {
  private final QuizService quizService;
  private final UserService userService;
  private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

  @PostMapping
  public ResponseEntity<List<UserDTO>> addCollaborator(
      @PathVariable("quizId") UUID quizId, @RequestBody AddCollaboratorDTO collaborator)
      throws UserNotFoundException, QuizNotFoundException, NotCollaboratorException {
    User user = userService.getUserByEmailOrThrow(collaborator.getEmail());
    Quiz quiz = quizService.addCollaborator(quizId, user);
    return ResponseEntity.ok(userMapper.toUserDTOList(quiz.getCollaborators()));
  }

  @GetMapping
  public ResponseEntity<List<UserDTO>> getCollaborators(@PathVariable("quizId") UUID quizId)
      throws QuizNotFoundException {
    Quiz quiz = quizService.getQuizById(quizId);
    return ResponseEntity.ok(userMapper.toUserDTOList(quiz.getCollaborators()));
  }

  @DeleteMapping("/{userId}")
  public ResponseEntity<List<UserDTO>> removeCollaborator(
      @PathVariable("quizId") UUID quizId, @PathVariable("userId") UUID userId)
      throws QuizNotFoundException, NotCollaboratorException {
    Quiz quiz = quizService.removeCollaborator(quizId, userId);
    return ResponseEntity.ok(userMapper.toUserDTOList(quiz.getCollaborators()));
  }
}
