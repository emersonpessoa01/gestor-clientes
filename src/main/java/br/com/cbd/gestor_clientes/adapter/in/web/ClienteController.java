package br.com.cbd.gestor_clientes.adapter.in.web;

import br.com.cbd.gestor_clientes.core.domain.model.Cliente;
import br.com.cbd.gestor_clientes.core.port.in.CriarClienteUseCase;
import br.com.cbd.gestor_clientes.core.port.in.AtualizarClienteUseCase;
import br.com.cbd.gestor_clientes.core.port.in.DeletarClienteUseCase;
import br.com.cbd.gestor_clientes.core.port.in.BuscarClienteUseCase;
import br.com.cbd.gestor_clientes.core.port.in.ListarClienteUseCase;
import br.com.cbd.gestor_clientes.core.service.ClienteService;
import br.com.cbd.gestor_clientes.mapper.ClienteMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
@Tag(name = "Gestor de Clientes", description = "API para gerenciamento de clientes (cadastro, consulta, atualização e inativação)")
@CrossOrigin(origins = "*")
public class ClienteController {

        private final CriarClienteUseCase criarUseCase;
        private final AtualizarClienteUseCase atualizarUseCase;
        private final DeletarClienteUseCase deletarUseCase;
        private final BuscarClienteUseCase buscarUseCase;
        private final ListarClienteUseCase listarUseCase;
        private final ClienteMapper mapper;
        private final ClienteService clienteService;

        public ClienteController(CriarClienteUseCase criarUseCase, AtualizarClienteUseCase atualizarUseCase,
                        DeletarClienteUseCase deletarUseCase, BuscarClienteUseCase buscarUseCase,
                        ListarClienteUseCase listarUseCase, ClienteMapper mapper, ClienteService clienteService) {
                this.criarUseCase = criarUseCase;
                this.atualizarUseCase = atualizarUseCase;
                this.deletarUseCase = deletarUseCase;
                this.buscarUseCase = buscarUseCase;
                this.listarUseCase = listarUseCase;
                this.mapper = mapper;
                this.clienteService = clienteService;
        }

        @PostMapping
        @Operation(summary = "Criar um novo cliente", description = "Cria um cliente com nome, email, CPF, telefone (opcional) e status (ATIVO, INATIVO ou PROSPECT). "
                        +
                        "O CPF deve ser válido e único, e o email também deve ser único. O telefone, se informado, deve seguir o formato +XX (XX) XXXXX-XXXX ou similar.")
        @ApiResponses({
                        @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso", content = @Content(schema = @Schema(implementation = ClienteResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Erro de validação: CPF inválido, email já existe, telefone inválido ou status inválido", content = @Content)
        })
        public ResponseEntity<ClienteResponse> criar(@RequestBody ClienteRequest request) {
                Cliente cliente = mapper.toModel(request);
                Cliente salvo = criarUseCase.create(cliente);
                return ResponseEntity.ok(mapper.toResponse(salvo));
        }

        @PutMapping("/{id}")
        @Operation(summary = "Atualizar cliente existente", description = "Atualiza os dados de um cliente (nome, email, telefone, status). O CPF não pode ser alterado, e o email deve permanecer único. "
                        +
                        "O status deve ser ATIVO, INATIVO ou PROSPECT.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso", content = @Content(schema = @Schema(implementation = ClienteResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Erro de validação: email duplicado, telefone inválido ou status inválido", content = @Content),
                        @ApiResponse(responseCode = "404", description = "Cliente não encontrado", content = @Content)
        })
        public ResponseEntity<ClienteResponse> atualizar(@PathVariable Long id, @RequestBody ClienteRequest request) {
                Cliente cliente = mapper.toModel(request);
                Cliente atualizado = atualizarUseCase.update(id, cliente);
                return ResponseEntity.ok(mapper.toResponse(atualizado));
        }

        @DeleteMapping("/{id}")
        @Operation(summary = "Inativar cliente (exclusão lógica)", description = "Marca um cliente como INATIVO, realizando uma exclusão lógica. Não remove o registro do banco de dados. "
                        +
                        "Se o cliente já estiver inativo, retorna um erro. O status é alterado para INATIVO e a data de atualização é registrada.")
        @ApiResponses({
                        @ApiResponse(responseCode = "204", description = "Cliente inativado com sucesso", content = @Content),
                        @ApiResponse(responseCode = "400", description = "Cliente já está inativo", content = @Content),
                        @ApiResponse(responseCode = "404", description = "Cliente não encontrado", content = @Content)
        })
        public ResponseEntity<Void> deletar(@PathVariable Long id) {
                deletarUseCase.delete(id);
                return ResponseEntity.noContent().build();
        }

        @GetMapping("/{id}")
        @Operation(summary = "Buscar cliente por ID", description = "Retorna os detalhes de um cliente específico pelo seu ID, incluindo nome, email, CPF, telefone, status e datas de criação/atualização.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Cliente encontrado", content = @Content(schema = @Schema(implementation = ClienteResponse.class))),
                        @ApiResponse(responseCode = "404", description = "Cliente não encontrado", content = @Content)
        })
        public ResponseEntity<ClienteResponse> buscar(@PathVariable Long id) {
                Cliente cliente = buscarUseCase.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Cliente com ID " + id + " não encontrado."));
                return ResponseEntity.ok(mapper.toResponse(cliente));
        }

        @GetMapping
        @Operation(summary = "Listar clientes", description = "Retorna uma lista de clientes com filtros opcionais por status (ATIVO, INATIVO, PROSPECT) e/ou nome (busca parcial, case-insensitive). "
                        +
                        "Se nenhum filtro for fornecido, retorna todos os clientes, ordenados por ID em ordem crescente.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Lista de clientes retornada com sucesso", content = @Content(schema = @Schema(implementation = ClienteResponse.class), array = @ArraySchema)),
                        @ApiResponse(responseCode = "400", description = "Status inválido informado no filtro", content = @Content)
        })
        public ResponseEntity<List<ClienteResponse>> listar() {
                List<Cliente> clientes = listarUseCase.findAll();
                List<ClienteResponse> responseList = mapper.toResponseList(clientes);
                return ResponseEntity.ok(responseList);
        }

        @Operation(summary = "Validar CPF", description = "Verifica se um CPF é válido em formato (não verifica existência real).")
        @GetMapping("/validate-cpf")
        public ResponseEntity<String> validateCpf(
                        @Parameter(description = "CPF a validar (ex.: 111.444.777-35)", required = true, example = "111.444.777-35") @RequestParam String cpf) {
                if (cpf == null || cpf.isEmpty()) {
                        return ResponseEntity.badRequest().body("CPF não pode ser vazio.");
                }
                boolean valido = clienteService.validarCpf(cpf);
                return ResponseEntity.ok(valido ? "CPF válido." : "CPF inválido.");
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