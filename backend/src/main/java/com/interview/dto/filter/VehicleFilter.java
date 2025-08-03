package com.interview.dto.filter;

import lombok.Builder;

/**
 * Filter object for vehicle search operations.
 *
 * <p>Encapsulates all possible filter criteria for vehicle searches.
 * Used with JPA Specifications to build dynamic queries in a type-safe manner.
 */
@Builder
public record VehicleFilter(
    Long customerId,
    String vin,
    String make,
    String model,
    Integer minYear,
    Integer maxYear,
    String customerEmail,
    String customerName
) {

    /**
     * Create an empty filter (no criteria applied).
     */
    public static VehicleFilter empty() {
        return VehicleFilter.builder().build();
    }

    /**
     * Create a filter for a specific customer.
     */
    public static VehicleFilter forCustomer(Long customerId) {
        return VehicleFilter.builder()
            .customerId(customerId)
            .build();
    }

    /**
     * Create a filter for make and model.
     */
    public static VehicleFilter forMakeAndModel(String make, String model) {
        return VehicleFilter.builder()
            .make(make)
            .model(model)
            .build();
    }
}