package org.ilia.licensingservice.controller;

import lombok.RequiredArgsConstructor;
import org.ilia.licensingservice.entity.License;
import org.ilia.licensingservice.service.LicenseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("v1/organization/{organizationId}/license")
@RequiredArgsConstructor
public class LicenseController {

    private final LicenseService licenseService;

    @GetMapping("/{licenseId}")
    public ResponseEntity<License> getLicense(@PathVariable("organizationId") String organizationId,
                                              @PathVariable("licenseId") String licenseId) {
        License license = licenseService.getLicense(licenseId, organizationId);

        if (license != null) {
            license.add(
                    linkTo(methodOn(LicenseController.class)
                            .getLicense(organizationId, license.getLicenseId()))
                            .withSelfRel(),
                    linkTo(methodOn(LicenseController.class)
                            .createLicense(license))
                            .withRel("createLicense"),
                    linkTo(methodOn(LicenseController.class)
                            .updateLicense(license))
                            .withRel("updateLicense"),
                    linkTo(methodOn(LicenseController.class)
                            .deleteLicense(license.getLicenseId()))
                            .withRel("deleteLicense")
            );
            return ResponseEntity.ok(license);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<License> createLicense(@RequestBody License license) {
        return ResponseEntity.ok(licenseService.createLicense(license));
    }

    @PutMapping("/{licenseId}")
    public ResponseEntity<License> updateLicense(@RequestBody License license) {
        return ResponseEntity.ok(licenseService.updateLicense(license));
    }

    @DeleteMapping("/{licenseId}")
    public ResponseEntity<?> deleteLicense(@PathVariable("licenseId") String licenseId) {
        licenseService.deleteLicense(licenseId);
        return ResponseEntity.ok().build();
    }
}
