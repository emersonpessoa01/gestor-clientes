package br.com.cbd.gestor_clientes.core.usecase;

import br.com.cbd.gestor_clientes.adapter.input.exception.BusinessException;
import br.com.cbd.gestor_clientes.adapter.input.exception.NotFoundException;
import br.com.cbd.gestor_clientes.adapter.output.repository.ClienteRepository;
import br.com.cbd.gestor_clientes.port.input.ClienteInputPort;
import br.com.cbd.gestor_clientes.port.output.ClienteOutputPort;
import br.com.cbd.gestor_clientes.core.domain.model.Cliente;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class ClienteUseCase implements ClienteInputPort {

    private final ClienteOutputPort clienteOutputPort;
    private final ClienteRepository clienteRepository;

    public ClienteUseCase(ClienteOutputPort repository, ClienteRepository clienteRepository) {
        this.clienteOutputPort = repository;
        this.clienteRepository = clienteRepository;

    }


    public Cliente create(Cliente cliente) {
        validarClienteParaCriacao(cliente);

        if (clienteOutputPort.existsByCpf(cliente.getCpf())) {
            throw new NotFoundException("CPF já cadastrado.");
        }
        if (clienteOutputPort.existsByEmail(cliente.getEmail())) {
            throw new NotFoundException("Email já cadastrado.");
        }

        cliente.setCriadoEm(LocalDateTime.now());
        cliente.setAtualizadoEm(LocalDateTime.now());
        return clienteOutputPort.save(cliente);
    }
    // Expondo publicamente esse metodo utiliário para reutilização e testabilidade
    public boolean existsByCpf(String cpf){
        return clienteOutputPort.existsByCpf(cpf);
    }


    public Cliente update(Long id, Cliente cliente) {
        Optional<Cliente> clienteExistente = clienteOutputPort.findById(id);
        if (clienteExistente.isEmpty()) {
            throw new NotFoundException("Cliente não encontrado.");
        }

        validarClienteParaAtualizacao(cliente, clienteExistente.get());

        if (!cliente.getCpf().equals(clienteExistente.get().getCpf())) {
            throw new BusinessException("Não é permitido alterar o CPF.");
        }

        cliente.setId(id);
        cliente.setCriadoEm(clienteExistente.get().getCriadoEm());
        cliente.setAtualizadoEm(LocalDateTime.now());

        return clienteOutputPort.update(cliente);
    }


    public void delete(Long id) {
        Optional<Cliente> clienteOpt = clienteOutputPort.findById(id);
        if (clienteOpt.isEmpty()) {
            throw new NotFoundException("Cliente não encontrado.");
        }
        Cliente cliente = clienteOpt.get();
        cliente.setStatus("INATIVO");
        cliente.setAtualizadoEm(LocalDateTime.now());
        clienteOutputPort.update(cliente);
        clienteRepository.delete(id);
    }



    public Optional<Cliente> findById(Long id) {
        Optional<Cliente> cliente = clienteOutputPort.findById(id);
        if (cliente.isEmpty()) {
            throw new NotFoundException("Cliente com ID " + id + " não encontrado.");
        }
        return cliente;
    }


    public List<Cliente> findAll() {
        return clienteOutputPort.findAll();
    }


    public boolean validarCpf(String cpf) {
        int sum1 = 0;
        int sum2 = 0;
        if (cpf == null || cpf.isBlank())
            return false;
        cpf = cpf.replaceAll("\\D", "");
        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}"))
            return false;

        int[] digits = cpf.chars().map(c -> c - '0').toArray();
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
            throw new BusinessException("Nome é obrigatório e deve ter pelo menos 3 caracteres.");
        }
        if (cliente.getEmail() == null || cliente.getEmail().trim().isEmpty()) {
            throw new BusinessException("Email é obrigatório.");
        }
        if (!cliente.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new BusinessException("Email inválido: " + cliente.getEmail());
        }
        if (cliente.getStatus() == null
                || !List.of("ATIVO", "INATIVO", "PROSPECT").contains(cliente.getStatus().toUpperCase())) {
            throw new BusinessException("Status deve ser ATIVO, INATIVO ou PROSPECT.");
        }
        if (cliente.getCpf() != null && !validarCpf(cliente.getCpf())) {
            throw new BusinessException("CPF inválido.");
        }
        if (cliente.getTelefone() != null && !validarTelefone(cliente.getTelefone())) {
            throw new BusinessException("Telefone inválido (formato DDI+DDD).");
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

    public int contarClientesAtivos() {
        return clienteRepository.contarClientesAtivos();
    }
    public int contarClientesInativos() {
        return clienteRepository.contarClientesInativos();
    }

    public Optional<Cliente> buscarPorCpf(String cpf) {
        Optional<Cliente> cliente = clienteRepository.findByCpf(cpf);
        if (cliente.isEmpty()) {
            throw new NotFoundException("Cliente com CPF " + cpf + " não encontrado.");
        }
        return cliente;
    }
    public List<Cliente> listarAtivos() {
        return clienteRepository.listarAtivos();
    }
    public List<Cliente> listarInativos() {
        return clienteRepository.listarInativos();
    }
    public void ativarCliente(Long id){
        Optional<Cliente> clienteOpt = clienteRepository.findById(id);
        if(clienteOpt.isEmpty()){
            throw new NotFoundException("Cliente com ID " + id + " não encontrado.");
        }
        clienteRepository.ativarCliente(id);
    }
    public void inativarCliente(Long id) {
        Optional<Cliente> clienteOpt = clienteRepository.findById(id);
        if (clienteOpt.isEmpty()) throw new NotFoundException("Cliente não encontrado.");
        clienteRepository.inativarCliente(id);
    }
}
