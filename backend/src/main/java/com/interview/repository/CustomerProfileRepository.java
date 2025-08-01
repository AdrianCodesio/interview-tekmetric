package com.interview.repository;

import com.interview.entity.CustomerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerProfileRepository extends JpaRepository<CustomerProfile, Long> {

    Optional<CustomerProfile> findByCustomerId(Long customerId);

    @Query("SELECT cp FROM CustomerProfile cp JOIN cp.customer c WHERE c.email = :email")
    Optional<CustomerProfile> findByCustomerEmail(@Param("email") String email);

    boolean existsByCustomerId(Long customerId);
}
