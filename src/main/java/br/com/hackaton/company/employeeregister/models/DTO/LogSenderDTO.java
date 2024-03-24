package br.com.hackaton.company.employeeregister.models.DTO;

import jakarta.validation.constraints.NotBlank;

public record LogSenderDTO(@NotBlank String matricula, @NotBlank String email) {
}
