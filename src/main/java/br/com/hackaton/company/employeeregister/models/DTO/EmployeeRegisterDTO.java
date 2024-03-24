package br.com.hackaton.company.employeeregister.models.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public record EmployeeRegisterDTO(
        String registerCode,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/Sao_Paulo")
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        Date timeRegister) {
}
