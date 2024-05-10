package org.ilia.licensingservice.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import org.springframework.hateoas.RepresentationModel;

@Data
@EqualsAndHashCode(callSuper = false)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class License extends RepresentationModel<License> {

    int id;
    String licenseId;
    String description;
    String organizationId;
    String productName;
    String licenseType;
}

