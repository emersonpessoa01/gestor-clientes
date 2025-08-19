package br.com.cbd.gestor_clientes.controller;

import br.com.cbd.gestor_clientes.dto.ClienteDTO;
import br.com.cbd.gestor_clientes.dto.CreateClienteDTO;
import br.com.cbd.gestor_clientes.dto.UpdateClienteDTO;
import br.com.cbd.gestor_clientes.service.ClienteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Table(name = "/clientes")
@Api(tags = "Gestão de clientes",description = "Endpoints para gerenciarmento de clientes")
public class ClienteController {

    @Autowired
    private ClienteService service;

    @PostMapping
    @ApiOperation(value="Criar um novo cliente", response = ClienteDTO.class)
    public ResponseEntity<ClienteDTO> create(@Valid @RequestBody CreateClienteDTO dto){
        return new ResponseEntity<>(service.create(dto), HttpStatus.CREATED);
    }

    @GetMapping
    @ApiOperation(value="Listar clientes com filtros opcionais", response = List.class )
    public ResponseEntity<List<ClienteDTO>> findAll(
            @ApiParam(value = "Filtro por status (ATIVO, INATIVO, PROSPECT)") @RequestParam(required = false) String status,
            @ApiParam(value = "Filtro por nome (parcial)") @RequestParam(required = false) String nome) {
        return ResponseEntity.ok(service.findAll(status, nome));
    }
    @PutMapping("/{id}")
    @ApiOperation(value="Atualizar cliene existente", response = ClienteDTO.class)
    public ResponseEntity<ClienteDTO>update(@ApiParam(value = "ID do cliente") @PathVariable Long id, @Valid @RequestBody UpdateClienteDTO dto){
        return ResponseEntity.ok(service.update(id,dto));
    }

    @DeleteMapping
    @ApiOperation(value = "Inativar cliente (exclusão lógica)")
    public ResponseEntity<Void> delete(@ApiParam(value="ID do cliente") @PathVariable Long id){
       service.delete(id);
       return ResponseEntity.noContent().build();
    }

}
