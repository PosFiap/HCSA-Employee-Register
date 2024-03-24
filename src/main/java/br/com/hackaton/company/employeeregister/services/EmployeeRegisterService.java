package br.com.hackaton.company.employeeregister.services;

import br.com.hackaton.company.employeeregister.models.DTO.EmployeeRegisterDTO;
import br.com.hackaton.company.employeeregister.models.EmployeeRegisterModel;
import br.com.hackaton.company.employeeregister.repositories.EmployeeRegisterRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class EmployeeRegisterService {

    private final EmployeeRegisterRepository repository;

    private final EmailService emailService;


    public EmployeeRegisterService(EmployeeRegisterRepository repository, EmailService emailService) {
        this.repository = repository;
        this.emailService = emailService;
    }

    public List<EmployeeRegisterModel> findByRegistracionCode(String employeeID) {
        LocalDate thisMonth = LocalDate.now();
        LocalDate previousMonth = thisMonth.minusMonths(1);

        return repository.findByRegistracionCode(employeeID, previousMonth.getMonthValue());
    }

    public boolean sendEmail(String employeeId, String receiver) {

        List<EmployeeRegisterModel> employeeRegisters = findByRegistracionCode(employeeId);

        if (employeeRegisters.isEmpty()) {
            return false;
        }

        String subject = "Relação Mensal do funcionário " + employeeId;
        String content = "";
        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder
                .append("Para o funcionário ")
                .append(employeeId)
                .append(", no mês passado, as horas registradas são: ");

        employeeRegisters.forEach(register -> contentBuilder.append("\nMatrícula: ")
                .append(register.getRegistracionCode())
                .append(" Hora Registrada: ")
                .append(register.getTimeRegister()));

        content = contentBuilder.toString();
        emailService.sendMail(receiver, subject, content);

        return true;
    }

    public EmployeeRegisterDTO registerTimeEmployee(String matricula) {

        EmployeeRegisterModel newRegister = EmployeeRegisterModel.builder()
                .registracionCode(matricula)
                .timeRegister(Calendar.getInstance().getTime())
                .build();

        EmployeeRegisterModel registered = repository.save(newRegister);
        return new EmployeeRegisterDTO(registered.getRegistracionCode(), registered.getTimeRegister());
    }

    public List<EmployeeRegisterModel> findByEmployeeAndDate(String employeeId, LocalDate date) {
        return repository.findByRegistracionCodeAndDate(
                employeeId, date.getYear(), date.getMonthValue(), date.getDayOfMonth());
    }




}
