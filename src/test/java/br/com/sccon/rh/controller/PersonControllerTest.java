package br.com.sccon.rh.controller;

import br.com.sccon.rh.domain.Person;
import br.com.sccon.rh.enums.SalaryOutputEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(OrderAnnotation.class)
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(10)
    public void testSave() throws Exception {
        Person person = new Person(
            4,
            "Robson Carvalho",
            LocalDate.of(1996, 2, 11),
            LocalDate.of(2020, 10, 5)
        );

        final MockHttpServletRequestBuilder request = post(PersonController.PATH)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(person));

        mockMvc.perform(request)
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value("4"))
            .andExpect(jsonPath("$.name").value(person.getName()))
            .andExpect(jsonPath("$.dateOfBirth").value(objectMapper.writeValueAsString(person.getDateOfBirth()))::equals)
            .andExpect(jsonPath("$.dateOfAdmission").value(objectMapper.writeValueAsString(person.getDateOfAdmission()))::equals);
    }

    @Test
    @Order(20)
    public void testSaveWithoutId() throws Exception {
        Person person = new Person(
            "Robson Carvalho",
            LocalDate.of(1996, 2, 11),
            LocalDate.of(2020, 10, 5)
        );

        final MockHttpServletRequestBuilder request = post(PersonController.PATH)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(person));

        mockMvc.perform(request)
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value("5"))
            .andExpect(jsonPath("$.name").value(person.getName()))
            .andExpect(jsonPath("$.dateOfBirth").value(objectMapper.writeValueAsString(person.getDateOfBirth()))::equals)
            .andExpect(jsonPath("$.dateOfAdmission").value(objectMapper.writeValueAsString(person.getDateOfAdmission()))::equals);
    }

    @Test
    @Order(30)
    public void testFindAll() throws Exception {
        final MockHttpServletRequestBuilder request = get(PersonController.PATH);

        mockMvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$[3].id").value("4"))
            .andExpect(jsonPath("$[3].name").value("Robson Carvalho"))
            .andExpect(jsonPath("$[3].dateOfBirth").value("1996-02-11"))
            .andExpect(jsonPath("$[3].dateOfAdmission").value("2020-10-05"));
    }

    @Test
    @Order(40)
    public void testFindById() throws Exception {
        final MockHttpServletRequestBuilder request = get(PersonController.PATH + "/4");

        mockMvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value("4"))
            .andExpect(jsonPath("$.name").value("Robson Carvalho"))
            .andExpect(jsonPath("$.dateOfBirth").value("1996-02-11"))
            .andExpect(jsonPath("$.dateOfAdmission").value("2020-10-05"));
    }

    @Test
    @Order(50)
    public void testFindByIdNotFound() throws Exception {
        final MockHttpServletRequestBuilder request = get(PersonController.PATH + "/10");

        mockMvc.perform(request)
            .andExpect(status().isNotFound());
    }

    @Test
    @Order(60)
    public void testUpdate() throws Exception {
        Person person = new Person();
        person.setName("Robson Carvalho de Almeida");

        MockHttpServletRequestBuilder request = put(PersonController.PATH + "/4")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(person));

        mockMvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value("4"))
            .andExpect(jsonPath("$.name").value(person.getName()))
            .andExpect(jsonPath("$.dateOfBirth").value("1996-02-11"))
            .andExpect(jsonPath("$.dateOfAdmission").value("2020-10-05"));

        person.setName("Robson Carvalho Vasconcelos");

        request = patch(PersonController.PATH + "/4")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(person));

        mockMvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value("4"))
            .andExpect(jsonPath("$.name").value(person.getName()))
            .andExpect(jsonPath("$.dateOfBirth").value("1996-02-11"))
            .andExpect(jsonPath("$.dateOfAdmission").value("2020-10-05"));
    }

    @Test
    @Order(70)
    public void testUpdateNotFound() throws Exception {
        Person person = new Person();
        person.setName("Robson Carvalho de Almeida");

        MockHttpServletRequestBuilder request = put(PersonController.PATH + "/10")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(person));

        mockMvc.perform(request)
            .andExpect(status().isNotFound());

        request = patch(PersonController.PATH + "/10")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(person));

        mockMvc.perform(request)
            .andExpect(status().isNotFound());
    }

    @Test
    @Order(80)
    public void testDeleteById() throws Exception {
        final MockHttpServletRequestBuilder request = delete(PersonController.PATH + "/4");
        mockMvc.perform(request)
            .andExpect(status().isNoContent())
            .andExpect(content().string(Matchers.blankOrNullString()));
    }

    @Test
    @Order(90)
    public void testDeleteByIdNotFound() throws Exception {
        final MockHttpServletRequestBuilder request = delete(PersonController.PATH + "/10");
        mockMvc.perform(request)
            .andExpect(status().isNotFound());
    }

    @Test
    @Order(100)
    public void testGetAge() throws Exception {
        final MockHttpServletRequestBuilder request = get(PersonController.PATH + "/1/age?output=days");
        mockMvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(content().string(Matchers.notNullValue()));
    }

    @Test
    @Order(110)
    public void testGetAgeNotFound() throws Exception {
        final MockHttpServletRequestBuilder request = get(PersonController.PATH + "/10/age?output=days");
        mockMvc.perform(request)
            .andExpect(status().isNotFound());
    }

    @Test
    @Order(120)
    public void testGetSalary() throws Exception {
        getSalary(SalaryOutputEnum.FULL, "3259.36");
        getSalary(SalaryOutputEnum.MIN, "2.51");
    }

    @Test
    @Order(130)
    public void testGetSalaryNotFound() throws Exception {
        final MockHttpServletRequestBuilder request = get(PersonController.PATH + "/10/salary?output=min");
        mockMvc.perform(request)
            .andExpect(status().isNotFound());
    }

    private void getSalary(SalaryOutputEnum output, String qtdReturned) throws Exception {
        final MockHttpServletRequestBuilder request = get(PersonController.PATH + "/1/salary?output=" + output);
        mockMvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(content().string(qtdReturned));
    }

}
