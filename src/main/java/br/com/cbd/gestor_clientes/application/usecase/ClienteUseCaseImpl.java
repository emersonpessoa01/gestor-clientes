package br.com.cbd.gestor_clientes.application.usecase;

import br.com.cbd.gestor_clientes.application.port.input.ClienteInputPort;
import br.com.cbd.gestor_clientes.application.port.output.ClienteOutputPort;
import br.com.cbd.gestor_clientes.core.model.Cliente;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteUseCaseImpl implements ClienteInputPort {

    private final ClienteOutputPort repository;

    public ClienteUseCaseImpl(ClienteOutputPort repository) {
        this.repository = repository;
    }

    @Override
    public Cliente create(Cliente cliente) {
        validarClienteParaCriacao(cliente);

        if (repository.existsByCpf(cliente.getCpf())) {
            throw new IllegalArgumentException("CPF já cadastrado.");
        }
        if (repository.existsByEmail(cliente.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado.");
        }

        cliente.setCriadoEm(LocalDateTime.now());
        cliente.setAtualizadoEm(LocalDateTime.now());
        return repository.save(cliente);
    }

    @Override
    public Cliente update(Long id, Cliente cliente) {
        Optional<Cliente> clienteExistente = repository.findById(id);
        if (clienteExistente.isEmpty()) {
            throw new IllegalArgumentException("Cliente não encontrado.");
        }

        validarClienteParaAtualizacao(cliente, clienteExistente.get());

        if (!cliente.getCpf().equals(clienteExistente.get().getCpf())) {
            throw new IllegalArgumentException("Não é permitido alterar o CPF.");
        }

        cliente.setId(id);
        cliente.setCriadoEm(clienteExistente.get().getCriadoEm());
        cliente.setAtualizadoEm(LocalDateTime.now());

        return repository.update(cliente);
    }

    @Override
    public void delete(Long id) {
        Optional<Cliente> cliente = repository.findById(id);
        if (cliente.isEmpty()) {
            throw new IllegalArgumentException("Cliente não encontrado.");
        }
        Cliente clienteAtualizado = cliente.get();
        clienteAtualizado.setStatus("INATIVO");
        clienteAtualizado.setAtualizadoEm(LocalDateTime.now());
        repository.update(clienteAtualizado);
    }

    @Override
    public Optional<Cliente> findById(Long id) {
        Optional<Cliente> cliente = repository.findById(id);
        if (cliente.isEmpty()) {
            throw new IllegalArgumentException("Cliente com ID " + id + " não encontrado.");
        }
        return cliente;
    }

    @Override
    public List<Cliente> findAll() {
        return repository.findAll();
    }

    @Override
    public boolean validarCpf(String cpf) {
        if (cpf == null || cpf.isBlank())
            return false;
        cpf = cpf.replaceAll("\\D", "");
        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}"))
            return false;

        int[] digits = cpf.chars().map(c -> c - '0').toArray();
        int sum1 = 0, sum2 = 0;
        for (int i = 0; i < 9; i++) {
            sum1 += digits[i] * (10 - i);
            sum2 += digits[i] * (11 - i);
        }
        sum2 += digits[9] * 2;

        int mod1 = (sum1 * 10) % 11 % 10;
        int mod2 = (sum2 * 10) % 11 % 10;

        return mod1 == digits[9] && mod2 == digits[10];
    }

    private void validarClienteParaCriacao(Cliente cliente) {
        if (cliente.getNome() == null || cliente.getNome().length() < 3) {
            throw new IllegalArgumentException("Nome é obrigatório e deve ter pelo menos 3 caracteres.");
        }
        if (cliente.getEmail() == null || cliente.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email é obrigatório.");
        }
        if (!cliente.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("Email inválido: " + cliente.getEmail());
        }
        if (cliente.getStatus() == null
                || !List.of("ATIVO", "INATIVO", "PROSPECT").contains(cliente.getStatus().toUpperCase())) {
            throw new IllegalArgumentException("Status deve ser ATIVO, INATIVO ou PROSPECT.");
        }
        if (cliente.getCpf() != null && !validarCpf(cliente.getCpf())) {
            throw new IllegalArgumentException("CPF inválido.");
        }
        if (cliente.getTelefone() != null && !validarTelefone(cliente.getTelefone())) {
            throw new IllegalArgumentException("Telefone inválido (formato DDI+DDD).");
        }
    }

    private void validarClienteParaAtualizacao(Cliente cliente, Cliente clienteExistente) {
        validarClienteParaCriacao(cliente); // Reaproveita validação, exceto CPF duplicado
    }

    private boolean validarTelefone(String telefone) {
        if (telefone == null || telefone.isBlank())
            return true; // Opcional
        return telefone.matches("^\\+\\d{2}\\s?\\(?\\d{2}\\)?\\s?\\d{4,5}-?\\d{4}$");
    }
}
