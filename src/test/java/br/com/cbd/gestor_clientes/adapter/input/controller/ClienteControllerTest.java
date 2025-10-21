package br.com.cbd.gestor_clientes.adapter.input.controller;

import br.com.cbd.gestor_clientes.adapter.input.mapper.ClienteMapper;
import br.com.cbd.gestor_clientes.adapter.input.response.ClienteResponse;
import br.com.cbd.gestor_clientes.core.domain.model.Cliente;
import br.com.cbd.gestor_clientes.port.input.ClienteInputPort;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de integração da camada de entrada (controller) para ClienteController.
 * Utiliza MockMvc para simular requisições HTTP reais.
 * Segue o padrão Given-When-Then.
 */
@WebMvcTest(controllers = ClienteController.class)
@ImportAutoConfiguration(exclude = {
        org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class
})
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClienteInputPort useCase;

    @MockBean
    private ClienteMapper clienteMapper;

    private Cliente cliente;

    private ClienteResponse clienteResponse;

    @BeforeEach
    void setUp() {
        cliente = new Cliente(
                1L,
                "Sabine Wren",
                "sabinewren@gmail.com",
                "+55(11)99999-9999",
                "11144477735",
                "ATIVO",
                null,
                null
        );

        clienteResponse = new ClienteResponse(
                1L,
                "Sabine Wren",
                "sabinewren@gmail.com",
                "+55(11)99999-9999",
                "11144477735",
                "ATIVO",
                null,
                null
        );
    }

    @Test
    @DisplayName("Deve retornar status 200 e lista de clientes ao listar todos")
    void deveListarClientes() throws Exception {
        // Given: Simula a camada use case retornando entidades
        when(useCase.findAll()).thenReturn(Arrays.asList(cliente));

        // Simula o mapper convertendo entidades em respostas
        when(clienteMapper.toResponse(any(Cliente.class)))
                .thenReturn(clienteResponse);
        when(clienteMapper.toResponseList(anyList()))
                .thenReturn(Arrays.asList(clienteResponse));

        // Executa GET /clientes e valida status e conteúdo JSON
        mockMvc.perform(get("/clientes")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Sabine Wren"));

        // Then
        verify(useCase, times(1)).findAll();
        verify(clienteMapper, times(1)).toResponseList(anyList());
    }
}
