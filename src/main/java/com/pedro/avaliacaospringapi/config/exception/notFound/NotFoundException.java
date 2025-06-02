package com.pedro.avaliacaospringapi.config.exception.notFound;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String mensagem) {
        super(mensagem);
    }
}
