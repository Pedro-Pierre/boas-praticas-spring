package com.pedro.avaliacaospringapi.config.exception.notFound;

public class IdNotFoundException extends NotFoundException {
    public IdNotFoundException() {
        super("Id não encontrado");
    }
}
