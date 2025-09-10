package br.com.cbd.gestor_clientes.port.input;

import br.com.cbd.gestor_clientes.core.domain.model.Cliente;

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
    Optional<Cliente> buscarPorCpf(String cpf);
    List<Cliente> listarAtivos();
}
