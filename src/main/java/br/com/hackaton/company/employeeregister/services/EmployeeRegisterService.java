package br.com.hackaton.company.employeeregister.services;

import br.com.hackaton.company.employeeregister.models.EmployeeRegisterModel;
import br.com.hackaton.company.employeeregister.repositories.EmployeeRegisterRepository;

import java.time.LocalDate;
import java.util.List;

public class EmployeeRegisterService {

    private final EmployeeRegisterRepository repository;


    public EmployeeRegisterService(EmployeeRegisterRepository repository) {
        this.repository = repository;
    }

    public List<EmployeeRegisterModel> findByRegistracionCode(String employeeID) {
        LocalDate thisMonth = LocalDate.now();
        LocalDate previousMonth = thisMonth.minusMonths(1);

        return repository.findByRegistracionCode(employeeID, previousMonth.getMonthValue());
    }



}
