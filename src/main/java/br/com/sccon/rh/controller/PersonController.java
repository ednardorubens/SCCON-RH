package br.com.sccon.rh.controller;

import br.com.sccon.rh.domain.Person;
import br.com.sccon.rh.enums.AgeOutputEnum;
import br.com.sccon.rh.enums.SalaryOutputEnum;
import br.com.sccon.rh.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(PersonController.PATH)
public class PersonController {

    private final PersonService personService;
    public static final String PATH = "/person";

    @GetMapping
    public List<Person> findAll() {
        return personService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Person save(@RequestBody Person person) {
        return personService.save(person);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("id") Integer id) {
        personService.deleteById(id);
    }

    @RequestMapping(path = "/{id}", method = { RequestMethod.PUT, RequestMethod.PATCH })
    public Person update(@PathVariable("id") Integer id, @RequestBody Person person) {
        return personService.update(id, person);
    }

    @GetMapping("/{id}")
    public Person findById(@PathVariable("id") Integer id) {
        return personService.findById(id);
    }

    @GetMapping("/{id}/age")
    public Integer getAge(@PathVariable("id") Integer id, @RequestParam("output") AgeOutputEnum output) {
        return personService.getAge(LocalDate.now(), id, output);
    }

    @GetMapping("/{id}/salary")
    public BigDecimal getSalary(@PathVariable("id") Integer id, @RequestParam("output") SalaryOutputEnum output) {
        return personService.getSalary(LocalDate.now(), id, output);
    }

}
