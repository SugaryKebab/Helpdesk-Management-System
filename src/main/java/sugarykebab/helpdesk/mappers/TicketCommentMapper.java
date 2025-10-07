package sugarykebab.helpdesk.mappers;

import org.springframework.stereotype.Component;
import sugarykebab.helpdesk.dto.TicketCommentDto;
import sugarykebab.helpdesk.entities.TicketComment;

@Component
public class TicketCommentMapper {

    public TicketCommentDto toDto(TicketComment comment) {
        TicketCommentDto dto = new TicketCommentDto();
        dto.setId(comment.getId());
        dto.setTicketId(comment.getTicket().getId());
        dto.setAuthorId(comment.getAuthor().getId());
        dto.setAuthorName(comment.getAuthor().getFullName());
        dto.setBody(comment.getBody());
        dto.setIsInternal(comment.getIsInternal());
        dto.setCreatedAt(comment.getCreatedAt());
        return dto;
    }
}
