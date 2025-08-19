package br.com.cbd.gestor_clientes.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateClienteDTO {
    @NotBlank(message = "Nome é obrigatório")
    @Size(min=3,message = "Nome dever ter no mínimo 3 caracteres")
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

    private String telefone;
    @NotNull
    private String status;

}
