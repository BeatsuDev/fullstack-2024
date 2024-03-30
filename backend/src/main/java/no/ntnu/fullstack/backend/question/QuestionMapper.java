package no.ntnu.fullstack.backend.question;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import no.ntnu.fullstack.backend.question.dto.QuestionCreateDTO;
import no.ntnu.fullstack.backend.question.dto.QuestionDTO;
import no.ntnu.fullstack.backend.question.dto.QuestionWithAnswerDTO;
import no.ntnu.fullstack.backend.question.model.Question;
import no.ntnu.fullstack.backend.question.model.QuestionOption;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
@RequiredArgsConstructor
public abstract class QuestionMapper {
  public String fromOption(QuestionOption questionOption) {
    return questionOption.getOption();
  }

  public QuestionOption toOption(String question) {
    return new QuestionOption(question);
  }

  public List<QuestionOption> fromOptions(List<String> options) {
    if (options == null) return new ArrayList<>();
    return options.stream().map(this::toOption).toList();
  }

  @Mappings({@Mapping(source = "questionId", target = "id")})
  public abstract QuestionDTO toDTO(Question question);

  @Mappings({@Mapping(source = "questionId", target = "id")})
  public abstract QuestionWithAnswerDTO toDTOWithAnswer(Question question);

  public abstract Question fromDTO(QuestionCreateDTO questionCreateDTO);
}
