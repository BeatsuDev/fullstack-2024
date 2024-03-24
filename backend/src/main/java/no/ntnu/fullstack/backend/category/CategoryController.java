package no.ntnu.fullstack.backend.category;

import lombok.RequiredArgsConstructor;
import no.ntnu.fullstack.backend.category.model.Category;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/category")
public class CategoryController {

  private final CategoryService categoryService;

  @GetMapping
  public List<Category> getAllCategories() {

    return categoryService.getAllCategories();
  }
}
