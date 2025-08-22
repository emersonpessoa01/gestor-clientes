package br.com.cbd.gestor_clientes.controller;

import br.com.cbd.gestor_clientes.dto.ClienteDTO;
import br.com.cbd.gestor_clientes.dto.CreateClienteDTO;
import br.com.cbd.gestor_clientes.dto.UpdateClienteDTO;
import br.com.cbd.gestor_clientes.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
@Tag(name = "Gestor de Clientes", description = "API para gerenciamento de clientes (cadastro, consulta, atualização e inativação)")
@CrossOrigin(origins = "*")
public class ClienteController {

    @Autowired
    private ClienteService service;

    @PostMapping
    @Operation(
            summary = "Criar um novo cliente",
            description = "Cria um cliente com nome, email, CPF, telefone (opcional) e status (ATIVO, INATIVO ou PROSPECT). " +
                    "O CPF deve ser válido e único, e o email também deve ser único. O telefone, se informado, deve seguir o formato +XX (XX) XXXXX-XXXX ou similar."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso", content = @Content(schema = @Schema(implementation = ClienteDTO.class))),
            @ApiResponse(responseCode = "400", description = "Erro de validação: CPF inválido, email já existe, telefone inválido ou status inválido", content = @Content)
    })
    public ResponseEntity<ClienteDTO> create(
            @Parameter(description = "Dados do cliente a ser criado", required = true,
                    example = "{\"nome\": \"Maria Silva\", \"email\": \"maria@teste.com\", \"telefone\": \"+55 (11) 98889-7789\", \"cpf\": \"111.444.777-35\", \"status\": \"ATIVO\"}")
            @Valid @RequestBody CreateClienteDTO dto) {
        return new ResponseEntity<>(service.create(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar cliente por ID",
            description = "Retorna os detalhes de um cliente específico pelo seu ID, incluindo nome, email, CPF, telefone, status e datas de criação/atualização."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente encontrado", content = @Content(schema = @Schema(implementation = ClienteDTO.class))),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado", content = @Content)
    })
    public ResponseEntity<ClienteDTO> findById(
            @Parameter(description = "ID do cliente", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    @Operation(
            summary = "Listar clientes",
            description = "Retorna uma lista de clientes com filtros opcionais por status (ATIVO, INATIVO, PROSPECT) e/ou nome (busca parcial, case-insensitive). " +
                    "Se nenhum filtro for fornecido, retorna todos os clientes, ordenados por ID em ordem crescente."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de clientes retornada com sucesso", content = @Content(schema = @Schema(implementation = ClienteDTO.class), array = @ArraySchema)),
            @ApiResponse(responseCode = "400", description = "Status inválido informado no filtro", content = @Content)
    })
    public ResponseEntity<List<ClienteDTO>> findAll(
            @Parameter(description = "Filtro por status (ATIVO, INATIVO, PROSPECT)", example = "ATIVO")
            @RequestParam(required = false) String status,
            @Parameter(description = "Filtro por nome (parcial, case-insensitive)", example = "Maria")
            @RequestParam(required = false) String nome) {
        return ResponseEntity.ok(service.findAll(status, nome));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar cliente existente",
            description = "Atualiza os dados de um cliente (nome, email, telefone, status). O CPF não pode ser alterado, e o email deve permanecer único. " +
                    "O status deve ser ATIVO, INATIVO ou PROSPECT."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso", content = @Content(schema = @Schema(implementation = ClienteDTO.class))),
            @ApiResponse(responseCode = "400", description = "Erro de validação: email duplicado, telefone inválido ou status inválido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado", content = @Content)
    })
    public ResponseEntity<ClienteDTO> update(
            @Parameter(description = "ID do cliente", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Dados atualizados do cliente", required = true,
                    example = "{\"nome\": \"Maria Silva\", \"email\": \"maria.nova@teste.com\", \"telefone\": \"+55 (11) 97777-8888\", \"status\": \"PROSPECT\"}")
            @Valid @RequestBody UpdateClienteDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Inativar cliente (exclusão lógica)",
            description = "Marca um cliente como INATIVO, realizando uma exclusão lógica. Não remove o registro do banco de dados. " +
                    "Se o cliente já estiver inativo, retorna um erro. O status é alterado para INATIVO e a data de atualização é registrada."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cliente inativado com sucesso", content = @Content),
            @ApiResponse(responseCode = "400", description = "Cliente já está inativo", content = @Content),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado", content = @Content)
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID do cliente a ser inativado", required = true, example = "1")
            @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
    @Operation(
            summary = "Validar CPF",
            description = "Verifica se um CPF é válido em formato (não verifica existência real)."
    )
    @GetMapping("/validate-cpf")
    public ResponseEntity<String> validateCpf(
            @Parameter(description = "CPF a validar (ex.: 111.444.777-35)", required = true, example = "111.444.777-35")
            @RequestParam String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("CPF não pode ser vazio");
        }
        boolean valido = service.isCpfValido(cpf);
        return ResponseEntity.ok(valido ? "Válido" : "Inválido");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "Erro de validação ou cliente já inativo", content = @Content),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado", content = @Content)
    })
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        if (ex.getMessage().contains("não encontrado")) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
