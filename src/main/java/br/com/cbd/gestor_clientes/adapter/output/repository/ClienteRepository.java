package br.com.cbd.gestor_clientes.adapter.output.repository;

import br.com.cbd.gestor_clientes.adapter.output.entity.ClienteEntity;
import br.com.cbd.gestor_clientes.core.domain.model.Cliente;
import br.com.cbd.gestor_clientes.port.output.ClienteOutputPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class ClienteRepository implements ClienteOutputPort {

    private final JdbcTemplate jdbcTemplate;

    public ClienteRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<ClienteEntity> rowMapper = (rs, rowNum) -> {
        ClienteEntity entity = new ClienteEntity();
        Integer id = (Integer) rs.getObject("id");
        entity.setId(id != null ? id.longValue() : null);
        entity.setNome(rs.getString("nome"));
        entity.setEmail(rs.getString("email"));
        entity.setTelefone(rs.getString("telefone"));
        entity.setCpf(rs.getString("cpf"));
        entity.setStatus(rs.getString("status"));
        entity.setCriadoEm(rs.getObject("criado_em", LocalDateTime.class));
        entity.setAtualizadoEm(rs.getObject("atualizado_em", LocalDateTime.class));
        return entity;
    };

    @Override
    public Cliente save(Cliente cliente) {
        ClienteEntity entity = toEntity(cliente);

        // Spring vai buscar os metadados da function automaticamente
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withSchemaName("public")
                .withFunctionName("fn_insert_cliente") // função que retorna o id
                .withReturnValue(); // Indica que esperamos um valor de retorno

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("p_nome", entity.getNome())
                .addValue("p_email", entity.getEmail())
                .addValue("p_telefone", entity.getTelefone())
                .addValue("p_cpf", entity.getCpf())
                .addValue("p_status", entity.getStatus());

        // Retorna o resultado diretamente, sem precisar declarar SqlOutParameter
        Integer id = jdbcCall.executeFunction(Integer.class, params);

        entity.setId(id != null ? id.longValue() : null);

        return toDomain(entity);
    }


    @Override
    public Optional<Cliente> findById(Long id) {
        String sql = "SELECT * FROM cliente WHERE id = ?";
        List<ClienteEntity> entities = jdbcTemplate.query(sql, rowMapper, id);
        return entities.isEmpty() ? Optional.empty() : Optional.of(toDomain(entities.get(0)));
    }

    @Override
    public List<Cliente> findAll() {
        String sql = "SELECT * FROM cliente ORDER BY id ASC";
        List<ClienteEntity> entities = jdbcTemplate.query(sql, rowMapper);
        return entities.stream().map(this::toDomain).toList();
    }

    @Override
    public Cliente update(Cliente cliente) {
        ClienteEntity entity = toEntity(cliente);
        String sql = "UPDATE cliente SET nome = ?, email = ?, telefone = ?, cpf = ?, status = ?, atualizado_em= ? WHERE id = ?";
        jdbcTemplate.update(sql, entity.getNome(), entity.getEmail(), entity.getTelefone(), entity.getCpf(),
                entity.getStatus(), entity.getAtualizadoEm(), entity.getId());
        String selectSql = "SELECT * FROM cliente WHERE id = ?";
        ClienteEntity updatedEntity = jdbcTemplate.queryForObject(selectSql, rowMapper, entity.getId());
        return toDomain(updatedEntity);
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM cliente WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existsByCpf(String cpf) {
        String sql = "SELECT COUNT(*) FROM cliente WHERE cpf = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, cpf);
        return count != null && count > 0;
    }

    @Override
    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM cliente WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    @Override
    public Optional<Cliente> findByCpf(String cpf) {
        String sql = "SELECT * FROM fn_get_cliente_por_cpf(?)";
        List<ClienteEntity> entities = jdbcTemplate.query(sql, rowMapper, cpf);
        return entities.isEmpty() ? Optional.empty() : Optional.of(toDomain(entities.get(0)));
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

    private Cliente toDomain(ClienteEntity entity) {
        Cliente cliente = new Cliente();
        cliente.setId(entity.getId());
        cliente.setNome(entity.getNome());
        cliente.setEmail(entity.getEmail());
        cliente.setTelefone(entity.getTelefone());
        cliente.setCpf(entity.getCpf());
        cliente.setStatus(entity.getStatus());
        cliente.setCriadoEm(entity.getCriadoEm());
        cliente.setAtualizadoEm(entity.getAtualizadoEm());
        return cliente;
    }

    public int contarClientesAtivos() {
        String sql = "SELECT fn_count_clientes_ativos()";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
    public List<Cliente> listarAtivos() {
        String sql = "SELECT * FROM fn_get_clientes_ativos()";
        List<ClienteEntity> entities = jdbcTemplate.query(sql, rowMapper);
        return entities.stream().map(this::toDomain).toList();
    }
    public List<Cliente> listarInativos() {
        String sql = "SELECT * FROM fn_get_clientes_inativos()";
        List<ClienteEntity> entities = jdbcTemplate.query(sql, rowMapper);
        return entities.stream().map(this::toDomain).toList();
    }
    public void ativarCliente(Long id) {
        String sql = "CALL pr_ativar_cliente(?)";
        jdbcTemplate.update(sql, id);
    }
    public void inativarCliente(Long id) {
        String sql = "CALL pr_inativar_cliente(?)";
        jdbcTemplate.update(sql, id);
    }

}