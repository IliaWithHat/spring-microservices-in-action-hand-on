package org.ilia.licensingservice.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.ilia.licensingservice.entity.License;
import org.ilia.licensingservice.entity.Organization;
import org.ilia.licensingservice.repository.LicenseRepository;
import org.ilia.licensingservice.service.client.OrganizationClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

@Service
@Transactional
@RequiredArgsConstructor
public class LicenseService {

    private final LicenseRepository licenseRepository;
    private final OrganizationClient organizationClient;

    public License getLicense(String licenseId, String organizationId) {
        License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
        if (null == license) {
            return null;
        }

        Organization organization = organizationClient.getOrganization(organizationId);
        if (null != organization) {
            license.setOrganizationName(organization.getName());
            license.setContactName(organization.getContactName());
            license.setContactEmail(organization.getContactEmail());
            license.setContactPhone(organization.getContactPhone());
        }

        return license;
    }

    @CircuitBreaker(name = "licenseService")
    public List<License> getLicenseByOrganizationId(String organizationId) {
        randomlyRunLong();
        return licenseRepository.findByOrganizationId(organizationId);
    }

    @SneakyThrows
    private void randomlyRunLong() {
        Random rand = new Random();
        int randomNum = rand.nextInt((3 - 1) + 1) + 1;
        if (randomNum == 3) {
            System.out.println("Sleep");
            Thread.sleep(5000);
            throw new TimeoutException();
        }
    }

    public License createLicense(License license) {
        license.setLicenseId(UUID.randomUUID().toString());
        return licenseRepository.save(license);
    }

    public License updateLicense(License license) {
        return licenseRepository.save(license);
    }

    public void deleteLicense(String licenseId) {
        License license = licenseRepository.findByLicenseId(licenseId);
        if (license != null) {
            licenseRepository.delete(license);
        } else {
            throw new RuntimeException("License not found");
        }
    }
}
