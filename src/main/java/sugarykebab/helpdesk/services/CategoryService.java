package sugarykebab.helpdesk.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sugarykebab.helpdesk.dto.CategoryDto;
import sugarykebab.helpdesk.entities.AppUser;
import sugarykebab.helpdesk.entities.Category;
import sugarykebab.helpdesk.entities.Organization;
import sugarykebab.helpdesk.mappers.CategoryMapper;
import sugarykebab.helpdesk.repositories.CategoryRepository;
import sugarykebab.helpdesk.repositories.OrganizationRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final OrganizationRepository organizationRepository;
    private final CategoryMapper mapper;

    @Transactional
    public Category createCategory(CategoryDto dto, AppUser currentUser) {
        Organization org = currentUser.getOrg(); // use user's org
        if (org == null) {
            throw new RuntimeException("User has no organization assigned");
        }

        Category category = new Category();
        category.setId(UUID.randomUUID().toString());
        category.setName(dto.getName());
        category.setOrg(org);

        return categoryRepository.save(category);
    }

    // Get a single Category entity by ID
    @Transactional(readOnly = true)
    public Category getCategoryByIdEntity(String id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    // Get all Categories for the user's org
    @Transactional(readOnly = true)
    public List<Category> getCategoriesByOrg(AppUser currentUser) {
        String orgId = currentUser.getOrg().getId();
        return categoryRepository.findByOrgId(orgId);
    }

    // Update a Category
    @Transactional
    public Category updateCategory(String id, CategoryDto dto, AppUser currentUser) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (!category.getOrg().getId().equals(currentUser.getOrg().getId())) {
            throw new RuntimeException("Cannot update category from another organization");
        }

        category.setName(dto.getName());
        return categoryRepository.save(category);
    }

    // Delete a Category
    @Transactional
    public void deleteCategory(String id, AppUser currentUser) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (!category.getOrg().getId().equals(currentUser.getOrg().getId())) {
            throw new RuntimeException("Cannot delete category from another organization");
        }

        categoryRepository.delete(category);
    }
}
