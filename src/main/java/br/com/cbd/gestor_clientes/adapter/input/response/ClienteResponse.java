package br.com.cbd.gestor_clientes.adapter.input.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Resposta contendo os dados de um cliente")
public class ClienteResponse {
    @Schema(description = "Identificador único do cliente", example = "1")
    private Long id;

    @Schema(description = "Nome completo do cliente", example = "João da Silva")
    private String nome;

    @Schema(description = "Endereço de email do cliente", example = "joao@mail.com")
    private String email;

    @Schema(description = "Número de telefone do cliente", example = "(11) 91234-5678")
    private String telefone;

    @Schema(description = "CPF do cliente", example = "123.456.789-00")
    private String cpf;

    @Schema(description = "Status do cliente (ATIVO, INATIVO ou PROSPECT)", example = "ATIVO")
    private String status;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime criadoEm;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime atualizadoEm;

    // Construtores, getters e setters
    public ClienteResponse() {
    }

    public ClienteResponse(Long id, String nome, String email, String telefone, String cpf, String status,
            LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.cpf = cpf;
        this.status = status;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setAtualizadoEm(LocalDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }
}