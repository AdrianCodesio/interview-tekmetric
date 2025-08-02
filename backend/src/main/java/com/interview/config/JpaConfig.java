package com.interview.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JPA configuration for enabling auditing functionality.
 *
 * <p>Enables automatic population of audit fields (createdDate, updatedDate)
 * in entities that extend BaseEntity.
 */
@Configuration
@EnableJpaAuditing
public class JpaConfig {
}