package sugarykebab.helpdesk.mappers;

import sugarykebab.helpdesk.dto.OrganizationDto;
import sugarykebab.helpdesk.entities.Organization;

public class OrganizationMapper {

    public static OrganizationDto toDto(Organization entity) {
        OrganizationDto dto = new OrganizationDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    public static Organization toEntity(OrganizationDto dto) {
        Organization org = new Organization();
        org.setId(dto.getId());
        org.setName(dto.getName());
        org.setCreatedAt(dto.getCreatedAt());
        return org;
    }
}
