package br.com.cbd.gestor_clientes.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jdk.jfr.Description;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ClienteDTO {
    private Long id;

    @Schema(description = "Nome do cliente", example = "João Silva")
    private String nome;

    @Schema(description = "Email do cliente", example = "meuemail@mail.com")
    private String email;

    @Schema(description = "Telefone do cliente", example = "+55 (xx) xxxxx-xxxx")
    private String telefone;

    @Schema(description = "CPF do cliente", example = "xxx.xxx.xxx-xx")
    private String cpf;

    @Schema(description = "Status do cliente", example = "ATIVO | INATIVO | PROSPECT")
    private String status;

    @Schema(description = "Data da criação", example = "2025-01-15")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime criadoEm;

    @Schema(description = "Data da última atualização", example = "2025-04-10")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime atualizadoEm;
}
