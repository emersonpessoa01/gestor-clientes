package br.com.cbd.gestor_clientes.core.port.in;

import br.com.cbd.gestor_clientes.core.domain.model.Cliente;

public interface AtualizarClienteUseCase {
    Cliente execute(Long id, Cliente cliente);
}
