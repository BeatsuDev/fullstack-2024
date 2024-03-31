package no.ntnu.fullstack.backend.quiz.dto;

import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class QuizFilters {
  private int page = 0;
  private int pageSize = 20;
  private String textSearch;
  private Integer minDifficulty;
  private Integer maxDifficulty;
  private List<UUID> category;

  public QuizFilters(
      Integer page,
      Integer pageSize,
      String textSearch,
      Integer minDifficulty,
      Integer maxDifficulty,
      List<UUID> category) {
    if (page != null) this.page = page;
    if (pageSize != null) this.pageSize = pageSize;
    this.textSearch = textSearch;
    this.minDifficulty = minDifficulty;
    this.maxDifficulty = maxDifficulty;
    this.category = category;
  }
}
