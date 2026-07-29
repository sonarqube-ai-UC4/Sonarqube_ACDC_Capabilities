package com.example.onboarding;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.onboarding.dto.CreateCustomerRequest;
import com.example.onboarding.dto.UpdateStatusRequest;
import com.example.onboarding.model.OnboardingStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerOnboardingControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateAndRetrieveCustomer() throws Exception {
        CreateCustomerRequest request = new CreateCustomerRequest();
        request.setFirstName("Ada");
        request.setLastName("Lovelace");
        request.setEmail("ada@example.com");
        request.setPhone("+1234567890");
        request.setCompanyName("Example Labs");
        request.setRequestedProduct("Enterprise Suite");
        request.setNotes("Need setup support");

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email", is("ada@example.com")))
                .andExpect(jsonPath("$.status", is("NEW")));

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void shouldUpdateCustomerStatus() throws Exception {
        CreateCustomerRequest request = new CreateCustomerRequest();
        request.setFirstName("Grace");
        request.setLastName("Hopper");
        request.setEmail("grace@example.com");
        request.setPhone("+1987654321");
        request.setCompanyName("Tech Co");
        request.setRequestedProduct("Analytics");

        String response = mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        UpdateStatusRequest updateRequest = new UpdateStatusRequest();
        updateRequest.setStatus(OnboardingStatus.IN_REVIEW);

        mockMvc.perform(patch("/api/customers/" + id + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("IN_REVIEW")));
    }
}
