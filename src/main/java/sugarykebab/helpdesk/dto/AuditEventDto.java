package sugarykebab.helpdesk.dto;

import lombok.Data;

import java.time.Instant;
import java.util.Map;

@Data
public class AuditEventDto {
    private String id;
    private String action;
    private Map<String, Object> data;
    private String actorUserId;
    private String orgId;
    private Instant createdAt;
}
