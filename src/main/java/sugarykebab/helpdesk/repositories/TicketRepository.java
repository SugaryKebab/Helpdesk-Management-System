package sugarykebab.helpdesk.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import sugarykebab.helpdesk.entities.Ticket;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, String> {
    List<Ticket> findAllByOrgId(String orgId);
    List<Ticket> findAllByRequesterIdOrAssignedUserId(String requesterId, String assignedUserId);
}
