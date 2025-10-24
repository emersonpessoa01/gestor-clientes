package br.com.cbd.gestor_clientes.adapter.input.exception;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveRetornarNotFoundParaNotFoundException() throws Exception {
        mockMvc.perform(get("/clientes/999999")) // id inexistente
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Cliente com ID 999999 não encontrado."));
    }

    @Test
    void deveRetornarBadRequestParaBusinessException() throws Exception {
        mockMvc.perform(get("/teste/erro-negocio"))  // endpoint deve existir e lançar BusinessException
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Erro de negócio simulado"));
    }

    @Test
    void deveRetornarBadRequestParaParametroInvalido() throws Exception {
        mockMvc.perform(get("/clientes/abc"))   // passagem de parâmetro inválido para id Long
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Parâmetro inválido: id"));
    }

    @Test
    void deveRetornarInternalServerErrorParaErroGenerico() throws Exception {
        mockMvc.perform(get("/teste/erro-generico"))  // endpoint deve existir e lançar RuntimeException
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Erro interno. Contate o suporte."));
    }
}
