package org.ilia.licensingservice.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class License {

    int id;
    String licenseId;
    String description;
    String organizationId;
    String productName;
    String licenseType;
}
