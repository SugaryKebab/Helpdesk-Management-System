package sugarykebab.helpdesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sugarykebab.helpdesk.entities.TicketComment;
import java.util.List;

public interface TicketCommentRepository extends JpaRepository<TicketComment, String> {
    List<TicketComment> findByTicket_IdAndOrg_Id(String ticketId, String orgId);
}
