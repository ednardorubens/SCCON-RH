package br.com.sccon.rh.controller;

import br.com.sccon.rh.domain.Person;
import br.com.sccon.rh.enums.AgeOutputEnum;
import br.com.sccon.rh.enums.SalaryOutputEnum;
import br.com.sccon.rh.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

import javax.validation.Valid;

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
    @Operation(summary = "Listar pessoas")
    public List<Person> findAll() {
        return personService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Salvar pessoas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pessoa criada com sucesso"),
        @ApiResponse(responseCode = "409", description = "Pessoa já existe", content = @Content(schema = @Schema(hidden = true)))
    })
    public Person save(@Valid @RequestBody Person person) {
        return personService.save(person);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remover uma pessoa")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Pessoa removida com sucesso"),
        @ApiResponse(responseCode = "404", description = "Pessoa não encontrada", content = @Content(schema = @Schema(hidden = true)))
    })
    public void deleteById(@PathVariable("id") Integer id) {
        personService.deleteById(id);
    }

    @Operation(summary = "Atualizar uma pessoa")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pessoa atualizada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Pessoa não encontrada", content = @Content(schema = @Schema(hidden = true)))
    })
    @RequestMapping(path = "/{id}", method = { RequestMethod.PUT, RequestMethod.PATCH })
    public Person update(@PathVariable("id") Integer id, @RequestBody Person person) {
        return personService.update(id, person);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar uma pessoa")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pessoa encontrada"),
        @ApiResponse(responseCode = "404", description = "Pessoa não encontrada", content = @Content(schema = @Schema(hidden = true)))
    })
    public Person findById(@PathVariable("id") Integer id) {
        return personService.findById(id);
    }

    @GetMapping("/{id}/age")
    @Operation(summary = "Retornar a idade da pessoa")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Idade da pessoa encontrada", content = @Content(schema = @Schema(type = "number", example = "8432"))),
        @ApiResponse(responseCode = "404", description = "Pessoa não encontrada", content = @Content(schema = @Schema(hidden = true)))
    })
    public Integer getAge(@PathVariable("id") Integer id, @RequestParam("output") AgeOutputEnum output) {
        return personService.getAge(LocalDate.now(), id, output);
    }

    @GetMapping("/{id}/salary")
    @Operation(summary = "Retornar o salário da pessoa")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Salário da pessoa encontrada", content = @Content(schema = @Schema(type = "number", example = "3526.56"))),
        @ApiResponse(responseCode = "404", description = "Pessoa não encontrada", content = @Content(schema = @Schema(hidden = true)))
    })
    public BigDecimal getSalary(@PathVariable("id") Integer id, @RequestParam("output") SalaryOutputEnum output) {
        return personService.getSalary(LocalDate.now(), id, output);
    }

}
