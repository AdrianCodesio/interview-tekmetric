package com.interview.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity representing a customer with basic contact information.
 *
 * <p>Includes optional one-to-one relationship with CustomerProfile for
 * additional customer details and one-to-many relationship with Vehicle.
 * Inherits audit fields from BaseEntity.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customers")
public class Customer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "phone", length = 20)
    private String phone;

    @OneToOne(mappedBy = "customer",
              cascade = {CascadeType.PERSIST, CascadeType.MERGE},
              fetch = FetchType.LAZY,
              orphanRemoval = true)
    private CustomerProfile customerProfile;

    @OneToMany(mappedBy = "customer",
               fetch = FetchType.LAZY,
               orphanRemoval = true)
    private List<Vehicle> vehicles = new ArrayList<>();
}
