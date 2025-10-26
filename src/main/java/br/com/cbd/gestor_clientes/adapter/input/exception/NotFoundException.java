package br.com.cbd.gestor_clientes.adapter.input.exception;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String message) {
        super(message);
    }
}
