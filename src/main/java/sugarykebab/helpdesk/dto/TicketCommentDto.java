package sugarykebab.helpdesk.dto;

import lombok.Data;
import java.time.Instant;

@Data
public class TicketCommentDto {
    private String id;
    private String ticketId;
    private String authorId;
    private String authorName;
    private String body;
    private Boolean isInternal;
    private Instant createdAt;
}
