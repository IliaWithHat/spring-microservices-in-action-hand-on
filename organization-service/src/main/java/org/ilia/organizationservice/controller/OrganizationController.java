package org.ilia.organizationservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ilia.organizationservice.entity.Organization;
import org.ilia.organizationservice.service.OrganizationService;
import org.ilia.organizationservice.utils.UserContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "v1/organization")
public class OrganizationController {

    private final OrganizationService service;

    @RequestMapping(value = "/{organizationId}", method = RequestMethod.GET)
    public ResponseEntity<Organization> getOrganization(@PathVariable("organizationId") String organizationId) {
        log.debug("OrganizationController Correlation id: {}", UserContextHolder.getContext().getCorrelationId());
        return ResponseEntity.ok(service.findById(organizationId));
    }

    @RequestMapping(value = "/{organizationId}", method = RequestMethod.PUT)
    public void updateOrganization(@PathVariable("organizationId") String id, @RequestBody Organization organization) {
        service.update(organization);
    }

    @PostMapping
    public ResponseEntity<Organization> saveOrganization(@RequestBody Organization organization) {
        return ResponseEntity.ok(service.create(organization));
    }

    @RequestMapping(value = "/{organizationId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrganization(@PathVariable("organizationId") String organizationId, @RequestBody Organization organization) {
        service.delete(organization);
    }

}
