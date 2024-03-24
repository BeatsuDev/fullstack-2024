package no.ntnu.fullstack.backend.quiz.mapper;

import no.ntnu.fullstack.backend.quiz.dto.QuizCreateDTO;
import no.ntnu.fullstack.backend.quiz.model.Revision;
import no.ntnu.fullstack.backend.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public abstract class RevisionMapper {

  @Mapping(source = "user", target = "creator")
  @Mapping(source = "quizCreate.title", target = "title")
  @Mapping(source = "quizCreate.description", target = "description")
  @Mapping(source = "quizCreate.difficulty", target = "difficulty")
  @Mapping(source = "quizCreate.categories", target = "categories")
  public abstract Revision fromQuizCreate(QuizCreateDTO quizCreate, User user);
}
