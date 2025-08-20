package br.com.cbd.gestor_clientes.repository;

import br.com.cbd.gestor_clientes.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    List<Cliente> findAllByOrderByIdAsc();
    Optional<Cliente> findByEmailOrderByIdAsc(String email);
    Optional<Cliente> findByCpfOrderByIdAsc(String cpf);
    List<Cliente> findByStatusOrderByIdAsc(String status);
    List<Cliente> findByNomeContainingIgnoreCaseOrderByIdAsc(String nome);
    List<Cliente> findByStatusAndNomeContainingIgnoreCaseOrderByIdAsc(String status, String nome);

}
