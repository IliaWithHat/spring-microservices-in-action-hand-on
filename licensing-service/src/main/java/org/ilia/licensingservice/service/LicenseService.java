package org.ilia.licensingservice.service;

import lombok.RequiredArgsConstructor;
import org.ilia.licensingservice.entity.License;
import org.ilia.licensingservice.repository.LicenseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class LicenseService {

    private final LicenseRepository licenseRepository;

    public Optional<License> getLicense(String licenseId, String organizationId) {
        return licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
    }

    public License createLicense(License license) {
        license.setLicenseId(UUID.randomUUID().toString());
        return licenseRepository.save(license);
    }

    public License updateLicense(License license) {
        return licenseRepository.save(license);
    }

    public void deleteLicense(String licenseId) {
        licenseRepository.findByLicenseId(licenseId)
                .ifPresent(licenseRepository::delete);
    }
}
