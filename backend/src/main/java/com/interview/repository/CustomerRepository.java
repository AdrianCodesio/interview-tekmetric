package com.interview.repository;

import com.interview.entity.Customer;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository for Customer entity operations.
 *
 * <p>Provides standard CRUD operations plus custom queries for finding
 * customers by email and fetching customers with their profiles.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT c FROM Customer c LEFT JOIN FETCH c.customerProfile WHERE c.id = :id")
    Optional<Customer> findByIdWithProfile(@Param("id") Long id);
}
