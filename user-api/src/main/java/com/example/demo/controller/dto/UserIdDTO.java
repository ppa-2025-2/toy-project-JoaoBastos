
package com.example.demo.controller.dto;

import jakarta.validation.constraints.NotNull;

public record UserIdDTO(
        @NotNull(message = "O usuário é obrigatório")
        Integer user_id
) {
}
