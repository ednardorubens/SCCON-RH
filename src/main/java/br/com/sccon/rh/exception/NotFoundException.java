package br.com.sccon.rh.exception;

public class NotFoundException extends IllegalArgumentException {

    public NotFoundException(Integer id) {
        super(String.format("O id '%d' não foi encontrado", id));
    }

}
