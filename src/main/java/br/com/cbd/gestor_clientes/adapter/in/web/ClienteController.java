package br.com.cbd.gestor_clientes.adapter.in.web;

import br.com.cbd.gestor_clientes.core.domain.model.Cliente;
import br.com.cbd.gestor_clientes.core.port.in.CriarClienteUseCase;
import br.com.cbd.gestor_clientes.core.port.in.AtualizarClienteUseCase;
import br.com.cbd.gestor_clientes.core.port.in.DeletarClienteUseCase;
import br.com.cbd.gestor_clientes.core.port.in.BuscarClienteUseCase;
import br.com.cbd.gestor_clientes.core.port.in.ListarClienteUseCase;
import br.com.cbd.gestor_clientes.mapper.ClienteMapper;
import jakarta.websocket.ClientEndpoint;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final CriarClienteUseCase criarUseCase;
    private final AtualizarClienteUseCase atualizarUseCase;
    private final DeletarClienteUseCase deletarUseCase;
    private final BuscarClienteUseCase buscarUseCase;
    private final ListarClienteUseCase listarUseCase;
    private final ClienteMapper mapper;

    public ClienteController(CriarClienteUseCase criarUseCase, AtualizarClienteUseCase atualizarUseCase,
                             DeletarClienteUseCase deletarUseCase, BuscarClienteUseCase buscarUseCase,
                             ListarClienteUseCase listarUseCase, ClienteMapper mapper) {
        this.criarUseCase = criarUseCase;
        this.atualizarUseCase = atualizarUseCase;
        this.deletarUseCase = deletarUseCase;
        this.buscarUseCase = buscarUseCase;
        this.listarUseCase = listarUseCase;
        this.mapper = mapper;
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
        Cliente cliente =  buscarUseCase.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Cliente com ID " + id + " não encontrado."));
        return ResponseEntity.ok(mapper.toResponse(cliente));
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponse>> listar() {
        List<Cliente> clientes = listarUseCase.findAll();
        List<ClienteResponse> responseList = mapper.toResponseList(clientes); // Assume que ClienteMapper tem toResponseList
        return ResponseEntity.ok(responseList);
    }
}