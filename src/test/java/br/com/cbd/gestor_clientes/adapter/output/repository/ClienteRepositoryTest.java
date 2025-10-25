package br.com.cbd.gestor_clientes.adapter.output.repository;

import br.com.cbd.gestor_clientes.adapter.output.entity.ClienteEntity;
import br.com.cbd.gestor_clientes.core.domain.model.Cliente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteRepositoryTest {
    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private ClienteRepository clienteRepository;

    @Test
    @DisplayName("Deve salvar cliente com sucesso usando SimpleJdbcCall")
    void deveSalvarClienteComSucesso() {
        Cliente cliente = new Cliente();
        cliente.setNome("João Silva");
        cliente.setEmail("joao@email.com");
        cliente.setTelefone("99999-9999");
        cliente.setCpf("12345678900");
        cliente.setStatus("ATIVO");
        cliente.setCriadoEm(LocalDateTime.now());
        cliente.setAtualizadoEm(LocalDateTime.now());

        SimpleJdbcCall jdbcCallMock = mock(SimpleJdbcCall.class);
        when(jdbcCallMock.executeFunction(eq(Long.class), any(MapSqlParameterSource.class)))
                .thenReturn(1L);

        ClienteRepository spyRepository = spy(clienteRepository);
        doReturn(jdbcCallMock).when(spyRepository).createSimpleJdbcCall();

        Cliente salvo = spyRepository.save(cliente);

        assertNotNull(salvo.getId());
        assertEquals("João Silva", salvo.getNome());
        verify(jdbcCallMock, times(1))
                .executeFunction(eq(Long.class), any(MapSqlParameterSource.class));
    }

    @Test
    @DisplayName("Deve atualizar cliente com sucesso")
    void deveAtualizarClienteComSucesso() {
        Cliente clienteAtualizado = new Cliente();
        clienteAtualizado.setId(1L);
        clienteAtualizado.setNome("Nome Atualizado");
        clienteAtualizado.setEmail("novo@email.com");
        clienteAtualizado.setTelefone("98888-8888");
        clienteAtualizado.setCpf("12345678900");
        clienteAtualizado.setStatus("ATIVO");
        clienteAtualizado.setAtualizadoEm(LocalDateTime.now());

        String sql = "UPDATE cliente SET nome = ?, email = ?, telefone = ?, cpf = ?, status = ?, atualizado_em= ? WHERE id = ?";

        when(jdbcTemplate.update(eq(sql), any(Object[].class))).thenReturn(1);
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), eq(1L)))
                .thenReturn(toEntity(clienteAtualizado));

        Cliente resultado = clienteRepository.update(clienteAtualizado);

        assertNotNull(resultado);
        assertEquals("Nome Atualizado", resultado.getNome());
        verify(jdbcTemplate, times(1)).update(eq(sql), any(Object[].class));
        verify(jdbcTemplate, times(1)).queryForObject(anyString(), any(RowMapper.class), eq(1L));
    }

    private ClienteEntity toEntity(Cliente cliente) {
        ClienteEntity entity = new ClienteEntity();
        entity.setId(cliente.getId());
        entity.setNome(cliente.getNome());
        entity.setEmail(cliente.getEmail());
        entity.setTelefone(cliente.getTelefone());
        entity.setCpf(cliente.getCpf());
        entity.setStatus(cliente.getStatus());
        entity.setCriadoEm(cliente.getCriadoEm());
        entity.setAtualizadoEm(cliente.getAtualizadoEm());
        return entity;
    }
}
