package com.example.onboarding.dto;

import com.example.onboarding.model.OnboardingStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateStatusRequest {
    @NotNull
    private OnboardingStatus status;

    public OnboardingStatus getStatus() {
        return status;
    }

    public void setStatus(OnboardingStatus status) {
        this.status = status;
    }
}
