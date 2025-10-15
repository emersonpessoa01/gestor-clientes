package br.com.cbd.gestor_clientes.adapter.input.controller;

import br.com.cbd.gestor_clientes.adapter.input.mapper.ClienteMapper;
import br.com.cbd.gestor_clientes.adapter.input.request.ClienteRequest;
import br.com.cbd.gestor_clientes.adapter.input.response.ClienteResponse;
import br.com.cbd.gestor_clientes.core.domain.model.Cliente;
import br.com.cbd.gestor_clientes.port.input.ClienteInputPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ClienteControllerTest {

    @Mock
    private ClienteInputPort inputPort;

    @Mock
    private ClienteMapper mapper;

    @InjectMocks
    private ClienteController controller;

    private ClienteRequest request;
    private Cliente cliente;
    private Cliente salvo;
    private ClienteResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Given - Dados de exemplo para os testes
        request = new ClienteRequest();
        request.setNome("João da Silva");
        request.setCpf("12345678900");

        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("João da Silva");
        cliente.setCpf("12345678900");

        salvo = new Cliente();
        salvo.setId(1L);
        salvo.setNome("João da Silva");
        salvo.setCpf("12345678900");

        response = new ClienteResponse();
        response.setId(1L);
        response.setNome("João da Silva");
        response.setCpf("12345678900");
    }

    @Test
    @DisplayName("Deve criar um cliente com sucesso")
    void deveCriarClienteComSucesso() {
        // Given - Um ClienteRequest válido e o comportamento esperado do mapper e inputPort
        when(mapper.toModel(request)).thenReturn(cliente);
        when(inputPort.create(cliente)).thenReturn(salvo);
        when(mapper.toResponse(salvo)).thenReturn(response);

        // When - O metodo criar é chamado
        ResponseEntity<ClienteResponse> resultado = controller.criar(request);

        // Then - O resultado deve conter o status 201 e o corpo esperado
        assertThat(resultado.getStatusCodeValue()).isEqualTo(201);
        assertThat(resultado.getBody()).isNotNull();
        assertThat(resultado.getBody().getNome()).isEqualTo("João da Silva");

        // And - As dependências devem ter sido chamadas corretamente
        verify(mapper, times(1)).toModel(request);
        verify(inputPort, times(1)).create(cliente);
        verify(mapper, times(1)).toResponse(salvo);
    }

}
