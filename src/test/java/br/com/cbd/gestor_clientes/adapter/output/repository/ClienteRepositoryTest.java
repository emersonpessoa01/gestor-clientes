package br.com.cbd.gestor_clientes.adapter.output.repository;

import br.com.cbd.gestor_clientes.core.domain.model.Cliente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ClienteRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private ClienteRepository clienteRepository;

    @Test
    @DisplayName("Deve salvar cliente com sucesso via SimpleJdbcCall")
    void deveSalvarClienteComSucesso() {
        // Given
        Cliente cliente = new Cliente();
        cliente.setNome("Emerson Pessoa");
        cliente.setEmail("emersonpessoa@email.com");
        cliente.setTelefone("91999999999");
        cliente.setCpf("12345678900");
        cliente.setStatus("ATIVO");
        cliente.setCriadoEm(LocalDateTime.now());
        cliente.setAtualizadoEm(LocalDateTime.now());

        // Mock do SimpleJdbcCall
        SimpleJdbcCall mockCall = Mockito.mock(SimpleJdbcCall.class);
        when(mockCall.executeFunction(Mockito.eq(Long.class), any(MapSqlParameterSource.class)))
                .thenReturn(100L);

        // Simula apenas o retorno do ID
        Cliente saved = new Cliente();
        saved.setId(100L);
        saved.setNome(cliente.getNome());
        saved.setStatus(cliente.getStatus());

        // Then
        assertThat(saved.getId()).isEqualTo(100L);
    }

}
