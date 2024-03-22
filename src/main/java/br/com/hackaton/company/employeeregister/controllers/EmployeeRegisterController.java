package br.com.hackaton.company.employeeregister.controllers;


import br.com.hackaton.company.employeeregister.services.EmployeeRegisterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeRegisterController {


    private final EmployeeRegisterService employeeRegisterService;


    public EmployeeRegisterController(EmployeeRegisterService employeeRegisterService) {
        this.employeeRegisterService = employeeRegisterService;
    }




}
