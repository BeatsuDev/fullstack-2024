package no.ntnu.fullstack.backend.category;

import no.ntnu.fullstack.backend.category.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper
public abstract class CategoryMapper {

    @Mapping(source = "categoryId", target = "id")
    public abstract Category toCategory(UUID categoryId);
}
