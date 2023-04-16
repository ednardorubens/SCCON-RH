package br.com.sccon.rh.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    private Integer id;
    private String name;
    private LocalDate dateOfBirth;
    private LocalDate dateOfAdmission;

    public Person(String name, LocalDate dateOfBirth, LocalDate dateOfAdmission) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.dateOfAdmission = dateOfAdmission;
    }

}
