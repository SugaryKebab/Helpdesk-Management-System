package sugarykebab.helpdesk.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sugarykebab.helpdesk.dto.TicketDto;
import sugarykebab.helpdesk.entities.*;
import sugarykebab.helpdesk.mappers.TicketMapper;
import sugarykebab.helpdesk.repositories.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketService {

    private final TicketRepository ticketRepo;
    private final TicketStatusRepository statusRepo;
    private final PriorityRepository priorityRepo;
    private final CategoryRepository categoryRepo;
    private final AppUserRepository appUserRepo;
    private final TeamRepository teamRepo;
    private final TicketMapper ticketMapper;

    public Ticket createTicket(TicketDto dto, AppUser requester) {
        if (requester.getOrg() == null) {
            throw new RuntimeException("Requester has no organization assigned");
        }

        Ticket ticket = ticketMapper.toEntity(dto);
        ticket.setOrg(requester.getOrg());
        ticket.setRequester(requester);
        ticket.setStatus(statusRepo.findById(dto.getStatusId())
                .orElseThrow(() -> new RuntimeException("Status not found")));
        ticket.setPriority(priorityRepo.findById(dto.getPriorityId())
                .orElseThrow(() -> new RuntimeException("Priority not found")));

        if (dto.getCategoryId() != null)
            ticket.setCategory(categoryRepo.findById(dto.getCategoryId()).orElse(null));
        if (dto.getAssignedUserId() != null)
            ticket.setAssignedUser(ticketMapper.findUser(dto.getAssignedUserId()));
        if (dto.getAssignedTeamId() != null)
            ticket.setAssignedTeam(ticketMapper.findTeam(dto.getAssignedTeamId()));

        return ticketRepo.save(ticket);
    }

    @Transactional(readOnly = true)
    public List<Ticket> getAllTickets(AppUser user) {
        return ticketRepo.findAllByOrgId(user.getOrg().getId());
    }

    @Transactional(readOnly = true)
    public Ticket getTicketById(String ticketId, AppUser user) {
        Ticket ticket = ticketRepo.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        if (!ticket.getOrg().getId().equals(user.getOrg().getId())) {
            throw new RuntimeException("Access denied: ticket not in your organization");
        }

        return ticket;
    }

    public Ticket updateTicket(String ticketId, TicketDto dto, AppUser user) {
        Ticket ticket = ticketRepo.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        if (!ticket.getOrg().getId().equals(user.getOrg().getId())) {
            throw new RuntimeException("Access denied: ticket not in your organization");
        }

        ticketMapper.updateEntityFromDTO(dto, ticket);
        return ticketRepo.save(ticket);
    }

    public void deleteTicket(String ticketId, AppUser user) {
        Ticket ticket = ticketRepo.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        if (!ticket.getOrg().getId().equals(user.getOrg().getId())) {
            throw new RuntimeException("Access denied: ticket not in your organization");
        }

        ticketRepo.delete(ticket);
    }

    public Ticket assignTicket(String ticketId, String userId, String teamId, AppUser currentUser) {
        Ticket ticket = ticketRepo.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        if (!ticket.getOrg().getId().equals(currentUser.getOrg().getId())) {
            throw new AccessDeniedException("Cannot assign ticket outside your organization");
        }

        if (userId != null) {
            AppUser user = appUserRepo.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            ticket.setAssignedUser(user);
        } else {
            ticket.setAssignedUser(null);
        }

        if (teamId != null) {
            Team team = teamRepo.findById(teamId)
                    .orElseThrow(() -> new RuntimeException("Team not found"));
            ticket.setAssignedTeam(team);
        } else {
            ticket.setAssignedTeam(null);
        }

        return ticketRepo.save(ticket);
    }

    @Transactional(readOnly = true)
    public List<Ticket> getTicketsByUser(String userId) {
        AppUser user = appUserRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));


        return ticketRepo.findAllByRequesterIdOrAssignedUserId(user.getId(), user.getId());
    }

}
