package br.com.hackaton.company.employeeregister.services;

import br.com.hackaton.company.employeeregister.models.EmployeeRegisterModel;
import br.com.hackaton.company.employeeregister.repositories.EmployeeRegisterRepository;
import br.com.hackaton.company.employeeregister.utils.TestUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmployeeRegisterServiceTest {

    @Mock
    EmployeeRegisterRepository employeeRegisterRepository;

    private EmployeeRegisterService service;

    AutoCloseable mock;

    @BeforeEach
    void setUp() {
        mock = MockitoAnnotations.openMocks(this);
        service = new EmployeeRegisterService(employeeRegisterRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @Nested
    class findByCodeTests {

        @Test
        void findEmployeeByRegistracionCode() {

            EmployeeRegisterModel employeeRegister = TestUtils.employeeRegisterEntry();
            EmployeeRegisterModel employeeRegister2 = TestUtils.employeeRegisterLunch();
            EmployeeRegisterModel employeeRegister3 = TestUtils.employeeRegisterLunchreturn();

            List<EmployeeRegisterModel> list = new ArrayList<>();


            list.add(employeeRegister);
            list.add(employeeRegister2);
            list.add(employeeRegister3);


            when(employeeRegisterRepository.findByRegistracionCode(any(String.class), any(Integer.class)))
                    .thenReturn(list);

            var listPreviousMonth = service.findByRegistracionCode(employeeRegister.getRegistracionCode());

            Assertions.assertThat(listPreviousMonth).isNotNull().isEqualTo(list);

            verify(employeeRegisterRepository, times(1))
                    .findByRegistracionCode(any(String.class), any(Integer.class));


        }

    }

}