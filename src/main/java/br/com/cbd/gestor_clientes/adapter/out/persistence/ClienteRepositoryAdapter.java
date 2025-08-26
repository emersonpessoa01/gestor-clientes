package br.com.cbd.gestor_clientes.adapter.out.persistence;

import br.com.cbd.gestor_clientes.core.domain.model.Cliente;
import br.com.cbd.gestor_clientes.core.port.out.ClienteRepositoryPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class ClienteRepositoryAdapter implements ClienteRepositoryPort {

    private final JdbcTemplate jdbcTemplate;

    public ClienteRepositoryAdapter(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<Cliente> rowMapper = (rs, rowNum) -> {
        Cliente cliente = new Cliente();
        cliente.setId(rs.getLong("id"));
        cliente.setNome(rs.getString("nome"));
        cliente.setEmail(rs.getString("email"));
        cliente.setTelefone(rs.getString("telefone"));
        cliente.setCpf(rs.getString("cpf"));
        cliente.setStatus(rs.getString("status"));
        cliente.setCriadoEm(rs.getObject("criado_em", LocalDateTime.class));
        cliente.setAtualizadoEm(rs.getObject("atualizado_em", LocalDateTime.class));
        return cliente;
    };

    @Override
    public Cliente save(Cliente cliente) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO cliente (nome, email, telefone, cpf, status) VALUES (?, ?, ?, ?, ?)",
                    new String[]{"id"}
            );
            ps.setString(1, cliente.getNome());
            ps.setString(2, cliente.getEmail());
            ps.setString(3, cliente.getTelefone());
            ps.setString(4, cliente.getCpf());
            ps.setString(5, cliente.getStatus());
            return ps;
        }, keyHolder);
        Long id = keyHolder.getKey().longValue();
        // Consultar o registro para pegar os timestamps
        String selectSql = "SELECT * FROM cliente WHERE id = ?";
        Cliente savedCliente = jdbcTemplate.queryForObject(selectSql, rowMapper, id);
        return savedCliente != null ? savedCliente : cliente;
    }

    @Override
    public Optional<Cliente> findById(Long id) {
        String sql = "SELECT * FROM cliente WHERE id = ?";
        List<Cliente> clientes = jdbcTemplate.query(sql, rowMapper, id);
        return clientes.isEmpty() ? Optional.empty() : Optional.of(clientes.get(0));
    }

    @Override
    public List<Cliente> findAll() {
        String sql = "SELECT * FROM cliente";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public Cliente update(Cliente cliente) {
        String sql = "UPDATE cliente SET nome = ?, email = ?, telefone = ?, cpf = ?, status = ? WHERE id = ?";
        jdbcTemplate.update(sql, cliente.getNome(), cliente.getEmail(), cliente.getTelefone(), cliente.getCpf(), cliente.getStatus(), cliente.getId());
        return cliente;
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM cliente WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}