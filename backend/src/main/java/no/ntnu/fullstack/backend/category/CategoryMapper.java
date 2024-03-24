package no.ntnu.fullstack.backend.category;

import no.ntnu.fullstack.backend.category.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mapper
public abstract class CategoryMapper {

    @Mapping(source = "categoryId", target = "id")
    public abstract Category toCategory(UUID categoryId);

    public List<Category> toCategories(List<UUID> categoryIds) {
        if (categoryIds == null) return new ArrayList<>();
        return categoryIds.stream().map(this::toCategory).toList();
    }
}
