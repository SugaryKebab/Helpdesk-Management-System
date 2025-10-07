package sugarykebab.helpdesk.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sugarykebab.helpdesk.dto.TicketDto;

import sugarykebab.helpdesk.entities.AppUserDetail;
import sugarykebab.helpdesk.mappers.TicketMapper;
import sugarykebab.helpdesk.services.TicketService;
import sugarykebab.helpdesk.utils.ResponseHelper;



@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final TicketMapper ticketMapper;

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('AGENT')")
    public ResponseEntity<?> createTicket(@RequestBody TicketDto dto,
                                          @AuthenticationPrincipal AppUserDetail currentUser) {
        try {
            var createdTicket = ticketService.createTicket(dto, currentUser.getAppUser());
            var responseDto = ticketMapper.toDTO(createdTicket);
            return ResponseHelper.respondCreated(responseDto, "Failed to create ticket");
        } catch (RuntimeException e) {
            return ResponseHelper.respondError(e.getMessage());
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('AGENT')")
    public ResponseEntity<?> getAllTickets(@AuthenticationPrincipal AppUserDetail currentUser) {
        try {
            var tickets = ticketService.getAllTickets(currentUser.getAppUser());
            var ticketDtos = tickets.stream()
                    .map(ticketMapper::toDTO)
                    .toList();
            return ResponseHelper.respondList(ticketDtos);
        } catch (RuntimeException e) {
            return ResponseHelper.respondError(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('AGENT')")
    public ResponseEntity<?> getTicket(@PathVariable String id,
                                       @AuthenticationPrincipal AppUserDetail currentUser) {
        try {
            var ticket = ticketService.getTicketById(id, currentUser.getAppUser());
            var ticketDto = ticketMapper.toDTO(ticket);
            return ResponseHelper.respondSingle(ticketDto, "Ticket not found");
        } catch (RuntimeException e) {
            return ResponseHelper.respondError(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateTicket(@PathVariable String id,
                                          @RequestBody TicketDto dto,
                                          @AuthenticationPrincipal AppUserDetail currentUser) {
        try {
            var ticket = ticketService.updateTicket(id, dto, currentUser.getAppUser());
            var ticketDto = ticketMapper.toDTO(ticket);
            return ResponseHelper.respondSingle(ticketDto, "Failed to update ticket");
        } catch (RuntimeException e) {
            return ResponseHelper.respondError(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteTicket(@PathVariable String id,
                                          @AuthenticationPrincipal AppUserDetail currentUser) {
        try {
            ticketService.deleteTicket(id, currentUser.getAppUser());
            return ResponseHelper.respondDeleted(true, "Ticket not found");
        } catch (RuntimeException e) {
            return ResponseHelper.respondError(e.getMessage());
        }
    }

    @PutMapping("/{id}/assign")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> assignTicket(@PathVariable String id,
                                          @RequestBody TicketDto dto,
                                          @AuthenticationPrincipal AppUserDetail currentUser) {
        try {
            var ticket = ticketService.assignTicket(
                    id,
                    dto.getAssignedUserId(),
                    dto.getAssignedTeamId(),
                    currentUser.getAppUser()
            );
            var ticketDto = ticketMapper.toDTO(ticket);
            return ResponseHelper.respondSingle(ticketDto, "Failed to assign ticket");
        } catch (RuntimeException e) {
            return ResponseHelper.respondError(e.getMessage());
        }
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getMyTickets(@AuthenticationPrincipal AppUserDetail currentUser) {
        try {
            var tickets = ticketService.getTicketsByUser(currentUser.getAppUser().getId());
            var ticketDtos = tickets.stream()
                    .map(ticketMapper::toDTO)
                    .toList();
            return ResponseHelper.respondList(ticketDtos);
        } catch (RuntimeException e) {
            return ResponseHelper.respondError(e.getMessage());
        }
    }

}