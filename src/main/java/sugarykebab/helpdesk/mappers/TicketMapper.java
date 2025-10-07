package sugarykebab.helpdesk.mappers;

import org.springframework.stereotype.Component;
import sugarykebab.helpdesk.dto.TicketDto;
import sugarykebab.helpdesk.entities.*;

@Component
public class TicketMapper {

    public TicketDto toDTO(Ticket ticket) {
        if (ticket == null) return null;

        TicketDto dto = new TicketDto();
        dto.setId(ticket.getId());

        dto.setOrgId(ticket.getOrg().getId());
        dto.setRequesterId(ticket.getRequester().getId());
        dto.setRequesterName(ticket.getRequester().getFirstName() + " " + ticket.getRequester().getLastName());

        dto.setSubject(ticket.getSubject());
        dto.setBody(ticket.getBody());

        if (ticket.getStatus() != null) {
            dto.setStatusId(ticket.getStatus().getId());
            dto.setStatusCode(ticket.getStatus().getCode());
        }
        if (ticket.getPriority() != null) {
            dto.setPriorityId(ticket.getPriority().getId());
            dto.setPriorityCode(ticket.getPriority().getCode());
        }
        if (ticket.getCategory() != null) {
            dto.setCategoryId(ticket.getCategory().getId());
            dto.setCategoryName(ticket.getCategory().getName());
        }
        if (ticket.getAssignedUser() != null) {
            dto.setAssignedUserId(ticket.getAssignedUser().getId());
            dto.setAssignedUserName(ticket.getAssignedUser().getFirstName() + " " +
                    ticket.getAssignedUser().getLastName());
        }
        if (ticket.getAssignedTeam() != null) {
            dto.setAssignedTeamId(ticket.getAssignedTeam().getId());
            dto.setAssignedTeamName(ticket.getAssignedTeam().getName());
        }

        dto.setIsInternal(ticket.getIsInternal());
        dto.setCreatedAt(ticket.getCreatedAt());
        dto.setUpdatedAt(ticket.getUpdatedAt());
        dto.setClosedAt(ticket.getClosedAt());

        return dto;
    }

    public Ticket toEntity(TicketDto dto) {
        if (dto == null) return null;
        Ticket ticket = new Ticket();

        ticket.setSubject(dto.getSubject());
        ticket.setBody(dto.getBody());
        ticket.setIsInternal(dto.getIsInternal() != null ? dto.getIsInternal() : false);

        return ticket;
    }

    public void updateEntityFromDTO(TicketDto dto, Ticket ticket) {
        if (dto.getSubject() != null) ticket.setSubject(dto.getSubject());
        if (dto.getBody() != null) ticket.setBody(dto.getBody());
        if (dto.getIsInternal() != null) ticket.setIsInternal(dto.getIsInternal());
    }

    // Helper methods for service layer
    public AppUser findUser(String userId) {
        AppUser user = new AppUser();
        user.setId(userId);
        return user;
    }

    public Team findTeam(String teamId) {
        Team team = new Team();
        team.setId(teamId);
        return team;
    }
}
