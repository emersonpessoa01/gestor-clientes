package br.com.cbd.gestor_clientes.application.port.input;

import br.com.cbd.gestor_clientes.core.model.Cliente;

public interface CriarClienteUseCase {
    Cliente create(Cliente cliente);
}
