package br.com.cbd.gestor_clientes.adapter.input.controller;

import br.com.cbd.gestor_clientes.application.port.input.ClienteInputPort;
import br.com.cbd.gestor_clientes.adapter.input.mapper.ClienteMapper;
import br.com.cbd.gestor_clientes.adapter.input.dto.ClienteRequest;
import br.com.cbd.gestor_clientes.adapter.input.dto.ClienteResponse;
import br.com.cbd.gestor_clientes.core.model.Cliente;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController implements SwaggerClienteController {

        private final ClienteInputPort inputPort;
        private final ClienteMapper mapper;

        public ClienteController(ClienteInputPort inputPort, ClienteMapper mapper) {
                this.inputPort = inputPort;
                this.mapper = mapper;
        }

        @PostMapping
        public ResponseEntity<ClienteResponse> criar(@RequestBody ClienteRequest request) {
                Cliente cliente = mapper.toModel(request);
                Cliente salvo = inputPort.create(cliente);
                return ResponseEntity.ok(mapper.toResponse(salvo));
        }

        @PutMapping("/{id}")
        public ResponseEntity<ClienteResponse> atualizar(@PathVariable Long id, @RequestBody ClienteRequest request) {
                Cliente cliente = mapper.toModel(request);
                Cliente atualizado = inputPort.update(id, cliente);
                return ResponseEntity.ok(mapper.toResponse(atualizado));
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deletar(@PathVariable Long id) {
                inputPort.delete(id);
                return ResponseEntity.noContent().build();
        }

        @GetMapping("/{id}")
        public ResponseEntity<ClienteResponse> buscar(@PathVariable Long id) {
                Cliente cliente = inputPort.findById(id).orElseThrow(() -> new IllegalArgumentException(
                        "Cliente com ID " + id + " não encontrado."));
                return ResponseEntity.ok(mapper.toResponse(cliente));
        }

        @GetMapping
        public ResponseEntity<List<ClienteResponse>> listar() {
                List<Cliente> clientes = inputPort.findAll();
                return ResponseEntity.ok(mapper.toResponseList(clientes));
        }

        @GetMapping("/validate-cpf")
        public ResponseEntity<String> validateCpf(@RequestParam String cpf) {
                boolean valido = inputPort.validarCpf(cpf);
                return ResponseEntity.ok(valido ? "CPF válido." : "CPF inválido.");
        }
        @GetMapping("/ativos/count")
        public ResponseEntity<Integer> contarAtivos(){
                int totalAtivos = inputPort.contarClientesAtivos();
                return ResponseEntity.ok(totalAtivos);
        }
}
