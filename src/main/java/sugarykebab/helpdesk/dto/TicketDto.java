package sugarykebab.helpdesk.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class TicketDto {

    private String id;

    private String orgId;          // for internal use
    private String requesterId;    // for internal use
    private String requesterName;

    private String subject;
    private String body;

    private Byte statusId;
    private String statusCode;

    private Byte priorityId;
    private String priorityCode;

    private String categoryId;
    private String categoryName;

    private String assignedUserId;
    private String assignedUserName;

    private String assignedTeamId;
    private String assignedTeamName;

    private Boolean isInternal;

    private Instant createdAt;
    private Instant updatedAt;
    private Instant closedAt;
}
