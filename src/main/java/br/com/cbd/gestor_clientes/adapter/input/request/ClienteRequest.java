package br.com.cbd.gestor_clientes.adapter.input.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ClienteRequest {

    @NotBlank(message = "O nome é obrigatório.")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres.")
    private String nome;

    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "O e-mail deve ser válido.")
    private String email;

    @NotBlank(message = "O telefone é obrigatório.")
    @Pattern(regexp = "^\\+?\\d{0,2}\\(?\\d{2}\\)?\\s?9?\\d{4}-?\\d{4}$",
            message = "O telefone deve seguir o formato válido, ex: +55(11)99999-9999.")
    private String telefone;

    @NotBlank(message = "O CPF é obrigatório.")
    @Pattern(regexp = "\\d{11}", message = "O CPF deve conter exatamente 11 dígitos numéricos.")
    private String cpf;

    @NotBlank(message = "O status é obrigatório.")
    @Pattern(regexp = "ATIVO|INATIVO", message = "O status deve ser 'ATIVO' ou 'INATIVO'.")
    private String status;

    // Construtores
    public ClienteRequest() {}

    public ClienteRequest(String nome, String email, String telefone, String cpf, String status) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.cpf = cpf;
        this.status = status;
    }

    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
