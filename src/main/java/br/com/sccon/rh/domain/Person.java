package br.com.sccon.rh.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    private Integer id;

    @NotBlank
    private String name;

    @NotNull
    private LocalDate dateOfBirth;

    @NotNull
    private LocalDate dateOfAdmission;

    public Person(String name, LocalDate dateOfBirth, LocalDate dateOfAdmission) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.dateOfAdmission = dateOfAdmission;
    }

}
