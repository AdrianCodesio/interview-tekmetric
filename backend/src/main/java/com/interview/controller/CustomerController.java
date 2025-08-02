package com.interview.controller;

import com.interview.dto.CustomerRequest;
import com.interview.dto.CustomerResponse;
import com.interview.service.CustomerService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing customer operations.
 *
 * <p>This controller provides endpoints for complete CRUD operations on customers,
 * including their associated profile information. All endpoints follow RESTful
 * conventions and return appropriate HTTP status codes.
 *
 * <p>Supported operations:
 * <ul>
 *   <li>POST /v1/customers - Create a new customer with optional profile</li>
 *   <li>GET /v1/customers/{id} - Retrieve a customer by ID</li>
 *   <li>GET /v1/customers - Retrieve all customers</li>
 *   <li>GET /api/v1/customers/paginated?page=0&size=10&sort=firstName,asc - Retrieve customers with pagination</li>
 *   <li>PUT /v1/customers/{id} - Update customer and profile information</li>
 *   <li>DELETE /v1/customers/{id} - Delete customer and associated profile</li>
 * </ul>
 *
 * <p>All endpoints include validation and proper error handling through the
 * global exception handler.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    /**
     * Create a new customer with profile.
     */
    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody CustomerRequest request) {
        log.info("Creating customer with email: {}", request.email());

        CustomerResponse response = customerService.createCustomer(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Get customer by ID (includes profile data).
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable Long id) {
        log.info("Fetching customer with ID: {}", id);

        CustomerResponse response = customerService.getCustomerById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all customers (includes profile data).
     */
    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        log.info("Fetching all customers");

        List<CustomerResponse> response = customerService.getAllCustomers();
        return ResponseEntity.ok(response);
    }

    /**
     * Get customers with pagination (includes profile data).
     */
    @GetMapping("/paginated")
    public ResponseEntity<Page<CustomerResponse>> getCustomersWithPagination(Pageable pageable) {
        log.info("Fetching customers with pagination: {}", pageable);

        Page<CustomerResponse> response = customerService.getCustomersWithPagination(pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Update customer and profile.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(@PathVariable Long id,
        @Valid @RequestBody CustomerRequest request) {
        log.info("Updating customer with ID: {}", id);

        CustomerResponse response = customerService.updateCustomer(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete customer (profile deleted automatically).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        log.info("Deleting customer with ID: {}", id);

        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}