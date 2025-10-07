package sugarykebab.helpdesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sugarykebab.helpdesk.entities.AuditEvent;

@Repository
public interface AuditEventRepository extends JpaRepository<AuditEvent, String> {
}
