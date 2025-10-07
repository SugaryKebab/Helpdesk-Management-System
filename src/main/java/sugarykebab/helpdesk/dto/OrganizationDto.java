package sugarykebab.helpdesk.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class OrganizationDto {
    private String id;
    private String name;
    private Instant createdAt;
}