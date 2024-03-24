package br.com.hackaton.company.employeeregister.repositories;

import br.com.hackaton.company.employeeregister.models.EmployeeRegisterModel;
import br.com.hackaton.company.employeeregister.utils.TestUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmployeeRegisterRepositoryTest {

    @Mock
    EmployeeRegisterRepository employeeRegisterRepository;

    AutoCloseable openMocks;

    @BeforeEach()
    void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception{
        openMocks.close();
    }


    @Nested
    class EmployeeRegisterTest {
        @Test
        void saveEmployeeRegisterWhenEntryTest() {

            EmployeeRegisterModel employeeRegister = TestUtils.employeeRegisterEntry();

            when(employeeRegisterRepository.save(any(EmployeeRegisterModel.class)))
                    .thenReturn(employeeRegister);

            var employeeRegistered = employeeRegisterRepository.save(employeeRegister);

            Assertions.assertThat(employeeRegistered)
                    .isNotNull()
                    .isInstanceOf(EmployeeRegisterModel.class)
                    .isEqualTo(employeeRegister);

            verify(employeeRegisterRepository, times(1))
                    .save(any(EmployeeRegisterModel.class));


        }

        @Test
        void saveEmployeeRegisterWhenLunchTest() {

            EmployeeRegisterModel employeeRegister = TestUtils.employeeRegisterLunch();

            when(employeeRegisterRepository.save(any(EmployeeRegisterModel.class)))
                    .thenReturn(employeeRegister);

            var employeeRegistered = employeeRegisterRepository.save(employeeRegister);

            Assertions.assertThat(employeeRegistered)
                    .isNotNull()
                    .isInstanceOf(EmployeeRegisterModel.class)
                    .isEqualTo(employeeRegister);

            verify(employeeRegisterRepository, times(1))
                    .save(any(EmployeeRegisterModel.class));


        }

        @Test
        void saveEmployeeRegisterWhenLunchReturnTest() {

            EmployeeRegisterModel employeeRegister = TestUtils.employeeRegisterLunchreturn();

            when(employeeRegisterRepository.save(any(EmployeeRegisterModel.class)))
                    .thenReturn(employeeRegister);

            var employeeRegistered = employeeRegisterRepository.save(employeeRegister);

            Assertions.assertThat(employeeRegistered)
                    .isNotNull()
                    .isInstanceOf(EmployeeRegisterModel.class)
                    .isEqualTo(employeeRegister);

            verify(employeeRegisterRepository, times(1))
                    .save(any(EmployeeRegisterModel.class));


        }

        @Test
        void saveEmployeeRegisterWhenExitTest() {

            EmployeeRegisterModel employeeRegister = TestUtils.employeeRegisterExit();

            when(employeeRegisterRepository.save(any(EmployeeRegisterModel.class)))
                    .thenReturn(employeeRegister);

            var employeeRegistered = employeeRegisterRepository.save(employeeRegister);

            Assertions.assertThat(employeeRegistered)
                    .isNotNull()
                    .isInstanceOf(EmployeeRegisterModel.class)
                    .isEqualTo(employeeRegister);

            verify(employeeRegisterRepository, times(1))
                    .save(any(EmployeeRegisterModel.class));


        }

    }

    @Nested
    class findEmployee {

        @Test
        void findEmployeeByCodeTest() {

            EmployeeRegisterModel employeeRegister = TestUtils.employeeRegisterEntry();
            EmployeeRegisterModel employeeRegister2 = TestUtils.employeeRegisterLunch();

            List<EmployeeRegisterModel> list = new ArrayList<>();

            list.add(employeeRegister);
            list.add(employeeRegister2);

            when(employeeRegisterRepository.findByRegistracionCode(any(String.class), any(Integer.class))).thenReturn(list);

            var employeeList = employeeRegisterRepository
                    .findByRegistracionCode(employeeRegister.getRegistracionCode(), 3);

            Assertions.assertThat(employeeList).isNotNull().isEqualTo(list);

            verify(employeeRegisterRepository, times(1))
                    .findByRegistracionCode(any(String.class), any(Integer.class));

        }

        @Test
        void findEmployeeByDatesTest() {

            EmployeeRegisterModel employeeRegister = TestUtils.employeeRegisterEntry();
            EmployeeRegisterModel employeeRegister2 = TestUtils.employeeRegisterLunch();

            List<EmployeeRegisterModel> list = new ArrayList<>();

            list.add(employeeRegister);
            list.add(employeeRegister2);

            when(employeeRegisterRepository.findByRegistracionCodeAndDate(
                    any(String.class),
                    any(Integer.class),
                    any(Integer.class),
                    any(Integer.class))).thenReturn(list);

            var employeeList = employeeRegisterRepository
                    .findByRegistracionCodeAndDate(employeeRegister.getRegistracionCode(), 2024, 3, 2);

            Assertions.assertThat(employeeList).isNotNull().isEqualTo(list);

            verify(employeeRegisterRepository, times(1))
                    .findByRegistracionCodeAndDate(
                            any(String.class),
                            any(Integer.class),
                            any(Integer.class),
                            any(Integer.class));

        }

    }


}