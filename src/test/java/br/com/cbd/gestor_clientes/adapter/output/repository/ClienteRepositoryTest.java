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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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
        Cliente cliente = criarClienteExemplo();
        SimpleJdbcCall jdbcCallMock = mock(SimpleJdbcCall.class);
        when(jdbcCallMock.executeFunction(eq(Long.class), any(MapSqlParameterSource.class)))
                .thenReturn(1L);
        ClienteRepository spyRepository = spy(clienteRepository);
        doReturn(jdbcCallMock).when(spyRepository).createSimpleJdbcCall();
        Cliente salvo = spyRepository.save(cliente);

        assertNotNull(salvo.getId());
        assertEquals("João Silva", salvo.getNome());
        verify(jdbcCallMock, times(1)).executeFunction(eq(Long.class), any(MapSqlParameterSource.class));
    }

    @Test
    @DisplayName("Deve atualizar cliente com sucesso")
    void deveAtualizarClienteComSucesso() {
        Cliente clienteAtualizado = criarClienteExemplo();
        clienteAtualizado.setId(1L);
        clienteAtualizado.setNome("Nome Atualizado");
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

    @Test
    @DisplayName("Deve deletar cliente com sucesso")
    void deveDeletarClienteComSucesso() {
        doReturn(1).when(jdbcTemplate).update(anyString(), anyLong());
        clienteRepository.delete(1L);
        verify(jdbcTemplate, times(1)).update(anyString(), eq(1L));
    }

    @Test
    @DisplayName("Deve retornar true se CPF existe")
    void deveVerificarExistsByCpf() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Long.class), anyString()))
                .thenReturn(1L);
        boolean existe = clienteRepository.existsByCpf("11111111111");
        assertTrue(existe);
    }

    @Test
    @DisplayName("Deve retornar true se Email existe")
    void deveVerificarExistsByEmail() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Long.class), anyString()))
                .thenReturn(1L);
        boolean existe = clienteRepository.existsByEmail("email@teste.com");
        assertTrue(existe);
    }

    @Test
    @DisplayName("Deve buscar cliente por CPF com sucesso")
    void deveBuscarPorCpf() {
        ClienteEntity entity = criarClienteEntity();
        List<ClienteEntity> entities = List.of(entity);
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), anyString()))
                .thenReturn(entities);
        var resultado = clienteRepository.findByCpf("12345678900");
        assertTrue(resultado.isPresent());
        assertEquals("João Silva", resultado.get().getNome());
    }

    @Test
    @DisplayName("Deve contar clientes ativos com sucesso")
    void deveContarClientesAtivos() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Long.class)))
                .thenReturn(5L);
        int count = clienteRepository.contarClientesAtivos();
        assertEquals(5, count);
    }

    @Test
    @DisplayName("Deve contar clientes inativos com sucesso")
    void deveContarClientesInativos() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Long.class)))
                .thenReturn(3L);
        int count = clienteRepository.contarClientesInativos();
        assertEquals(3, count);
    }

    @Test
    @DisplayName("Deve listar clientes ativos com sucesso")
    void deveListarAtivos() {
        ClienteEntity entity = criarClienteEntity();
        List<ClienteEntity> entities = List.of(entity);
        when(jdbcTemplate.query(anyString(), any(RowMapper.class)))
                .thenReturn(entities);
        List<Cliente> clientes = clienteRepository.listarAtivos();
        assertFalse(clientes.isEmpty());
        assertEquals("João Silva", clientes.get(0).getNome());
    }

    @Test
    @DisplayName("Deve listar clientes inativos com sucesso")
    void deveListarInativos() {
        ClienteEntity entity = criarClienteEntity();
        List<ClienteEntity> entities = List.of(entity);
        when(jdbcTemplate.query(anyString(), any(RowMapper.class)))
                .thenReturn(entities);
        List<Cliente> clientes = clienteRepository.listarInativos();
        assertFalse(clientes.isEmpty());
        assertEquals("João Silva", clientes.get(0).getNome());
    }

    @Test
    @DisplayName("Deve ativar cliente com sucesso")
    void deveAtivarCliente() {
        when(jdbcTemplate.update(anyString(), anyLong())).thenReturn(1);
        clienteRepository.ativarCliente(1L);
        verify(jdbcTemplate, times(1)).update(anyString(), eq(1L));
    }

    @Test
    @DisplayName("Deve inativar cliente com sucesso")
    void deveInativarCliente() {
        when(jdbcTemplate.update(anyString(), anyLong())).thenReturn(1);
        clienteRepository.inativarCliente(1L);
        verify(jdbcTemplate, times(1)).update(anyString(), eq(1L));
    }


    private Cliente criarClienteExemplo() {
        Cliente cliente = new Cliente();
        cliente.setNome("João Silva");
        cliente.setEmail("joao@email.com");
        cliente.setTelefone("99999-9999");
        cliente.setCpf("12345678900");
        cliente.setStatus("ATIVO");
        cliente.setCriadoEm(LocalDateTime.now());
        cliente.setAtualizadoEm(LocalDateTime.now());
        return cliente;
    }

    private ClienteEntity criarClienteEntity() {
        ClienteEntity entity = new ClienteEntity();
        entity.setId(1L);
        entity.setNome("João Silva");
        entity.setEmail("joao@email.com");
        entity.setTelefone("99999-9999");
        entity.setCpf("12345678900");
        entity.setStatus("ATIVO");
        entity.setCriadoEm(LocalDateTime.now());
        entity.setAtualizadoEm(LocalDateTime.now());
        return entity;
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
