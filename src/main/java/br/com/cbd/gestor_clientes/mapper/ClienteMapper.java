package br.com.cbd.gestor_clientes.mapper;

import br.com.cbd.gestor_clientes.adapter.in.web.ClienteRequest;
import br.com.cbd.gestor_clientes.adapter.in.web.ClienteResponse;
import br.com.cbd.gestor_clientes.core.domain.model.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClienteMapper {
    @Mapping(target = "id", ignore = true)
    Cliente toModel(ClienteRequest request);

    ClienteResponse toResponse(Cliente cliente);
}