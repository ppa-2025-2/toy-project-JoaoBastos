package com.example.demo.controller.dto;

import com.example.demo.repository.entity.Island;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NewIslandDTO(
        @NotNull(message = "A disposição é obrigatória")
        Island.Disposition disposition,

        @NotNull(message = "A descrição é obrigatória")
        @NotBlank(message = "Não pode ser composta apenas de espaços")
        String description
) {}