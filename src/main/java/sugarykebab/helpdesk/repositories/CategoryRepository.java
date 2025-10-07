package sugarykebab.helpdesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sugarykebab.helpdesk.entities.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, String> {
    List<Category> findByOrgId(String orgId);
}