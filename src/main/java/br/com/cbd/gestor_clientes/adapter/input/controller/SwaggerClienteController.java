package br.com.cbd.gestor_clientes.adapter.input.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.cbd.gestor_clientes.adapter.input.request.ClienteRequest;
import br.com.cbd.gestor_clientes.adapter.input.request.ClienteResponse;

import java.util.List;

@Tag(name = "Gestor de Clientes", description = "API para gerenciamento de clientes (cadastro, consulta, atualização e inativação)")
public interface SwaggerClienteController {

    @Operation(
            summary = "Criar um novo cliente",
            description = "Cria um cliente com nome, email, CPF, telefone (opcional) e status (ATIVO, INATIVO ou PROSPECT)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso", content = @Content(schema = @Schema(implementation = ClienteResponse.class))),
            @ApiResponse(responseCode = "400", description = "Erro de validação: CPF inválido, email já existe, telefone inválido ou status inválido", content = @Content)
    })
    ResponseEntity<ClienteResponse> criar(@RequestBody ClienteRequest request);

    @Operation(
            summary = "Atualizar cliente existente",
            description = "Atualiza os dados de um cliente (nome, email, telefone, status). O CPF não pode ser alterado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso", content = @Content(schema = @Schema(implementation = ClienteResponse.class))),
            @ApiResponse(responseCode = "400", description = "Erro de validação: email duplicado, telefone inválido ou status inválido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado", content = @Content)
    })
    ResponseEntity<ClienteResponse> atualizar(@PathVariable Long id, @RequestBody ClienteRequest request);

    @Operation(
            summary = "Inativar cliente (exclusão lógica)",
            description = "Marca um cliente como INATIVO. Não remove o registro do banco de dados."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cliente inativado com sucesso", content = @Content),
            @ApiResponse(responseCode = "400", description = "Cliente já está inativo", content = @Content),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado", content = @Content)
    })
    ResponseEntity<Void> deletar(@PathVariable Long id);

    @Operation(
            summary = "Buscar cliente por ID",
            description = "Retorna os detalhes de um cliente específico pelo seu ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente encontrado", content = @Content(schema = @Schema(implementation = ClienteResponse.class))),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado", content = @Content)
    })
    ResponseEntity<ClienteResponse> buscar(@PathVariable Long id);

    @Operation(
            summary = "Listar clientes",
            description = "Retorna uma lista de clientes com filtros opcionais por status e/ou nome."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de clientes retornada com sucesso", content = @Content(schema = @Schema(implementation = ClienteResponse.class), array = @ArraySchema)),
            @ApiResponse(responseCode = "400", description = "Status inválido informado no filtro", content = @Content)
    })
    ResponseEntity<List<ClienteResponse>> listar();

    @Operation(
            summary = "Validar CPF",
            description = "Verifica se um CPF é válido em formato."
    )
    ResponseEntity<String> validateCpf(@Parameter(description = "CPF a validar(ex: 111.444.777-35)", required = true, example = "111.444.777-35") @RequestParam String cpf);

    @Operation(
            summary = "Contar clientes ativos",
            description = "Retorna a quantidade de clientes com status ATIVO."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Quantidade de clientes ativos retornada com sucesso",
                    content = @Content(schema = @Schema(implementation = Integer.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno ao tentar contar clientes ativos", content = @Content)
    })
    ResponseEntity<Integer> contarAtivos();

    @Operation(
            summary = "Buscar cliente por CPF",
            description = "Retorna so detalhes de um cliente específico pelo CPF."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente encontrado", content = @Content(schema = @Schema(implementation = ClienteResponse.class))),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado", content = @Content)
    })
    ResponseEntity<ClienteResponse> buscarPorCpf(
            @Parameter(description = "CPF do cliente", required = true, example = "111.444.777-35")
            @PathVariable String cpf);

    @Operation(
            summary = "Listar clientes ativos", description = "Retorna todos os clientes com status ATIVO")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de clientes ativos retornada com sucesso",
                    content = @Content(schema = @Schema(implementation = ClienteResponse.class), array = @ArraySchema)),
            @ApiResponse(responseCode = "500", description = "Erro interno ao buscar clientes ativos", content = @Content)
    })
    ResponseEntity<List<ClienteResponse>> listarAtivos();

    @Operation(summary = "Listar clientes inativos", description = "Retorna todos os clientes com status INATIVO")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de clientes inativos retornada com sucesso",
                    content = @Content(schema = @Schema(implementation = ClienteResponse.class), array = @ArraySchema)),
            @ApiResponse(responseCode = "500", description = "Erro interno ao buscar clientes inativos", content = @Content)
    })
    ResponseEntity<List<ClienteResponse>> listarInativos();

    @Operation(summary = "Ativar cliente", description = "Atualiza o status do cliente para ATIVO")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cliente ativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    ResponseEntity<Void> ativarCliente(@PathVariable Long id);

    @Operation(summary = "Inativar cliente", description = "Atualiza o status do cliente para INATIVO")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cliente inativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    ResponseEntity<Void> inativarCliente(@PathVariable Long id);






}