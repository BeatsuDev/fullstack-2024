package no.ntnu.fullstack.backend.quiz.dto;

import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
  private UUID creator;
  private UUID collaborator;

  public QuizFilters(
      Integer page,
      Integer pageSize,
      String textSearch,
      Integer minDifficulty,
      Integer maxDifficulty,
      List<UUID> category,
      UUID creator,
      UUID collaborator) {
    if (page != null) this.page = page;
    if (pageSize != null) this.pageSize = pageSize;
    this.textSearch = textSearch;
    this.minDifficulty = minDifficulty;
    this.maxDifficulty = maxDifficulty;
    this.category = category;
    this.creator = creator;
    this.collaborator = collaborator;
  }

  public Pageable toPageable() {
    return PageRequest.of(page, pageSize);
  }
}
