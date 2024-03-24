package no.ntnu.fullstack.backend.category.component;

import lombok.RequiredArgsConstructor;
import no.ntnu.fullstack.backend.category.CategoryService;
import no.ntnu.fullstack.backend.category.model.Category;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class Startup {

   private final CategoryService categoryService;


   @EventListener
   public void onApplicationEvent(ContextRefreshedEvent event) {

       if (categoryService.getAllCategories().isEmpty()) {
           createCategories();
       }

   }

    private void createCategories() {

       List<Category> categories = List.of(
               Category.builder().category("Math").build(),
                Category.builder().category("Science").build(),
                Category.builder().category("History").build(),
                Category.builder().category("Geography").build(),
                Category.builder().category("Literature").build(),
                Category.builder().category("Art").build(),
                Category.builder().category("Music").build(),
                Category.builder().category("Sports").build()
       );

         categories.forEach(categoryService::saveCategory);

   }
}