package sugarykebab.helpdesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sugarykebab.helpdesk.entities.Team;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, String> {
    List<Team> findByOrgId(String orgId);
}