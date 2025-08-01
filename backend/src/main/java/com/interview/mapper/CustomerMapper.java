package com.interview.mapper;

import com.interview.dto.CustomerRequest;
import com.interview.dto.CustomerResponse;
import com.interview.entity.Customer;
import com.interview.entity.CustomerProfile;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    /**
     * Convert CustomerRequest to Customer entity
     * Profile will be handled separately in service layer
     * createdDate and updatedDate are handled by JPA auditing
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customerProfile", ignore = true)
    Customer toEntity(CustomerRequest request);

    /**
     * Convert Customer entity to CustomerResponse
     * Flattens customer and profile data into single response
     */
    @Mapping(target = "address", source = "customerProfile.address")
    @Mapping(target = "dateOfBirth", source = "customerProfile.dateOfBirth")
    @Mapping(target = "preferredContactMethod", source = "customerProfile.preferredContactMethod")
    CustomerResponse toResponse(Customer customer);

    /**
     * Convert list of Customer entities to list of CustomerResponse
     */
    List<CustomerResponse> toResponseList(List<Customer> customers);

    /**
     * Update existing Customer entity with CustomerRequest
     * Profile updates will be handled separately in service layer
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "customerProfile", ignore = true)
    void updateEntity(@MappingTarget Customer existingCustomer, CustomerRequest request);

    /**
     * Create CustomerProfile entity from CustomerRequest profile fields
     * createdDate and updatedDate are handled by JPA auditing
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    CustomerProfile toProfileEntity(CustomerRequest request);

    /**
     * Update existing CustomerProfile entity with CustomerRequest profile fields
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    void updateProfileEntity(@MappingTarget CustomerProfile existingProfile, CustomerRequest request);
}