package no.ntnu.fullstack.backend.question.dto;

import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class QuestionCreateDTO {
  private String question;
  private UUID quizId;
  private String answer;
  private List<String> options;
}
