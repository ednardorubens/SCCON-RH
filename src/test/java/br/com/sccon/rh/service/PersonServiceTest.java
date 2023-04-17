package br.com.sccon.rh.service;

import br.com.sccon.rh.domain.Person;
import br.com.sccon.rh.dto.PersonMapperImpl;
import br.com.sccon.rh.enums.AgeOutputEnum;
import br.com.sccon.rh.enums.SalaryOutputEnum;
import br.com.sccon.rh.exception.ConflictException;
import br.com.sccon.rh.exception.NotFoundException;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = {
    PersonMapperImpl.class,
    PersonService.class
})
@TestMethodOrder(OrderAnnotation.class)
public class PersonServiceTest {

    @Autowired
    private PersonService personRepository;

    @Test
    @Order(10)
    public void testSave_Success() {
        Person person = new Person(
            4,
            "Robson Carvalho",
            LocalDate.of(1996, 2, 11),
            LocalDate.of(2020, 10, 5)
        );
        Person personDB = personRepository.save(person);
        assertEquals(4, personDB.getId());
        assertEquals(person.getName(), personDB.getName());
        assertEquals(person.getDateOfBirth(), personDB.getDateOfBirth());
        assertEquals(person.getDateOfAdmission(), personDB.getDateOfAdmission());
    }

    @Test
    @Order(20)
    public void testSave_ConflictException() {
        Person person = new Person(
            1,
            "José da Silva",
            LocalDate.of(2000, 4, 6),
            LocalDate.of(2020, 10, 5)
        );
        ConflictException exception = assertThrows(ConflictException.class, () -> personRepository.save(person));
        assertEquals("O id '1' já está em uso em outra entidade", exception.getMessage());
    }

    @Test
    @Order(30)
    public void testFindAll() {
        List<Person> people = personRepository.findAll();
        assertEquals("Carlos Augusto Pires", people.get(0).getName());
        assertEquals("José da Silva", people.get(1).getName());
        assertEquals("Luiz Fernando Bezerra", people.get(2).getName());
    }

    @Test
    @Order(40)
    public void testFindById_Success() {
        Person person = personRepository.findById(4);
        assertEquals("Robson Carvalho", person.getName());
    }

    @Test
    @Order(50)
    public void testFindById_NotFoundException() {
        NotFoundException exception = assertThrows(NotFoundException.class, () -> personRepository.findById(10));
        assertEquals("O id '10' não foi encontrado", exception.getMessage());
    }

    @Test
    @Order(60)
    public void testUpdate_Success() {
        String name = "Robson Carvalho de Almeida";
        Person person = new Person();
        person.setName(name);

        Person personDB = personRepository.update(4, person);

        assertEquals(name, personDB.getName());
        assertEquals(LocalDate.of(1996, 2, 11), personDB.getDateOfBirth());
        assertEquals(LocalDate.of(2020, 10, 5), personDB.getDateOfAdmission());
    }

    @Test
    @Order(70)
    public void testUpdate_NotFoundException() {
        NotFoundException exception = assertThrows(NotFoundException.class, () -> personRepository.update(10, new Person()));
        assertEquals("O id '10' não foi encontrado", exception.getMessage());
    }

    @Test
    @Order(80)
    public void testDeleteById_Success() {
        Person personDB = personRepository.deleteById(4);

        assertEquals(4, personDB.getId());
        assertEquals("Robson Carvalho de Almeida", personDB.getName());
    }

    @Test
    @Order(90)
    public void testDeleteById_NotFoundException() {
        NotFoundException exception = assertThrows(NotFoundException.class, () -> personRepository.deleteById(10));
        assertEquals("O id '10' não foi encontrado", exception.getMessage());
    }

    @Test
    @Order(100)
    public void testAge() {
        assertEquals(8342, personRepository.getAge(LocalDate.of(2023, 2, 7), 1, AgeOutputEnum.DAYS));
        assertEquals(274, personRepository.getAge(LocalDate.of(2023, 2, 7), 1, AgeOutputEnum.MONTHS));
        assertEquals(22, personRepository.getAge(LocalDate.of(2023, 2, 7), 1, AgeOutputEnum.YEARS));
    }

    @Test
    @Order(110)
    public void testSalary() {
        assertEquals(BigDecimal.valueOf(3259.36d), personRepository.getSalary(LocalDate.of(2023, 2, 7), 1, SalaryOutputEnum.FULL));
        assertEquals(BigDecimal.valueOf(2.51d), personRepository.getSalary(LocalDate.of(2023, 2, 7), 1, SalaryOutputEnum.MIN));
    }

}
