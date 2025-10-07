package sugarykebab.helpdesk.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sugarykebab.helpdesk.dto.ErrorDto;
import sugarykebab.helpdesk.dto.OrganizationDto;
import sugarykebab.helpdesk.entities.Organization;
import sugarykebab.helpdesk.mappers.OrganizationMapper;
import sugarykebab.helpdesk.services.OrganizationService;
import sugarykebab.helpdesk.utils.ResponseHelper;

import java.util.List;

@RestController
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
public class OrganizationController {
    private final OrganizationService orgService;


    @GetMapping
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<?> getAll() {
        try {
            var dtos = orgService.getAll()
                    .stream()
                    .map(OrganizationMapper::toDto)
                    .toList();
            return ResponseHelper.respondList(dtos);
        } catch (RuntimeException e) {
            return ResponseHelper.respondError(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<?> getById(@PathVariable String id) {
        try {
            var dto = OrganizationMapper.toDto(orgService.getById(id));
            return ResponseHelper.respondSingle(dto, "Organization not found");
        } catch (RuntimeException e) {
            return ResponseHelper.respondError(e.getMessage());
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<?> create(@RequestBody OrganizationDto dto) {
        try {
            if (orgService.existsByName(dto.getName())) {
                return ResponseHelper.respondError("Organization with name '" + dto.getName() + "' already exists.");
            }
            Organization created = orgService.create(OrganizationMapper.toEntity(dto));
            var responseDto = OrganizationMapper.toDto(created);
            return ResponseHelper.respondCreated(responseDto, "Failed to create organization");
        } catch (RuntimeException e) {
            return ResponseHelper.respondError(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody OrganizationDto dto) {
        try {
            Organization updated = orgService.update(id, OrganizationMapper.toEntity(dto));
            var responseDto = OrganizationMapper.toDto(updated);
            return ResponseHelper.respondSingle(responseDto, "Failed to update organization");
        } catch (RuntimeException e) {
            return ResponseHelper.respondError(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<?> delete(@PathVariable String id) {
        try {
            orgService.delete(id);
            return ResponseHelper.respondDeleted(true, "Organization not found");
        } catch (RuntimeException e) {
            return ResponseHelper.respondError(e.getMessage());
        }
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<?> searchByName(@RequestParam("q") String keyword) {
        try {
            var dtos = orgService.searchByName(keyword)
                    .stream()
                    .map(OrganizationMapper::toDto)
                    .toList();
            return ResponseHelper.respondList(dtos);
        } catch (RuntimeException e) {
            return ResponseHelper.respondError(e.getMessage());
        }
    }

}