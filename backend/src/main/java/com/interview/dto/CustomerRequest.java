package com.interview.dto;

import com.interview.enums.ContactMethod;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record CustomerRequest(
    @NotBlank(message = "First name is required")
    @Size(max = 100, message = "First name must not exceed 100 characters")
    String firstName,

    @NotBlank(message = "Last name is required")
    @Size(max = 100, message = "Last name must not exceed 100 characters")
    String lastName,

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    String email,

    @Size(max = 20, message = "Phone must not exceed 20 characters")
    String phone,

    // Profile fields
    String address,

    @PastOrPresent(message = "Date of birth cannot be in the future") LocalDate dateOfBirth,

    ContactMethod preferredContactMethod
) {}
