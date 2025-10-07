package sugarykebab.helpdesk.mappers;

import org.springframework.stereotype.Component;
import sugarykebab.helpdesk.dto.TeamDto;
import sugarykebab.helpdesk.entities.Organization;
import sugarykebab.helpdesk.entities.Team;

@Component
public class TeamMapper {

    public Team toEntity(TeamDto dto, Organization org) {
        Team team = new Team();
        team.setId(dto.getId()); // can be null if creating new
        team.setName(dto.getName());
        team.setOrg(org);
        return team;
    }

    public TeamDto toDto(Team team) {
        TeamDto dto = new TeamDto();
        dto.setId(team.getId());
        dto.setName(team.getName());
        dto.setOrgId(team.getOrg().getId());
        return dto;
    }
}
