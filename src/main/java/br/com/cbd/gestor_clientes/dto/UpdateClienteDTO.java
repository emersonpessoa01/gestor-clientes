package br.com.cbd.gestor_clientes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateClienteDTO {
    @NotBlank(message = "Nome é obrigatório")
    @Size(min=3,message = "Nome dever ter no mínimo 3 caracteres")
    @Schema(description = "Nome do cliente", example = "João Silva")
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    @Schema(description = "Email do cliente", example = "meuemail@gmail.com")
    private String email;

    @Schema(description = "Telefone do cliente", example = "+55 (xx) xxxxx-xxxx")
    private String telefone;

    @NotNull
    @Schema(description = "Status do cliente", example = "ATIVO | INATIVO | PROSPECT")
    private String status;

}
