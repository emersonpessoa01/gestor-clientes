package br.com.cbd.gestor_clientes.adapter.input.controller;

import br.com.cbd.gestor_clientes.adapter.input.request.ClienteRequest;
import br.com.cbd.gestor_clientes.adapter.input.request.ClienteResponse;
import br.com.cbd.gestor_clientes.adapter.input.mapper.ClienteMapper;
import br.com.cbd.gestor_clientes.core.model.Cliente;
import br.com.cbd.gestor_clientes.application.port.input.CriarClienteUseCase;
import br.com.cbd.gestor_clientes.application.port.input.AtualizarClienteUseCase;
import br.com.cbd.gestor_clientes.application.port.input.DeletarClienteUseCase;
import br.com.cbd.gestor_clientes.application.port.input.BuscarClienteUseCase;
import br.com.cbd.gestor_clientes.application.port.input.ListarClienteUseCase;


import br.com.cbd.gestor_clientes.usecase.ClienteUseCaseImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController implements SwaggerClienteController {

        private final CriarClienteUseCase criarUseCase;
        private final AtualizarClienteUseCase atualizarUseCase;
        private final DeletarClienteUseCase deletarUseCase;
        private final BuscarClienteUseCase buscarUseCase;
        private final ListarClienteUseCase listarUseCase;
        private final ClienteMapper mapper;
        private final ClienteUseCaseImpl clienteUseCase;

        public ClienteController(CriarClienteUseCase criarUseCase, AtualizarClienteUseCase atualizarUseCase,
                                 DeletarClienteUseCase deletarUseCase, BuscarClienteUseCase buscarUseCase,
                                 ListarClienteUseCase listarUseCase, ClienteMapper mapper, ClienteUseCaseImpl clienteUseCase) {
                this.criarUseCase = criarUseCase;
                this.atualizarUseCase = atualizarUseCase;
                this.deletarUseCase = deletarUseCase;
                this.buscarUseCase = buscarUseCase;
                this.listarUseCase = listarUseCase;
                this.mapper = mapper;
                this.clienteUseCase = clienteUseCase;

        }

        @PostMapping
        public ResponseEntity<ClienteResponse> criar(@RequestBody ClienteRequest request) {
                Cliente cliente = mapper.toModel(request);
                Cliente salvo = criarUseCase.create(cliente);
                return ResponseEntity.ok(mapper.toResponse(salvo));
        }

        @PutMapping("/{id}")
        public ResponseEntity<ClienteResponse> atualizar(@PathVariable Long id, @RequestBody ClienteRequest request) {
                Cliente cliente = mapper.toModel(request);
                Cliente atualizado = atualizarUseCase.update(id, cliente);
                return ResponseEntity.ok(mapper.toResponse(atualizado));
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deletar(@PathVariable Long id) {
                deletarUseCase.delete(id);
                return ResponseEntity.noContent().build();
        }

        @GetMapping("/{id}")
        public ResponseEntity<ClienteResponse> buscar(@PathVariable Long id) {
                Cliente cliente = buscarUseCase.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Cliente com ID " + id + " não encontrado."));
                return ResponseEntity.ok(mapper.toResponse(cliente));
        }

        @GetMapping
        public ResponseEntity<List<ClienteResponse>> listar() {
                List<Cliente> clientes = listarUseCase.findAll();
                List<ClienteResponse> responseList = mapper.toResponseList(clientes);
                return ResponseEntity.ok(responseList);
        }

        @GetMapping("/validate-cpf")
        public ResponseEntity<String> validateCpf(@RequestParam String cpf) {
                if (cpf == null || cpf.isEmpty()) {
                        return ResponseEntity.badRequest().body("CPF não pode ser vazio.");
                }
                boolean valido = clienteUseCase.validarCpf(cpf);
                return ResponseEntity.ok(valido ? "CPF válido." : "CPF inválido.");
        }
}