package br.com.cbd.gestor_clientes.port.output;

import br.com.cbd.gestor_clientes.core.domain.model.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteOutputPort {
    Cliente save(Cliente cliente);

    Optional<Cliente> findById(Long id);

    List<Cliente> findAll();

    Cliente update(Cliente cliente);

    void delete(Long id);

    boolean existsByCpf(String cpf); // Para validar duplicidade de CPF

    boolean existsByEmail(String email); // Para validar duplicidade de Email

    Optional<Cliente> findByCpf(String cpf);
}
