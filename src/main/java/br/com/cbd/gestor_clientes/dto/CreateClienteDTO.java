package br.com.cbd.gestor_clientes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class CreateClienteDTO {
    @NotBlank(message = "Nome é obrigatório")
    @Schema(description = "Nome do cliente", example = "João Silva")
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    @Schema(description = "Email do cliente", example = "meuemail@gmail.com")
    private String email;

    @Schema(description = "Telefone do usuário", example = "+55 (xx) xxxxx-xxxx")
    private String telefone;

    @NotBlank(message = "CPF é obrigatório")
    @Schema(description = "CPF do cliente", example = "999.999.999-00")
    private String cpf;

    @NotNull
    @Schema(description = "Status do cliente", example = "ATIVO | INATIVO | PROSPECT")
    private String status = "ATIVO";
}