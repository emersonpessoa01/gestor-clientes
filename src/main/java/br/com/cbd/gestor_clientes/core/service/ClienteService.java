package br.com.cbd.gestor_clientes.core.service;


import br.com.cbd.gestor_clientes.core.domain.model.Cliente;
import br.com.cbd.gestor_clientes.core.port.in.CriarClienteUseCase;
import br.com.cbd.gestor_clientes.core.port.out.ClienteRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class ClienteService implements CriarClienteUseCase {

    private final ClienteRepositoryPort repository;

    public ClienteService(ClienteRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public Cliente execute(Cliente cliente) {
        return repository.save(cliente);
    }
}
