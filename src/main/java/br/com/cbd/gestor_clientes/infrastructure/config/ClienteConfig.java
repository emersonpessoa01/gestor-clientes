package br.com.cbd.gestor_clientes.infrastructure.config;

import br.com.cbd.gestor_clientes.adapter.output.repository.ClienteRepository;
import br.com.cbd.gestor_clientes.core.usecase.ClienteUseCase;
import br.com.cbd.gestor_clientes.port.input.ClienteInputPort;
import br.com.cbd.gestor_clientes.port.output.ClienteOutputPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClienteConfig {
    @Bean
    public ClienteInputPort clienteInputPort(
            ClienteOutputPort clienteOutputPort, ClienteRepository clienteRepository) {
        return new ClienteUseCase(clienteOutputPort, clienteRepository);
    }
}
