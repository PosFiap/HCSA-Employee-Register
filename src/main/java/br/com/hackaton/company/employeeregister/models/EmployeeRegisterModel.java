package br.com.hackaton.company.employeeregister.models;

import br.com.hackaton.company.employeeregister.enums.TypeRegister;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "TB_EMPLOYEE_REGISTERS")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeRegisterModel {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String registracionCode;
    private Date timeRegister;
    private TypeRegister typeRegister;

}
