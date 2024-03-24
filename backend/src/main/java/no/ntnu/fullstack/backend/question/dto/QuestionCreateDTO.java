package no.ntnu.fullstack.backend.question.dto;

import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class QuestionCreateDTO {
  private String question;
  private UUID quizId;
  private String answer;
  private List<String> options;
}
