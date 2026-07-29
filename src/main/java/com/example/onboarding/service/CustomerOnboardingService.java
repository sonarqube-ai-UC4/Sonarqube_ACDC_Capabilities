package com.example.onboarding.service;

import com.example.onboarding.dto.CreateCustomerRequest;
import com.example.onboarding.dto.CustomerResponse;
import com.example.onboarding.dto.UpdateStatusRequest;
import com.example.onboarding.model.CustomerOnboarding;
import com.example.onboarding.model.OnboardingStatus;
import com.example.onboarding.repository.CustomerOnboardingRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerOnboardingService {
    private final CustomerOnboardingRepository repository;

    public CustomerOnboardingService(CustomerOnboardingRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public CustomerResponse createCustomer(CreateCustomerRequest request) {
        CustomerOnboarding entity = new CustomerOnboarding();
        entity.setFirstName(request.getFirstName());
        entity.setLastName(request.getLastName());
        entity.setEmail(request.getEmail());
        entity.setPhone(request.getPhone());
        entity.setCompanyName(request.getCompanyName());
        entity.setRequestedProduct(request.getRequestedProduct());
        entity.setNotes(request.getNotes());
        entity.setStatus(OnboardingStatus.NEW);
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());
        CustomerOnboarding saved = repository.save(entity);
        return toResponse(saved);
    }

    public List<CustomerResponse> findAll() {
        return repository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public CustomerResponse findById(Long id) {
        CustomerOnboarding customer = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found: " + id));
        return toResponse(customer);
    }

    @Transactional
    public CustomerResponse updateStatus(Long id, UpdateStatusRequest request) {
        CustomerOnboarding customer = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found: " + id));
        customer.setStatus(request.getStatus());
        customer.setUpdatedAt(Instant.now());
        return toResponse(repository.save(customer));
    }

    private CustomerResponse toResponse(CustomerOnboarding entity) {
        return new CustomerResponse(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getCompanyName(),
                entity.getRequestedProduct(),
                entity.getNotes(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
