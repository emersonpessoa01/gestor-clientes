package br.com.cbd.gestor_clientes.repository;

import br.com.cbd.gestor_clientes.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByEmail(String email);
    Optional<Cliente> findByCpf(String cpf);
    List<Cliente> findByStatus(String status);
    List<Cliente> findByNomeContainingIgnoreCase(String nome);

}
