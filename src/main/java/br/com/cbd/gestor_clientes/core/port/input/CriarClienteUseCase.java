package br.com.cbd.gestor_clientes.core.port.input;

import br.com.cbd.gestor_clientes.core.domain.model.Cliente;

public interface CriarClienteUseCase {
    Cliente create(Cliente cliente);
}
