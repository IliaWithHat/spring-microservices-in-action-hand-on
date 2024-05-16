package org.ilia.licensingservice.repository;

import org.ilia.licensingservice.entity.License;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LicenseRepository extends JpaRepository<License, Integer> {

    License findByLicenseId(String licenseId);

    License findByOrganizationIdAndLicenseId(String organizationId, String licenseId);
}
