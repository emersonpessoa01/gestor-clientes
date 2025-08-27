package br.com.cbd.gestor_clientes.adapter.in.web;

public class ClienteRequest {
    private String nome;
    private String email;
    private String telefone;
    private String cpf;
    private String status;

    // Construtores, getters e setters
    public ClienteRequest() {
    }

    public ClienteRequest(String nome, String email, String telefone, String cpf, String status) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.cpf = cpf;
        this.status = status;
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
}