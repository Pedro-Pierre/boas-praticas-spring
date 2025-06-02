package com.pedro.avaliacaospringapi.config.exception.notFound;

public class NameNotFoundException extends NotFoundException {
    public NameNotFoundException() {
        super("Nome ou descrição não encontrada");
    }
}
