package org.ilia.licensingservice.service.client;

import org.ilia.licensingservice.entity.Organization;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("organization-service")
public interface OrganizationClient {

    @GetMapping("/v1/organization/{organizationId}")
    Organization getOrganization(@PathVariable String organizationId);
}
