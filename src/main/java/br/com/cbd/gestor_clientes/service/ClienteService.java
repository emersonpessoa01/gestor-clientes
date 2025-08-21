package br.com.cbd.gestor_clientes.service;

import br.com.cbd.gestor_clientes.dto.ClienteDTO;
import br.com.cbd.gestor_clientes.dto.CreateClienteDTO;
import br.com.cbd.gestor_clientes.dto.UpdateClienteDTO;
import br.com.cbd.gestor_clientes.entity.Cliente;
import br.com.cbd.gestor_clientes.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repository;

    // Utilitário para validar CPF (simples algoritmo de verificação)
    private boolean isCpfValido(String cpf) {
        cpf = cpf.replaceAll("\\D", ""); // Remove não dígitos
        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) return false;

        try {
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
        } catch (Exception e) {
            return false;
        }
    }

    // Validar telefone se presente (exemplo: +55 (DD) XXXXX-XXXX)
    private boolean isTelefoneValido(String telefone) {
        if (telefone == null || telefone.isBlank()) return true; // Opcional
        return telefone.matches("^\\+\\d{2}\\s?\\(?\\d{2}\\)?\\s? \\d{4,5}-?\\d{4}$"); // Formato DDI+DDD+numero
    }

    public ClienteDTO create(CreateClienteDTO dto) {
        //Validação explícita do nome
        if(dto.getNome() == null || dto.getNome().length() < 3){
            throw new IllegalArgumentException("O nome deve ter pelo menos 3 caracteres");
        }
        if (!isCpfValido(dto.getCpf())) {
            throw new IllegalArgumentException("CPF inválido");
        }
        if (repository.findByCpfOrderByIdAsc(dto.getCpf()).isPresent()) {
            throw new IllegalArgumentException("CPF já cadastrado");
        }
        if (repository.findByEmailOrderByIdAsc(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado");
        }
        if (!isTelefoneValido(dto.getTelefone())) {
            throw new IllegalArgumentException("Telefone inválido");
        }
        if (!List.of("ATIVO", "INATIVO", "PROSPECT").contains(dto.getStatus().toUpperCase())) {
            throw new IllegalArgumentException("Status inválido");
        }

        Cliente cliente = new Cliente();
        cliente.setNome(dto.getNome());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefone(dto.getTelefone());
        cliente.setCpf(dto.getCpf());
        cliente.setStatus(dto.getStatus().toUpperCase());

        Cliente saved = repository.save(cliente);
        return mapToDTO(saved);
    }

    public ClienteDTO findById(Long id) {
        return repository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
    }

    public List<ClienteDTO> findAll(String status, String nome) {
        List<Cliente> clientes;
        if (status != null && nome != null) {
            clientes = repository.findByStatusAndNomeContainingIgnoreCaseOrderByIdAsc(status.toUpperCase(),nome);
        } else if (status != null) {
            clientes = repository.findByStatusOrderByIdAsc(status.toUpperCase());
        } else if (nome != null) {
            clientes = repository.findByNomeContainingIgnoreCaseOrderByIdAsc(nome);
        } else {
            clientes = repository.findAllByOrderByIdAsc();
        }
        return clientes.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public ClienteDTO update(Long id, UpdateClienteDTO dto) {
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

        // Proibir atualização de CPF
        // Email pode ser atualizado, mas checar unicidade se alterado
        if (!cliente.getEmail().equals(dto.getEmail())) {
            if (repository.findByEmailOrderByIdAsc(dto.getEmail()).isPresent()) {
                throw new IllegalArgumentException("Email já cadastrado");
            }
        }
        if (!isTelefoneValido(dto.getTelefone())) {
            throw new IllegalArgumentException("Telefone inválido");
        }
        if (!List.of("ATIVO", "INATIVO", "PROSPECT").contains(dto.getStatus().toUpperCase())) {
            throw new IllegalArgumentException("Status inválido");
        }

        cliente.setNome(dto.getNome());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefone(dto.getTelefone());
        cliente.setStatus(dto.getStatus().toUpperCase());
        // atualizadoEm é automático via @UpdateTimestamp

        Cliente updated = repository.save(cliente);
        return mapToDTO(updated);
    }

    public void delete(Long id) {
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
        if("INATIVO".equalsIgnoreCase(cliente.getStatus())){
            throw new IllegalArgumentException("Cliente já está inativo");
        }
        cliente.setStatus("INATIVO");
        repository.save(cliente);
    }

    private ClienteDTO mapToDTO(Cliente cliente) {
        ClienteDTO dto = new ClienteDTO();
        dto.setId(cliente.getId());
        dto.setNome(cliente.getNome());
        dto.setEmail(cliente.getEmail());
        dto.setTelefone(cliente.getTelefone());
        dto.setCpf(cliente.getCpf());
        dto.setStatus(cliente.getStatus());
        dto.setCriadoEm(cliente.getCriadoEm());
        dto.setAtualizadoEm(cliente.getAtualizadoEm());
        return dto;
    }
}