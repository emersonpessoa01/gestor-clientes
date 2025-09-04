package br.com.cbd.gestor_clientes.application.port.input;

import br.com.cbd.gestor_clientes.core.model.Cliente;

import java.util.Optional;

public interface BuscarClienteUseCase {
    Optional<Cliente> findById(Long id);
}
