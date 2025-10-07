package sugarykebab.helpdesk.mappers;

import org.springframework.stereotype.Component;
import sugarykebab.helpdesk.dto.CategoryDto;
import sugarykebab.helpdesk.entities.Category;
import sugarykebab.helpdesk.entities.Organization;

@Component
public class CategoryMapper {

    public CategoryDto toDto(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setOrgId(category.getOrg().getId());
        dto.setName(category.getName());
        return dto;
    }

    public Category toEntity(CategoryDto dto, Organization org) {
        Category category = new Category();
        category.setId(dto.getId());
        category.setOrg(org);
        category.setName(dto.getName());
        return category;
    }
}
