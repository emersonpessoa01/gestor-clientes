package br.com.cbd.gestor_clientes.adapter.input.controller;

import br.com.cbd.gestor_clientes.adapter.input.exception.BusinessException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teste")
public class TesteController {

    @GetMapping
    public String ping() {
        return "pong";
    }
    @GetMapping("/erro-negocio")
    public void erroNegocio() {
        throw new BusinessException("Erro de negócio simulado");
    }

    @GetMapping("/erro-generico")
    public void erroGenerico() {
        throw new RuntimeException("Erro genérico simulado");
    }
}
