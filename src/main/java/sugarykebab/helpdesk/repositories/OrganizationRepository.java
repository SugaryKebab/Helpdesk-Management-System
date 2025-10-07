package sugarykebab.helpdesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sugarykebab.helpdesk.entities.Organization;

import java.util.List;

public interface OrganizationRepository extends JpaRepository<Organization, String> {
    List<Organization> findByNameContainingIgnoreCase(String namePart);

    boolean existsByNameIgnoreCase(String name);
}