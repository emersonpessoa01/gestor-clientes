package br.com.cbd.gestor_clientes.mapper;

import br.com.cbd.gestor_clientes.adapter.input.web.ClienteRequest;
import br.com.cbd.gestor_clientes.adapter.input.web.ClienteResponse;
import br.com.cbd.gestor_clientes.core.domain.model.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "criadoEm", ignore = true)
    @Mapping(target = "atualizadoEm", ignore = true)
    Cliente toModel(ClienteRequest clienteRequest);

    ClienteResponse toResponse(Cliente cliente);

    List<ClienteResponse> toResponseList(List<Cliente> clientes);
}