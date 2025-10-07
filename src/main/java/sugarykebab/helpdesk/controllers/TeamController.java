package sugarykebab.helpdesk.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sugarykebab.helpdesk.dto.*;
import sugarykebab.helpdesk.entities.AppUserDetail;
import sugarykebab.helpdesk.entities.Team;
import sugarykebab.helpdesk.entities.TeamMember;
import sugarykebab.helpdesk.mappers.TeamMapper;
import sugarykebab.helpdesk.mappers.TeamMemberMapper;
import sugarykebab.helpdesk.services.TeamService;
import sugarykebab.helpdesk.utils.ResponseHelper;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;
    private final TeamMapper teamMapper;
    private final TeamMemberMapper teamMemberMapper;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createTeam(@RequestBody TeamDto dto,
                                        @AuthenticationPrincipal AppUserDetail currentUser) {
        try {
            Team team = teamService.createTeam(dto, currentUser.getAppUser());
            return ResponseHelper.respondCreated(teamMapper.toDto(team), "Failed to create team");
        } catch (RuntimeException e) {
            return ResponseHelper.respondError(e.getMessage());
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getTeamsByOrg(@AuthenticationPrincipal AppUserDetail currentUser) {
        try {
            List<Team> teams = teamService.getTeamsByOrg(currentUser.getAppUser());
            List<TeamDto> dtos = teams.stream().map(teamMapper::toDto).toList();
            return ResponseHelper.respondList(dtos);
        } catch (RuntimeException e) {
            return ResponseHelper.respondError(e.getMessage());
        }
    }

    @PutMapping("/{teamId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateTeam(@PathVariable String teamId,
                                        @RequestBody TeamDto dto,
                                        @AuthenticationPrincipal AppUserDetail currentUser) {
        try {
            Team team = teamService.updateTeam(teamId, dto, currentUser.getAppUser());
            return ResponseHelper.respondSingle(teamMapper.toDto(team), "Team not found");
        } catch (RuntimeException e) {
            return ResponseHelper.respondError(e.getMessage());
        }
    }

    @DeleteMapping("/{teamId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteTeam(@PathVariable String teamId,
                                        @AuthenticationPrincipal AppUserDetail currentUser) {
        try {
            teamService.deleteTeam(teamId, currentUser.getAppUser());
            return ResponseHelper.respondDeleted(true, "Team not found");
        } catch (RuntimeException e) {
            return ResponseHelper.respondError(e.getMessage());
        }
    }

    @PostMapping("/{teamId}/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addUserToTeam(@PathVariable String teamId,
                                           @RequestBody TeamMemberDto dto,
                                           @AuthenticationPrincipal AppUserDetail currentUser) {
        try {
            dto.setTeamId(teamId);
            TeamMember member = teamService.addUserToTeam(dto, currentUser.getAppUser());
            return ResponseHelper.respondCreated(teamMemberMapper.toDto(member), "Failed to add user to team");
        } catch (RuntimeException e) {
            return ResponseHelper.respondError(e.getMessage());
        }
    }

    @DeleteMapping("/{teamId}/users/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> removeUserFromTeam(@PathVariable String teamId,
                                                @PathVariable String userId,
                                                @AuthenticationPrincipal AppUserDetail currentUser) {
        try {
            teamService.removeUserFromTeam(teamId, userId, currentUser.getAppUser());
            return ResponseHelper.respondDeleted(true, "Team member not found");
        } catch (RuntimeException e) {
            return ResponseHelper.respondError(e.getMessage());
        }
    }

}
