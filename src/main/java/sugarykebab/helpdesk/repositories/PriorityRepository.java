package sugarykebab.helpdesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sugarykebab.helpdesk.entities.Priority;

public interface PriorityRepository extends JpaRepository<Priority, Byte> {
}