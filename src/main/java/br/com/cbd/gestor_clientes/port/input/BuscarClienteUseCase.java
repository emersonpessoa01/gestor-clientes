package br.com.cbd.gestor_clientes.port.input;

import br.com.cbd.gestor_clientes.core.domain.model.Cliente;

import java.util.Optional;

public interface BuscarClienteUseCase {
    Optional<Cliente> findById(Long id);
}
