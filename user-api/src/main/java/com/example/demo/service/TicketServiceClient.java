package com.example.demo.service;

import com.example.demo.controller.dto.OnboardingTicketRequest;
import com.example.demo.repository.entity.User;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TicketServiceClient {

    private final RestTemplate restTemplate;
    private final String TICKET_SERVICE_URL = "http://localhost:8081/api/v1/ticket";

    public TicketServiceClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public void createOnboardingTickets(User user) {
        try {
            String userName = user.getProfile() != null ? user.getProfile().getName() : user.getHandle();

            OnboardingTicketRequest request = new OnboardingTicketRequest(
                    user.getId(),
                    user.getEmail(),
                    userName,
                    user.getHandle()
            );

            ResponseEntity<Void> response = restTemplate.postForEntity(
                    TICKET_SERVICE_URL + "/onBoarding",
                    request,
                    Void.class
            );

            if (!response.getStatusCode().is2xxSuccessful()) {
                System.err.println("Falha ao criar tickets de onboarding - Status: " + response.getStatusCode());
            }

        } catch (Exception e) {
            System.err.println("Erro ao criar tickets no ms-ticket: " + e.getMessage());
        }
    }
}
