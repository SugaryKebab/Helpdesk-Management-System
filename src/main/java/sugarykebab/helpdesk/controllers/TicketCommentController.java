package sugarykebab.helpdesk.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sugarykebab.helpdesk.dto.TicketCommentDto;
import sugarykebab.helpdesk.entities.AppUserDetail;
import sugarykebab.helpdesk.entities.TicketComment;
import sugarykebab.helpdesk.mappers.TicketCommentMapper;
import sugarykebab.helpdesk.services.TicketCommentService;
import sugarykebab.helpdesk.utils.ResponseHelper;


@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class TicketCommentController {

    private final TicketCommentService commentService;
    private final TicketCommentMapper mapper;

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('AGENT')")
    public ResponseEntity<?> createComment(
            @RequestBody TicketCommentDto dto,
            @AuthenticationPrincipal AppUserDetail currentUser) {
        try {
            TicketComment comment = commentService.createComment(dto, currentUser.getAppUser());
            var commentDto = mapper.toDto(comment);
            return ResponseHelper.respondCreated(commentDto, "Failed to add comment");
        } catch (RuntimeException e) {
            return ResponseHelper.respondError(e.getMessage());
        }
    }


    @GetMapping("/{ticketId}/comments")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('AGENT')")
    public ResponseEntity<?> getComments(@PathVariable String ticketId,
                                         @AuthenticationPrincipal AppUserDetail currentUser) {
        try {
            var comments = commentService.getCommentsByTicket(ticketId, currentUser.getAppUser());
            var commentDtos = comments.stream()
                    .map(mapper::toDto)
                    .toList();
            return ResponseHelper.respondList(commentDtos);
        } catch (RuntimeException e) {
            return ResponseHelper.respondError(e.getMessage());
        }
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteComment(
            @PathVariable String id,
            @AuthenticationPrincipal AppUserDetail currentUser) {
        try {
            commentService.deleteComment(id, currentUser.getAppUser());
            return ResponseHelper.respondDeleted(true, "Comment not found");
        } catch (RuntimeException e) {
            return ResponseHelper.respondError(e.getMessage());
        }
    }

}
