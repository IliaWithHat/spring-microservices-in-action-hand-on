package org.ilia.licensingservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ilia.licensingservice.entity.License;
import org.ilia.licensingservice.service.LicenseService;
import org.ilia.licensingservice.utils.UserContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("v1/organization/{organizationId}/license")
@RequiredArgsConstructor
@Slf4j
public class LicenseController {

    private final LicenseService licenseService;

    @GetMapping("/{licenseId}")
    public ResponseEntity<License> getLicense(@PathVariable("organizationId") String organizationId,
                                              @PathVariable("licenseId") String licenseId) {
        log.debug("LicenseServiceController Correlation id: {}", UserContextHolder.getContext().getCorrelationId());

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
