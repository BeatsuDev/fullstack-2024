package no.ntnu.fullstack.backend.quiz.repository;

import lombok.RequiredArgsConstructor;
import no.ntnu.fullstack.backend.quiz.dto.QuizDTO;
import no.ntnu.fullstack.backend.quiz.model.Quiz;
import no.ntnu.fullstack.backend.quiz.model.Revision;
import no.ntnu.fullstack.backend.user.UserMapper;
import no.ntnu.fullstack.backend.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = UserMapper.class)
@RequiredArgsConstructor
public abstract class QuizMapper {
  @Mapping(source = "revision.title", target = "title")
  @Mapping(source = "revision.description", target = "description")
  @Mapping(source = "revision.difficulty", target = "difficulty")
  @Mapping(source = "quiz.createdAt", target = "createdAt")
  @Mapping(source = "quiz.creator", target = "creator")
  public abstract QuizDTO toDTO(Quiz quiz, Revision revision);

  @Mapping(source = "user", target = "creator")
  public abstract Quiz fromCreateQuiz(User user);
}