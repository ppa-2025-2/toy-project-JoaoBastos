package com.example.demo.controller.dto;

public class OnboardingTicketRequest {
    private Integer userId;
    private String email;
    private String name;
    private String handle;

    public OnboardingTicketRequest() {}

    public OnboardingTicketRequest(Integer userId, String email, String name, String handle) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.handle = handle;
    }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getHandle() { return handle; }
    public void setHandle(String handle) { this.handle = handle; }
}
