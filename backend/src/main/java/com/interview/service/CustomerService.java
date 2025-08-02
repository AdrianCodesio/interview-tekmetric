package com.interview.service;

import com.interview.dto.CustomerRequest;
import com.interview.dto.CustomerResponse;
import com.interview.entity.Customer;
import com.interview.entity.CustomerProfile;
import com.interview.exception.CustomerAlreadyExistsException;
import com.interview.exception.CustomerNotFoundException;
import com.interview.mapper.CustomerMapper;
import com.interview.repository.CustomerRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing customer operations including CRUD operations and customer profiles.
 *
 * <p>This service handles the business logic for customer management, including:
 * <ul>
 *   <li>Creating customers with optional profile data</li>
 *   <li>Retrieving customers by ID, listing all customers or paginated list of customers</li>
 *   <li>Updating customer information and profiles</li>
 *   <li>Deleting customers (cascades to profiles)</li>
 * </ul>
 *
 * <p>All read operations are performed within read-only transactions for optimal performance.
 * Write operations use full transactions with proper exception handling.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    /**
     * Create a new customer with profile.
     */
    @Transactional
    public CustomerResponse createCustomer(CustomerRequest request) {
        log.debug("Creating customer with email: {}", request.email());

        // Check if customer with email already exists
        if (customerRepository.existsByEmail(request.email())) {
            throw new CustomerAlreadyExistsException(request.email());
        }

        // Create customer entity
        Customer customer = customerMapper.toEntity(request);

        // Create profile if profile fields are provided
        if (hasProfileData(request)) {
            CustomerProfile profile = customerMapper.toProfileEntity(request);
            profile.setCustomer(customer);
            customer.setCustomerProfile(profile);
        }

        Customer savedCustomer = customerRepository.save(customer);

        log.info("Created customer with ID: {} and email: {}", savedCustomer.getId(), savedCustomer.getEmail());
        return customerMapper.toResponse(savedCustomer);
    }

    /**
     * Get customer by ID (always includes profile data).
     */
    public CustomerResponse getCustomerById(Long id) {
        log.debug("Fetching customer with ID: {}", id);

        Customer customer = customerRepository.findByIdWithProfile(id)
            .orElseThrow(() -> new CustomerNotFoundException(id));

        return customerMapper.toResponse(customer);
    }

    /**
     * Get all customers (always includes profile data).
     */
    public List<CustomerResponse> getAllCustomers() {
        log.debug("Fetching all customers");

        List<Customer> customers = customerRepository.findAll();
        return customerMapper.toResponseList(customers);
    }

    /**
     * Get customers with pagination (always includes profile data).
     */
    public Page<CustomerResponse> getCustomersWithPagination(Pageable pageable) {
        log.debug("Fetching customers with pagination: {}", pageable);

        Page<Customer> customerPage = customerRepository.findAll(pageable);
        return customerPage.map(customerMapper::toResponse);
    }

    /**
     * Update customer and profile.
     */
    @Transactional
    public CustomerResponse updateCustomer(Long id, CustomerRequest request) {
        log.debug("Updating customer with ID: {}", id);

        Customer existingCustomer = customerRepository.findByIdWithProfile(id)
            .orElseThrow(() -> new CustomerNotFoundException(id));

        // Check if email is changing and new email already exists
        if (!existingCustomer.getEmail().equals(request.email())
            && customerRepository.existsByEmail(request.email())) {
            throw new CustomerAlreadyExistsException(request.email());
        }

        // Update customer fields
        customerMapper.updateEntity(existingCustomer, request);

        // Update or create profile
        if (hasProfileData(request)) {
            if (existingCustomer.getCustomerProfile() != null) {
                // Update existing profile
                customerMapper.updateProfileEntity(existingCustomer.getCustomerProfile(), request);
            } else {
                // Create new profile
                CustomerProfile profile = customerMapper.toProfileEntity(request);
                profile.setCustomer(existingCustomer);
                existingCustomer.setCustomerProfile(profile);
            }
        }

        Customer updatedCustomer = customerRepository.save(existingCustomer);

        log.info("Updated customer with ID: {}", updatedCustomer.getId());
        return customerMapper.toResponse(updatedCustomer);
    }

    /**
     * Delete customer (profile will be deleted automatically due to cascade).
     */
    @Transactional
    public void deleteCustomer(Long id) {
        log.debug("Deleting customer with ID: {}", id);

        if (!customerRepository.existsById(id)) {
            throw new CustomerNotFoundException(id);
        }

        customerRepository.deleteById(id);
        log.info("Deleted customer with ID: {}", id);
    }

    /**
     * Check if request contains any profile data.
     */
    private boolean hasProfileData(CustomerRequest request) {
        return request.address() != null
            || request.dateOfBirth() != null
            || request.preferredContactMethod() != null;
    }
}