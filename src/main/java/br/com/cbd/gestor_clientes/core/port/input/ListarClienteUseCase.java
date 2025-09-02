package br.com.cbd.gestor_clientes.core.port.input;

import br.com.cbd.gestor_clientes.core.domain.model.Cliente;

import java.util.List;

public interface ListarClienteUseCase {
    List<Cliente> findAll();
}
