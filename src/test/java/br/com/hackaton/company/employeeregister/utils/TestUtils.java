package br.com.hackaton.company.employeeregister.utils;

import br.com.hackaton.company.employeeregister.enums.TypeRegister;
import br.com.hackaton.company.employeeregister.models.EmployeeRegisterModel;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class TestUtils {

    public static EmployeeRegisterModel employeeRegisterEntry() {

        Calendar calendar = Calendar.getInstance();

        calendar.set(2024, Calendar.MARCH, 20, 9, 1, 14);

        return EmployeeRegisterModel.builder()
                .id(999L)
                .registracionCode("M98765")
                .timeRegister(calendar.getTime())
                .typeRegister(TypeRegister.ENTRY)
                .build();
    }

    public static EmployeeRegisterModel employeeRegisterLunch() {

        Calendar calendar = Calendar.getInstance();

        calendar.set(2024, Calendar.MARCH, 20, 12, 15, 17);

        return EmployeeRegisterModel.builder()
                .id(999L)
                .registracionCode("M98765")
                .timeRegister(calendar.getTime())
                .typeRegister(TypeRegister.LUNCH)
                .build();
    }

    public static EmployeeRegisterModel employeeRegisterLunchreturn() {

        Calendar calendar = Calendar.getInstance();

        calendar.set(2024, Calendar.MARCH, 20, 13, 19, 14);

        return EmployeeRegisterModel.builder()
                .id(999L)
                .registracionCode("M98765")
                .timeRegister(calendar.getTime())
                .typeRegister(TypeRegister.LUNCH_RETURN)
                .build();
    }

    public static EmployeeRegisterModel employeeRegisterExit() {

        Calendar calendar = Calendar.getInstance();

        calendar.set(2024, Calendar.MARCH, 20, 18, 36, 44);

        return EmployeeRegisterModel.builder()
                .id(999L)
                .registracionCode("M98765")
                .timeRegister(calendar.getTime())
                .typeRegister(TypeRegister.EXIT)
                .build();
    }



}
