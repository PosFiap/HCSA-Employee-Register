package br.com.hackaton.company.employeeregister.controllers;

import br.com.hackaton.company.employeeregister.models.DTO.EmployeeRegisterDTO;
import br.com.hackaton.company.employeeregister.models.DTO.LogSenderDTO;
import br.com.hackaton.company.employeeregister.models.EmployeeRegisterModel;
import br.com.hackaton.company.employeeregister.services.EmployeeRegisterService;
import br.com.hackaton.company.employeeregister.utils.TestUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class EmployeeRegisterControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmployeeRegisterService service;

    AutoCloseable mock;

    @BeforeEach
    void setUp() {
        mock = MockitoAnnotations.openMocks(this);
        EmployeeRegisterController controller = new EmployeeRegisterController(service);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addFilter((request, response, chain) -> {
                    response.setCharacterEncoding("UTF-8");
                    chain.doFilter(request, response);
                }, "/*")
                .build();

    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @Test
    void registerEmployeeHourTest() throws Exception {

        var register = "M123456";

        LocalDate localDate = LocalDate.now();
        Date date = new Date(System.currentTimeMillis());

        when(service.registerTimeEmployee(any(String.class))).thenReturn(new EmployeeRegisterDTO(register, date));

        mockMvc.perform(
                post("/employee/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(register)
        ).andDo(print()).andExpect(status().isCreated());

        verify(service, times(1)).registerTimeEmployee(anyString());

    }

    @Test
    void findAllEmployeeLogsDayTest() throws Exception {

        var register = "M123456";
        var date = "2024-03-20";

        EmployeeRegisterModel employeeRegister = TestUtils.employeeRegisterEntry();
        EmployeeRegisterModel employeeRegister2 = TestUtils.employeeRegisterLunch();
        EmployeeRegisterModel employeeRegister3 = TestUtils.employeeRegisterLunchreturn();

        List<EmployeeRegisterModel> list =
                new ArrayList<>(Arrays.asList(employeeRegister, employeeRegister2, employeeRegister3));


        when(service.findByEmployeeAndDate(anyString(), any(LocalDate.class))).thenReturn(list);

        mockMvc.perform(
                get("/employee/logs/{id}", register)
                        .param("date", date)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(list.get(0).getId().toString()))
                .andExpect(jsonPath("$.[0].registracionCode").value(list.get(0).getRegistracionCode()));


        verify(service, times(1)).findByEmployeeAndDate(anyString(), any(LocalDate.class));
    }

    @Test
    void logSenderToEmailTest() throws Exception {

        var sender = new LogSenderDTO("M123456", "email@example.com");

        when(service.sendEmail(anyString(), anyString())).thenReturn(true);

        mockMvc.perform(
                post("/employee/logs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(sender))
        )
                .andDo(print())
                .andExpect(status().isOk());


        verify(service, times(1)).sendEmail(anyString(), anyString());
    }

    @Test
    void logSenderToEmailWhenFalseTest() throws Exception {

        var sender = new LogSenderDTO("M123456", "email@example.com");

        when(service.sendEmail(anyString(), anyString())).thenReturn(false);

        mockMvc.perform(
                        post("/employee/logs")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(sender))
                )
                .andDo(print())
                .andExpect(status().isNotFound());


        verify(service, times(1)).sendEmail(anyString(), anyString());
    }


    public static String asJsonString(final Object obj) throws JsonProcessingException {

        ObjectMapper om = new ObjectMapper();

        om.registerModule(new JavaTimeModule());

        return om.writeValueAsString(obj);
    }
}












