package br.com.cbd.gestor_clientes.adapter.input.mapper;

import br.com.cbd.gestor_clientes.adapter.input.request.ClienteRequest;
import br.com.cbd.gestor_clientes.core.domain.model.Cliente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@DisplayName("Teste de unidade do ClienteMapper")
public class ClienteMapperTest {

    // Obtém a implementação gerada automaticamente pelo MapStruct
    private final ClienteMapper mapper = Mappers.getMapper(ClienteMapper.class);

    @Test
    @DisplayName("Deve converter ClienteRequest em Cliente corretamente")
    void deveConverterRequestParaModel() {
        // Given
        ClienteRequest request = new ClienteRequest();
        request.setNome("Emerson Pessoa");
        request.setEmail("emerson@teste.com");
        request.setTelefone("99999-9999");
        request.setCpf("12345678900");
        request.setStatus("ATIVO");


        // When
        Cliente cliente = mapper.toModel(request);

        // Then
        assertThat(cliente).isNotNull();
        assertThat(cliente.getNome()).isEqualTo(request.getNome());
        assertThat(cliente.getEmail()).isEqualTo(request.getEmail());
        assertThat(cliente.getTelefone()).isEqualTo(request.getTelefone());
        assertThat(cliente.getCpf()).isEqualTo(request.getCpf());
        assertThat(cliente.getStatus()).isEqualTo(request.getStatus());

    }
}
