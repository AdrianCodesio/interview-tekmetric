package com.interview.controller;

import com.interview.dto.CustomerRequest;
import com.interview.dto.CustomerResponse;
import com.interview.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    /**
     * Create a new customer with profile
     * POST /api/v1/customers
     */
    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody CustomerRequest request) {
        log.info("Creating customer with email: {}", request.email());

        CustomerResponse response = customerService.createCustomer(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Get customer by ID (includes profile data)
     * GET /api/v1/customers/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable Long id) {
        log.info("Fetching customer with ID: {}", id);

        CustomerResponse response = customerService.getCustomerById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all customers (includes profile data)
     * GET /api/v1/customers
     */
    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        log.info("Fetching all customers");

        List<CustomerResponse> response = customerService.getAllCustomers();
        return ResponseEntity.ok(response);
    }

    /**
     * Get customers with pagination (includes profile data)
     * GET /api/v1/customers/paginated?page=0&size=10&sort=firstName,asc
     */
    @GetMapping("/paginated")
    public ResponseEntity<Page<CustomerResponse>> getCustomersWithPagination(Pageable pageable) {
        log.info("Fetching customers with pagination: {}", pageable);

        Page<CustomerResponse> response = customerService.getCustomersWithPagination(pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Update customer and profile
     * PUT /api/v1/customers/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(@PathVariable Long id,
        @Valid @RequestBody CustomerRequest request) {
        log.info("Updating customer with ID: {}", id);

        CustomerResponse response = customerService.updateCustomer(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete customer (profile deleted automatically)
     * DELETE /api/v1/customers/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        log.info("Deleting customer with ID: {}", id);

        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}