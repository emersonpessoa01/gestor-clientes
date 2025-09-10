package br.com.cbd.gestor_clientes.adapter.input.controller;

import br.com.cbd.gestor_clientes.adapter.input.exception.NotFoundException;
import br.com.cbd.gestor_clientes.port.input.ClienteInputPort;
import br.com.cbd.gestor_clientes.adapter.input.mapper.ClienteMapper;
import br.com.cbd.gestor_clientes.adapter.input.request.ClienteRequest;
import br.com.cbd.gestor_clientes.adapter.input.request.ClienteResponse;
import br.com.cbd.gestor_clientes.core.domain.model.Cliente;
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
                Cliente cliente = inputPort.findById(id).orElseThrow(() -> new NotFoundException(
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
        @GetMapping("/cpf/{cpf}")
        public ResponseEntity<ClienteResponse> buscarPorCpf(@PathVariable String cpf) {
                return inputPort.buscarPorCpf(cpf)
                        .map(cliente -> ResponseEntity.ok(mapper.toResponse(cliente)))
                        .orElse(ResponseEntity.notFound().build());
        }
        @GetMapping("/ativos")
        public ResponseEntity<List<ClienteResponse>> listarAtivos() {
                List<Cliente> clientes = inputPort.listarAtivos();
                return ResponseEntity.ok(mapper.toResponseList(clientes));
        }

}
