package br.com.cbd.gestor_clientes.adapter.input.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ClienteControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Deve retornar status 200 ao listar clientes")
    void deveListarClientes() throws Exception {
        mockMvc.perform(get("/clientes")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


}
