package org.ilia.licensingservice.controller;

import lombok.RequiredArgsConstructor;
import org.ilia.licensingservice.entity.License;
import org.ilia.licensingservice.service.LicenseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("v1/organization/{organizationId}/license")
@RequiredArgsConstructor
public class LicenseController {

    private final LicenseService licenseService;

    @GetMapping
    public ResponseEntity<List<License>> getLicensesByOrganizationId(@PathVariable("organizationId") String organizationId) {
        return ResponseEntity.ok(licenseService.getLicenseByOrganizationId(organizationId));
    }

    @GetMapping("/{licenseId}")
    public ResponseEntity<License> getLicense(@PathVariable("organizationId") String organizationId,
                                              @PathVariable("licenseId") String licenseId) {
        License license = licenseService.getLicense(licenseId, organizationId);

        if (license != null) {
            license.add(
                    linkTo(methodOn(LicenseController.class)
                            .getLicense(organizationId, licenseId))
                            .withSelfRel(),
                    linkTo(methodOn(LicenseController.class)
                            .createLicense(license, organizationId))
                            .withRel("createLicense"),
                    linkTo(methodOn(LicenseController.class)
                            .updateLicense(license, organizationId, licenseId))
                            .withRel("updateLicense"),
                    linkTo(methodOn(LicenseController.class)
                            .deleteLicense(licenseId, organizationId))
                            .withRel("deleteLicense")
            );
            return ResponseEntity.ok(license);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<License> createLicense(@RequestBody License license,
                                                 @PathVariable("organizationId") String organizationId) {
        return ResponseEntity.ok(licenseService.createLicense(license));
    }

    @PutMapping("/{licenseId}")
    public ResponseEntity<License> updateLicense(@RequestBody License license,
                                                 @PathVariable("organizationId") String organizationId,
                                                 @PathVariable("licenseId") String licenseId) {
        return ResponseEntity.ok(licenseService.updateLicense(license));
    }

    @DeleteMapping("/{licenseId}")
    public ResponseEntity<?> deleteLicense(@PathVariable("licenseId") String licenseId,
                                           @PathVariable("organizationId") String organizationId) {
        licenseService.deleteLicense(licenseId);
        return ResponseEntity.ok().build();
    }
}
