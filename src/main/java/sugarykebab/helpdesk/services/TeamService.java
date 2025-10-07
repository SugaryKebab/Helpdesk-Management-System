package sugarykebab.helpdesk.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sugarykebab.helpdesk.dto.TeamDto;
import sugarykebab.helpdesk.dto.TeamMemberDto;
import sugarykebab.helpdesk.entities.*;
import sugarykebab.helpdesk.repositories.AppUserRepository;
import sugarykebab.helpdesk.repositories.TeamMemberRepository;
import sugarykebab.helpdesk.repositories.TeamRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final AppUserRepository appUserRepository;


    @Transactional
    public Team createTeam(TeamDto dto, AppUser currentUser) {

        Organization org = currentUser.getOrg();
        if (org == null) throw new RuntimeException("Current user has no organization");

        Team team = new Team();
        team.setId(UUID.randomUUID().toString());
        team.setOrg(org);
        team.setName(dto.getName());

        return teamRepository.save(team);
    }

    @Transactional
    public Team updateTeam(String teamId, TeamDto dto, AppUser currentUser) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));


        if (!team.getOrg().getId().equals(currentUser.getOrg().getId())) {
            throw new RuntimeException("Unauthorized to update this team");
        }

        if (dto.getName() != null) team.setName(dto.getName());
        return teamRepository.save(team);
    }


    @Transactional
    public void deleteTeam(String teamId, AppUser currentUser) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        if (!team.getOrg().getId().equals(currentUser.getOrg().getId())) {
            throw new RuntimeException("Unauthorized to delete this team");
        }

        teamRepository.delete(team);
    }


    @Transactional
    public TeamMember addUserToTeam(TeamMemberDto dto, AppUser currentUser) {
        Team team = teamRepository.findById(dto.getTeamId())
                .orElseThrow(() -> new RuntimeException("Team not found"));


        if (!team.getOrg().getId().equals(currentUser.getOrg().getId())) {
            throw new RuntimeException("Unauthorized to modify this team");
        }

        AppUser user = appUserRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));


        if (!team.getOrg().getId().equals(user.getOrg().getId())) {
            throw new RuntimeException("User does not belong to the same organization");
        }

        TeamMember member = new TeamMember();
        member.setTeam(team);
        member.setUser(user);
        member.setId(new TeamMemberId(team.getId(), user.getId()));

        return teamMemberRepository.save(member);
    }


    @Transactional
    public void removeUserFromTeam(String teamId, String userId, AppUser currentUser) {
        TeamMemberId id = new TeamMemberId(teamId, userId);
        TeamMember member = teamMemberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team member not found"));

        // Only allow removing members from same org
        if (!member.getTeam().getOrg().getId().equals(currentUser.getOrg().getId())) {
            throw new RuntimeException("Unauthorized to remove this member");
        }

        teamMemberRepository.delete(member);
    }

    public List<Team> getTeamsByOrg(AppUser appUser) {
        String orgId = appUser.getOrg().getId();
        return teamRepository.findByOrgId(orgId);
    }
}
