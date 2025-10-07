package sugarykebab.helpdesk.services;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sugarykebab.helpdesk.entities.Organization;
import sugarykebab.helpdesk.repositories.OrganizationRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganizationService {
    private final OrganizationRepository orgRepo;

    public List<Organization> getAll() {
        return orgRepo.findAll();
    }

    public Organization getById(String id) {
        return orgRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Organization not found"));
    }

    public List<Organization> searchByName(String keyword) {
        return orgRepo.findByNameContainingIgnoreCase(keyword);
    }

    public Organization create(Organization org) {

        return orgRepo.save(org);
    }

    public Organization update(String id, Organization updatedOrg) {
        Organization existing = getById(id);
        existing.setName(updatedOrg.getName());
        return orgRepo.save(existing);
    }

    public void delete(String id) {
        orgRepo.deleteById(id);
    }

    public boolean existsByName(String name) {
        return orgRepo.existsByNameIgnoreCase(name);
    }
}
