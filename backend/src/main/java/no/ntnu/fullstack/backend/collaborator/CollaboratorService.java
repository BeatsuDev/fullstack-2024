package no.ntnu.fullstack.backend.collaborator;

import lombok.RequiredArgsConstructor;
import no.ntnu.fullstack.backend.collaborator.exceptions.NotCollaboratorException;
import no.ntnu.fullstack.backend.collaborator.exceptions.NotCreatorException;
import no.ntnu.fullstack.backend.quiz.model.Quiz;
import no.ntnu.fullstack.backend.user.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CollaboratorService {
  public boolean loggedInUserIsCollaborator(Quiz quiz) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null) {
      return false;
    }

    User loggedInUser = (User) auth.getPrincipal();
    if (loggedInUser.getIsAnonymous()) return false;
    if (quiz.getCreator().getId().equals(loggedInUser.getId())) return true;
    return quiz.getCollaborators().stream()
        .map(User::getId)
        .anyMatch(id -> id.equals(loggedInUser.getId()));
  }

  public void loggedInUserIsCreatorOrThrow(Quiz quiz) throws NotCreatorException {
    if (!loggedInUserIsCreator(quiz)) throw new NotCreatorException();
  }

  public boolean loggedInUserIsCreator(Quiz quiz) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null) return false;

    User loggedInUser = (User) auth.getPrincipal();
    if (loggedInUser.getIsAnonymous()) return false;
    return quiz.getCreator().getId().equals(loggedInUser.getId());
  }

  public void loggedInUserIsCollaboratorOrThrow(Quiz quiz) throws NotCollaboratorException {
    if (!loggedInUserIsCollaborator(quiz)) throw new NotCollaboratorException();
  }
}
