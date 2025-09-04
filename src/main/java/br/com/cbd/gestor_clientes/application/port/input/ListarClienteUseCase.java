package br.com.cbd.gestor_clientes.application.port.input;

import br.com.cbd.gestor_clientes.core.model.Cliente;

import java.util.List;

public interface ListarClienteUseCase {
    List<Cliente> findAll();
}
