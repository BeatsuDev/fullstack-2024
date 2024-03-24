package no.ntnu.fullstack.backend.quiz.mapper;

import lombok.RequiredArgsConstructor;
import no.ntnu.fullstack.backend.category.CategoryMapper;
import no.ntnu.fullstack.backend.question.QuestionMapper;
import no.ntnu.fullstack.backend.quiz.dto.QuizDTO;
import no.ntnu.fullstack.backend.quiz.model.Quiz;
import no.ntnu.fullstack.backend.quiz.model.Revision;
import no.ntnu.fullstack.backend.user.UserMapper;
import no.ntnu.fullstack.backend.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(uses = {UserMapper.class, CategoryMapper.class, QuestionMapper.class})
@RequiredArgsConstructor
public abstract class QuizMapper {
  @Mappings({
    @Mapping(source = "revision.title", target = "title"),
    @Mapping(source = "revision.description", target = "description"),
    @Mapping(source = "revision.difficulty", target = "difficulty"),
    @Mapping(source = "quiz.createdAt", target = "createdAt"),
    @Mapping(source = "quiz.creator", target = "creator"),
    @Mapping(source = "revision.categories", target = "categories"),
    @Mapping(source = "quiz.id", target = "id"),
    @Mapping(source = "revision.questions", target = "questions")
  })
  public abstract QuizDTO toDTO(Quiz quiz, Revision revision);

  @Mapping(source = "user", target = "creator")
  public abstract Quiz fromCreateQuiz(User user);
}
