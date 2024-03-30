package no.ntnu.fullstack.backend.revision;

import java.util.List;
import no.ntnu.fullstack.backend.category.CategoryMapper;
import no.ntnu.fullstack.backend.quiz.dto.QuizCreateDTO;
import no.ntnu.fullstack.backend.quiz.mapper.QuizMapper;
import no.ntnu.fullstack.backend.quiz.model.Quiz;
import no.ntnu.fullstack.backend.revision.dto.RevisionDTO;
import no.ntnu.fullstack.backend.revision.model.Revision;
import no.ntnu.fullstack.backend.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.util.Pair;

@Mapper(uses = {CategoryMapper.class, QuizMapper.class})
public abstract class RevisionMapper {
  private final QuizMapper quizMapper = Mappers.getMapper(QuizMapper.class);

  @Mapping(source = "user", target = "creator")
  @Mapping(source = "quizCreate.title", target = "title")
  @Mapping(source = "quizCreate.description", target = "description")
  @Mapping(source = "quizCreate.difficulty", target = "difficulty")
  @Mapping(source = "quizCreate.categories", target = "categories")
  public abstract Revision fromQuizCreate(QuizCreateDTO quizCreate, User user);

  public RevisionDTO toRevisionDTO(Quiz quiz, Revision revision) {
    return new RevisionDTO(revision.getId(), quizMapper.toQuizDTO(quiz, revision));
  }

  public List<RevisionDTO> toRevisionDTOs(Pair<Quiz, List<Revision>> quizListPair) {
    Quiz quiz = quizListPair.getFirst();
    List<Revision> revisions = quizListPair.getSecond();
    return revisions.stream().map(revision -> toRevisionDTO(quiz, revision)).toList();
  }
}
