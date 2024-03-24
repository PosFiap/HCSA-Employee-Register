package br.com.hackaton.company.employeeregister.controllers;


import br.com.hackaton.company.employeeregister.models.DTO.EmployeeRegisterDTO;
import br.com.hackaton.company.employeeregister.models.DTO.LogSenderDTO;
import br.com.hackaton.company.employeeregister.models.EmployeeRegisterModel;
import br.com.hackaton.company.employeeregister.services.EmployeeRegisterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeRegisterController {


    private final EmployeeRegisterService employeeRegisterService;


    public EmployeeRegisterController(EmployeeRegisterService employeeRegisterService) {
        this.employeeRegisterService = employeeRegisterService;
    }


    @PostMapping(value = "/register")
    public ResponseEntity<EmployeeRegisterDTO> registerEmployeeHour(@RequestBody String register) {
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeRegisterService.registerTimeEmployee(register));
    }

    @GetMapping(value = "/logs/{id}")
    public ResponseEntity<List<EmployeeRegisterModel>> findAllEmployeeLogsDay(
            @PathVariable(value = "id") String register,
            @RequestParam("date") String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(employeeRegisterService.findByEmployeeAndDate(register, localDate));
    }

    @PostMapping(value = "/logs")
    public ResponseEntity<String> logSenderToEmail(
            @RequestBody LogSenderDTO sender) {

        boolean hasList = employeeRegisterService.sendEmail(sender.matricula(), sender.email());

        if (!hasList) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Não foi encotrado dados, o envio não pôde ser concluido.");
        }

        return ResponseEntity.status(HttpStatus.OK).body("O Envio foi realizado com sucesso");
    }



}
