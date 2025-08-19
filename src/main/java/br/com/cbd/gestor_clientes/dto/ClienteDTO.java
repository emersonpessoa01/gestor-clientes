package br.com.cbd.gestor_clientes.dto;


import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ClienteDTO {
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String cpf;
    private String status;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}
