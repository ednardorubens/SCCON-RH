package br.com.sccon.rh.service;

import br.com.sccon.rh.domain.Person;
import br.com.sccon.rh.dto.PersonMapper;
import br.com.sccon.rh.enums.AgeOutputEnum;
import br.com.sccon.rh.enums.SalaryOutputEnum;
import br.com.sccon.rh.exception.ConflictException;
import br.com.sccon.rh.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@Scope("singleton")
@RequiredArgsConstructor
public class PersonService {

    private final PersonMapper personMapper;
    private final Map<Integer, Person> data = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        save(new Person(
            "José da Silva",
            LocalDate.of(2000, 4, 6),
            LocalDate.of(2020, 10, 5)
        ));
        save(new Person(
            "Luiz Fernando Bezerra",
            LocalDate.of(1971, 1, 17),
            LocalDate.of(1994, 2, 15)
        ));
        save(new Person(
            "Carlos Augusto Pires",
            LocalDate.of(1993, 7, 21),
            LocalDate.of(2010, 8, 22)
        ));
    }

    public List<Person> findAll() {
        return data.values()
            .stream()
            .sorted(Comparator.comparing(Person::getName))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    public Person findById(Integer id) {
        return Optional.ofNullable(id)
            .map(data::get)
            .map(Person.class::cast)
            .orElseThrow(() -> new NotFoundException(id));
    }

    public Person deleteById(Integer id) {
        return Optional.ofNullable(data.remove(id))
            .orElseThrow(() -> new NotFoundException(id));
    }

    public Person update(@NotNull Integer id, @NotNull Person person) {
        Person personDB = findById(id);
        return data.put(id, personMapper.updatePersonFromPersonDB(person, personDB));
    }

    public Person save(@NotNull Person person) {
        return Optional.ofNullable(person)
            .map(Person::getId)
            .map(id -> saveWithId(person, id))
            .orElseGet(() -> saveWithoutId(person));
    }

    public Integer getAge(LocalDate today, @NotNull Integer id, @NotNull AgeOutputEnum ageOutput) {
        Person personDB = findById(id);
        return (int) ageOutput.getUnit().between(personDB.getDateOfBirth(), today);
    }

    public BigDecimal getSalary(LocalDate today, @NotNull Integer id, @NotNull SalaryOutputEnum salaryOutput) {
        Person personDB = findById(id);
        BigDecimal bonus = BigDecimal.valueOf(500);
        BigDecimal percent = BigDecimal.valueOf(0.18d);
        BigDecimal minSalary = BigDecimal.valueOf(1302);
        BigDecimal initialSalary = BigDecimal.valueOf(1558);
        BigDecimal newSalary = BigDecimal.ZERO.add(initialSalary);
        Integer age = (int) ChronoUnit.YEARS.between(personDB.getDateOfAdmission(), today);
        for (int i = 0; i < age; i++) {
            newSalary = newSalary.multiply(BigDecimal.ONE.add(percent)).add(bonus);
        }
        if (SalaryOutputEnum.MIN.equals(salaryOutput)) {
            newSalary = newSalary.divide(minSalary, RoundingMode.UP);
        }
        return newSalary.setScale(2, RoundingMode.UP);
    }

    private Integer getNextId() {
        return 1 + data.keySet()
            .stream()
            .mapToInt(Integer::intValue)
            .max()
            .orElse(0);
    }

    private @NotNull Person saveWithId(Person person, Integer id) {
        if (!data.containsKey(id)) {
            data.put(id, person);
            return person;
        } else {
            throw new ConflictException(id);
        }
    }

    private @NotNull Person saveWithoutId(Person person) {
        Integer id = getNextId();
        person.setId(id);
        data.put(id, person);
        return person;
    }

}
