package org.ilia.licensingservice.repository;

import org.ilia.licensingservice.entity.License;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LicenseRepository extends JpaRepository<License, Integer> {

    Optional<License> findByLicenseId(String licenseId);

    Optional<License> findByOrganizationIdAndLicenseId(String organizationId, String licenseId);
}
