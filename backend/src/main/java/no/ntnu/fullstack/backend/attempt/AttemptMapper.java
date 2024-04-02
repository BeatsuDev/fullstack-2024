package no.ntnu.fullstack.backend.attempt;

import java.util.List;
import lombok.RequiredArgsConstructor;
import no.ntnu.fullstack.backend.attempt.dto.AnswerDTO;
import no.ntnu.fullstack.backend.attempt.dto.QuestionAttemptDTO;
import no.ntnu.fullstack.backend.attempt.dto.QuizAttemptDTO;
import no.ntnu.fullstack.backend.attempt.model.QuestionAttempt;
import no.ntnu.fullstack.backend.attempt.model.QuizAttempt;
import no.ntnu.fullstack.backend.question.QuestionMapper;
import no.ntnu.fullstack.backend.quiz.mapper.QuizMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(uses = {QuizMapper.class, QuestionMapper.class})
@RequiredArgsConstructor
public abstract class AttemptMapper {

  public abstract QuestionAttempt toQuestionAttempt(AnswerDTO dto);

  public abstract QuestionAttemptDTO toQuestionAttemptDTO(QuestionAttempt question);

  public List<QuestionAttemptDTO> toQuestionAttemptDTOs(List<QuestionAttempt> questions) {
    if (questions == null) return List.of();
    return questions.stream().map(this::toQuestionAttemptDTO).toList();
  }

  @Mappings({
    @Mapping(target = "id", source = "id"),
    @Mapping(target = "quiz", source = "revision"),
    @Mapping(target = "attemptedAt", source = "createdAt"),
  })
  public abstract QuizAttemptDTO toQuizAttemptDTO(QuizAttempt attempt);

  public List<QuizAttemptDTO> toQuizAttemptDTOs(List<QuizAttempt> attempts) {
    if (attempts == null) return List.of();
    return attempts.stream().map(this::toQuizAttemptDTO).toList();
  }
}
