package br.com.cbd.gestor_clientes.adapter.output.repository;

import br.com.cbd.gestor_clientes.core.domain.model.Cliente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteRepositoryTest {
    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private ClienteRepository clienteRepository;

    @Test
    @DisplayName("Deve salvar cliente com sucesso usando SimpleJdbcCall")
    void deveSalvarClienteComSucesso() {
        // Given (Dado)
        Cliente cliente = new Cliente();
        cliente.setNome("João Silva");
        cliente.setEmail("joao@email.com");
        cliente.setTelefone("99999-9999");
        cliente.setCpf("12345678900");
        cliente.setStatus("ATIVO");
        cliente.setCriadoEm(LocalDateTime.now());
        cliente.setAtualizadoEm(LocalDateTime.now());

        // When (Quando)
        // Mocka a execução da função no banco
        SimpleJdbcCall jdbcCallMock = mock(SimpleJdbcCall.class);
        when(jdbcCallMock.executeFunction(eq(Long.class), any(MapSqlParameterSource.class)))
                .thenReturn(1L);

        // Espiona a criação do SimpleJdbcCall dentro do método
        ClienteRepository spyRepository = spy(clienteRepository);
        doReturn(jdbcCallMock).when(spyRepository).createSimpleJdbcCall();

        Cliente salvo = spyRepository.save(cliente);

        // Then (Então)
        assertNotNull(salvo.getId());
        assertEquals("João Silva", salvo.getNome());
        verify(jdbcCallMock, times(1))
                .executeFunction(eq(Long.class), any(MapSqlParameterSource.class));
    }
}
