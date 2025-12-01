package com.example.demo.controller.dto;

public record TicketCreateDTO(
        String object,
        String action,
        String details,
        String local,
        String status,
        Integer creatorId
) {
}