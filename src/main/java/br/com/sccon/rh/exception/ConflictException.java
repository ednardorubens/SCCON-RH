package br.com.sccon.rh.exception;

public class ConflictException extends IllegalArgumentException {

    public ConflictException(Integer id) {
        super(String.format("O id '%d' já está em uso em outra entidade", id));
    }

}
