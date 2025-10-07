package sugarykebab.helpdesk.dto;

import lombok.Data;

@Data
public class TeamDto {
    private String id; // optional for creation, generated in service
    private String orgId; // required
    private String name;
}