package no.ntnu.fullstack.backend.attempt;

import java.util.List;
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
public abstract class AttemptMapper {
  public abstract QuestionAttempt toQuestionAttempt(AnswerDTO dto);

  public abstract QuestionAttemptDTO toQuestionAttemptDTO(QuestionAttempt question);

  public abstract List<QuestionAttemptDTO> toQuestionAttemptDTOs(List<QuestionAttempt> questions);

  @Mappings({
    @Mapping(target = "id", source = "id"),
    @Mapping(target = "quiz", source = "revision")
  })
  public abstract QuizAttemptDTO toQuizAttemptDTO(QuizAttempt attempt);

  public abstract List<QuizAttemptDTO> toQuizAttemptDTOs(List<QuizAttempt> attempts);
}
