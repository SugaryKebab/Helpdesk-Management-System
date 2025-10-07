package sugarykebab.helpdesk.mappers;

import org.springframework.stereotype.Component;
import sugarykebab.helpdesk.dto.TeamMemberDto;
import sugarykebab.helpdesk.entities.TeamMember;

@Component
public class TeamMemberMapper {

    public TeamMemberDto toDto(TeamMember member) {
        TeamMemberDto dto = new TeamMemberDto();
        dto.setTeamId(member.getTeam().getId());
        dto.setUserId(member.getUser().getId());
        return dto;
    }

}
