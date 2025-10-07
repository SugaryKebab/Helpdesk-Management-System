package sugarykebab.helpdesk.mappers;

import org.springframework.stereotype.Component;
import sugarykebab.helpdesk.dto.AuditEventDto;
import sugarykebab.helpdesk.entities.AuditEvent;

@Component
public class AuditEventMapper {

    public AuditEventDto toDto(AuditEvent entity) {
        if (entity == null) return null;

        AuditEventDto dto = new AuditEventDto();
        dto.setId(entity.getId());
        dto.setAction(entity.getAction());
        dto.setData(entity.getData());
        dto.setActorUserId(entity.getActorUser() != null ? entity.getActorUser().getId() : null);
        dto.setOrgId(entity.getOrg().getId());
        dto.setCreatedAt(entity.getCreatedAt());

        return dto;
    }

}
