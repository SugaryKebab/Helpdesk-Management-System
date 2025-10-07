package sugarykebab.helpdesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sugarykebab.helpdesk.entities.TeamMember;
import sugarykebab.helpdesk.entities.TeamMemberId;

import java.util.List;

public interface TeamMemberRepository extends JpaRepository<TeamMember, TeamMemberId> {
    List<TeamMember> findByTeamId(String teamId);

    List<TeamMember> findByUserId(String userId);
}