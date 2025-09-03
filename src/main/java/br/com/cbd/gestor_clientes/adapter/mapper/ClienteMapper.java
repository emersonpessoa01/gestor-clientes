package br.com.cbd.gestor_clientes.adapter.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.cbd.gestor_clientes.adapter.input.request.ClienteRequest;
import br.com.cbd.gestor_clientes.adapter.input.request.ClienteResponse;
import br.com.cbd.gestor_clientes.core.domain.model.Cliente;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "criadoEm", ignore = true)
    @Mapping(target = "atualizadoEm", ignore = true)
    Cliente toModel(ClienteRequest clienteRequest);

    ClienteResponse toResponse(Cliente cliente);

    List<ClienteResponse> toResponseList(List<Cliente> clientes);
}