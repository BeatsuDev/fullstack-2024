package no.ntnu.fullstack.backend.collaborator;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import no.ntnu.fullstack.backend.collaborator.dto.AddCollaboratorDTO;
import no.ntnu.fullstack.backend.collaborator.exceptions.NotCollaboratorException;
import no.ntnu.fullstack.backend.quiz.QuizService;
import no.ntnu.fullstack.backend.quiz.exception.QuizNotFoundException;
import no.ntnu.fullstack.backend.quiz.model.Quiz;
import no.ntnu.fullstack.backend.user.UserMapper;
import no.ntnu.fullstack.backend.user.UserService;
import no.ntnu.fullstack.backend.user.dto.UserDTO;
import no.ntnu.fullstack.backend.user.exception.UserNotFoundException;
import no.ntnu.fullstack.backend.user.model.User;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
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
  public ResponseEntity<UserDTO> addCollaborator(
      @PathVariable("quizId") UUID quizId, @RequestBody AddCollaboratorDTO collaborator)
      throws UserNotFoundException, QuizNotFoundException, NotCollaboratorException {
    User user = userService.getUserByEmailOrThrow(collaborator.getEmail());
    User newCollaborator = quizService.addCollaborator(quizId, user);
    return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toUserDTO(newCollaborator));
  }

  @GetMapping
  public ResponseEntity<List<UserDTO>> getCollaborators(@PathVariable("quizId") UUID quizId)
      throws QuizNotFoundException {
    Quiz quiz = quizService.getQuizById(quizId);
    return ResponseEntity.ok(userMapper.toUserDTOList(quiz.getCollaborators()));
  }

  @DeleteMapping("/{userId}")
  public ResponseEntity<?> removeCollaborator(
      @PathVariable("quizId") UUID quizId, @PathVariable("userId") UUID userId)
      throws QuizNotFoundException, NotCollaboratorException {
    quizService.removeCollaborator(quizId, userId);
    return ResponseEntity.noContent().build();
  }
}
