package br.com.cbd.gestor_clientes.core.port.input;

import br.com.cbd.gestor_clientes.core.domain.model.Cliente;

public interface AtualizarClienteUseCase {
    Cliente update(Long id, Cliente cliente);
}
