package sugarykebab.helpdesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sugarykebab.helpdesk.entities.TicketStatus;

import java.util.Optional;

public interface TicketStatusRepository extends JpaRepository<TicketStatus, Byte> {

}