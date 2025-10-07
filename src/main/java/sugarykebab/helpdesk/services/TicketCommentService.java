package sugarykebab.helpdesk.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sugarykebab.helpdesk.dto.TicketCommentDto;
import sugarykebab.helpdesk.entities.*;
import sugarykebab.helpdesk.mappers.TicketCommentMapper;
import sugarykebab.helpdesk.repositories.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketCommentService {

    private final TicketCommentRepository commentRepository;
    private final TicketRepository ticketRepository;


    public TicketComment createComment(TicketCommentDto dto, AppUser currentUser) {
        Ticket ticket = ticketRepository.findById(dto.getTicketId())
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        if (!ticket.getOrg().getId().equals(currentUser.getOrg().getId())) {
            throw new SecurityException("You cannot comment on a ticket outside your organization");
        }

        TicketComment comment = new TicketComment();
        comment.setId(UUID.randomUUID().toString());
        comment.setOrg(currentUser.getOrg());
        comment.setTicket(ticket);
        comment.setAuthor(currentUser);
        comment.setBody(dto.getBody());
        comment.setIsInternal(dto.getIsInternal() != null ? dto.getIsInternal() : false);
        comment.setCreatedAt(Instant.now());

        return commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public List<TicketComment> getCommentsByTicket(String ticketId, AppUser currentUser) {
        return commentRepository.findByTicket_IdAndOrg_Id(ticketId, currentUser.getOrg().getId());
    }


    public void deleteComment(String id, AppUser currentUser) {
        TicketComment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        if (!comment.getOrg().getId().equals(currentUser.getOrg().getId())) {
            throw new SecurityException("You cannot delete comments from another organization");
        }

        commentRepository.delete(comment);
    }
}
