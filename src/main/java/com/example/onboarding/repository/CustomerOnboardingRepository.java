package com.example.onboarding.repository;

import com.example.onboarding.model.CustomerOnboarding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerOnboardingRepository extends JpaRepository<CustomerOnboarding, Long> {
}
