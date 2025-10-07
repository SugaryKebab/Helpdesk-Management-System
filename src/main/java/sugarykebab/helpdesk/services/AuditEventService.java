package sugarykebab.helpdesk.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sugarykebab.helpdesk.entities.AuditEvent;
import sugarykebab.helpdesk.entities.AppUser;
import sugarykebab.helpdesk.entities.Organization;
import sugarykebab.helpdesk.mappers.AuditEventMapper;
import sugarykebab.helpdesk.repositories.AuditEventRepository;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuditEventService {

    private final AuditEventRepository auditEventRepository;
    private final AuditEventMapper mapper;

    /**
     * Logs an audit event.
     *
     * @param action     HTTP method or action name
     * @param data       any relevant payload as map
     * @param org        the organization of the current user
     * @param actorUser  the currently logged-in user
     * @return saved AuditEvent entity
     */
    public AuditEvent logEvent(String action, Map<String, Object> data, Organization org, AppUser actorUser) {
        AuditEvent event = new AuditEvent();
        event.setId(UUID.randomUUID().toString());
        event.setAction(action);
        event.setData(data);
        event.setOrg(org);
        event.setActorUser(actorUser);
        event.setCreatedAt(Instant.now());

        return auditEventRepository.save(event);
    }
}
