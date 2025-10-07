package sugarykebab.helpdesk.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sugarykebab.helpdesk.dto.CategoryDto;
import sugarykebab.helpdesk.entities.AppUserDetail;
import sugarykebab.helpdesk.entities.Category;
import sugarykebab.helpdesk.mappers.CategoryMapper;
import sugarykebab.helpdesk.services.CategoryService;
import sugarykebab.helpdesk.utils.ResponseHelper;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper mapper;


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createCategory(@RequestBody CategoryDto dto,
                                            @AuthenticationPrincipal AppUserDetail currentUser) {
        try {
            Category category = categoryService.createCategory(dto, currentUser.getAppUser());
            CategoryDto responseDto = mapper.toDto(category);
            return ResponseHelper.respondCreated(responseDto, "Failed to create category");
        } catch (RuntimeException e) {
            return ResponseHelper.respondError(e.getMessage());
        }
    }


    @GetMapping("/org")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getCategoriesByOrg(@AuthenticationPrincipal AppUserDetail currentUser) {
        try {
            List<Category> categories = categoryService.getCategoriesByOrg(currentUser.getAppUser());
            List<CategoryDto> dtos = categories.stream()
                    .map(mapper::toDto)
                    .toList();
            return ResponseHelper.respondList(dtos);
        } catch (RuntimeException e) {
            return ResponseHelper.respondError(e.getMessage());
        }
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getCategoryById(@PathVariable String id) {
        try {
            Category category = categoryService.getCategoryByIdEntity(id); // New service method returning Category entity
            CategoryDto dto = mapper.toDto(category);
            return ResponseHelper.respondSingle(dto, "Category not found");
        } catch (RuntimeException e) {
            return ResponseHelper.respondError(e.getMessage());
        }
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateCategory(@PathVariable String id,
                                            @RequestBody CategoryDto dto,
                                            @AuthenticationPrincipal AppUserDetail currentUser) {
        try {
            Category updatedCategory = categoryService.updateCategory(id, dto, currentUser.getAppUser());
            CategoryDto responseDto = mapper.toDto(updatedCategory);
            return ResponseHelper.respondSingle(responseDto, "Failed to update category");
        } catch (RuntimeException e) {
            return ResponseHelper.respondError(e.getMessage());
        }
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteCategory(@PathVariable String id,
                                            @AuthenticationPrincipal AppUserDetail currentUser) {
        try {
            categoryService.deleteCategory(id, currentUser.getAppUser());
            return ResponseHelper.respondDeleted(true, "Category not found");
        } catch (RuntimeException e) {
            return ResponseHelper.respondError(e.getMessage());
        }
    }

}
