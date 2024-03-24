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

                Category.builder().name("Science").color("#00FF00").build(),
                Category.builder().name("History").color("#0000FF").build(),
                Category.builder().name("Geography").color("#FFFF00").build(),
                Category.builder().name("Literature").color("#FF00FF").build(),
                Category.builder().name("Art").color("#00FFFF").build(),
                Category.builder().name("Music").color("#000000").build(),
                Category.builder().name("Sports").color("#FFFFFF").build()
       );

         categories.forEach(categoryService::saveCategory);

   }
}