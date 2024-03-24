package no.ntnu.fullstack.backend.category;

import java.util.List;
import lombok.RequiredArgsConstructor;
import no.ntnu.fullstack.backend.category.model.Category;
import no.ntnu.fullstack.backend.category.repository.CategoryRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryService {

  private final CategoryRepository categoryRepository;

  public List<Category> getAllCategories() {

    return categoryRepository.findAll();
  }
}
