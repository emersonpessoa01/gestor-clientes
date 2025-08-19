package br.com.cbd.gestor_clientes.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


@Entity
@Table(name = "cliente")
@Data
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, message = "Nome deve ter no mínimo 3 caracteres")
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    @Column(unique = true)
    private String email;

    private String telefone; // Opcional, validado no service se presente

    @NotBlank(message = "CPF é obrigatório")
    @Column(unique = true)
    private String cpf; // Validado no service

    @NotNull
    @Column(columnDefinition = "VARCHAR(20)")
    private String status = "ATIVO"; // Default ATIVO

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime criadoEm;

    @UpdateTimestamp
    private LocalDateTime atualizadoEm;
}