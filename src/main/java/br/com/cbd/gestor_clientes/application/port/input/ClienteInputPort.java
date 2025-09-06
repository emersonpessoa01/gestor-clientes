package br.com.cbd.gestor_clientes.application.port.input;

import br.com.cbd.gestor_clientes.core.model.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteInputPort {
    Cliente create(Cliente cliente);
    Cliente update(Long id, Cliente cliente);
    void delete(Long id);
    Optional<Cliente> findById(Long id);
    List<Cliente> findAll();
    boolean validarCpf(String cpf);
    int contarClientesAtivos();
}
