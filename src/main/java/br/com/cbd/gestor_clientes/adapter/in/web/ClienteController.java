package br.com.cbd.gestor_clientes.adapter.in.web;

import br.com.cbd.gestor_clientes.core.domain.model.Cliente;
import br.com.cbd.gestor_clientes.core.port.in.CriarClienteUseCase;
import br.com.cbd.gestor_clientes.mapper.ClienteMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final CriarClienteUseCase criarUseCase;
    private final ClienteMapper mapper;

    public ClienteController(CriarClienteUseCase criarUseCase, ClienteMapper mapper) {
        this.criarUseCase = criarUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> criar(@RequestBody ClienteRequest request) {
        Cliente cliente = mapper.toModel(request);
        Cliente salvo = criarUseCase.execute(cliente);
        return ResponseEntity.ok(mapper.toResponse(salvo));
    }
}