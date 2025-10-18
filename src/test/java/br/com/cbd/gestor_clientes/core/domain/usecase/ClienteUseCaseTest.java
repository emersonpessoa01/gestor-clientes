package br.com.cbd.gestor_clientes.core.domain.usecase;

import br.com.cbd.gestor_clientes.adapter.output.repository.ClienteRepository;
import br.com.cbd.gestor_clientes.core.domain.model.Cliente;
import br.com.cbd.gestor_clientes.core.usecase.ClienteUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Testes de unidade para ClienteUseCase, camada de domínio (regras de negócio).
 * Segue o padrão Given-When-Then.
 */
@ExtendWith(MockitoExtension.class)
class ClienteUseCaseTest {

    @Mock
    private ClienteRepository repository;

    @InjectMocks
    private ClienteUseCase useCase;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = new Cliente(
                1L,
                "Sabine Wren",
                "sabinewren@mandalore.com",
                "+55(11)99999-9999",
                "11144477735",
                "ATIVO",
                null,
                null
        );
    }
    // ------------------------------------------------------------------
    @Test
    @DisplayName("Deve criar cliente com sucesso")
    void deveCriarClienteComSucesso() {
        // Given
        when(repository.save(any(Cliente.class))).thenReturn(cliente);

        // When
        Cliente resultado = useCase.create(cliente);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getNome())
                .isEqualTo("Sabine Wren");
        verify(repository, times(1))
                .save(any(Cliente.class));
    }
    //------------------------------------------------------------------
    @Test
    @DisplayName("Deve buscar cliente por ID com sucesso")
    void deveBuscarClientePorIdComSucesso(){
        // Given
        when(repository.findById(1L)).thenReturn(Optional.of(cliente));

        // When
        Optional<Cliente> resultado = useCase.findById(1L);

        // Then
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getEmail()).isEqualTo("sabinewren@mandalore.com");
        verify(repository, times(1)).findById(1L);
    }
    //------------------------------------------------------------------
    @Test
    @DisplayName("Deve lançar exceção ao buscar cliente inexistente por ID")
    void deveLancarExcecaoQuandoClienteNaoExistir() {
        // Given
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> useCase.findById(99L));

        assertThat(exception.getMessage()).contains("Cliente com ID 99 não encontrado");
        verify(repository, times(1)).findById(99L);
    }
    //------------------------------------------------------------------
    @Test
    @DisplayName("Deve listar todos os clientes com suesso")
    void deveListartodosOsClientesComSucesso(){
        // Given
        when(repository.findAll()).thenReturn(List.of(cliente));

        // When
        List<Cliente> resultado = useCase.findAll();

        // Then
        assertThat(resultado).isNotEmpty();
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getCpf()).isEqualTo("11144477735");
        verify(repository,times(1)).findAll();
    }
    //------------------------------------------------------------------
    @Test
    @DisplayName("Deve atualizar cliente com sucesso")
    void deveAtualizarClienteComSucesso() {
        // Given
        Cliente existente = new Cliente(
                1L,
                "Sabine Wren",
                "sabinewren@gmail.com",
                "+55(11)99999-9999",
                "11144477735",
                "ATIVO",
                null,
                null
        );

        Cliente atualizado = new Cliente(
                1L,
                "Sabine Ren",
                "sabinewren@mandalore.com",
                "+55(11)98888-8888",
                "11144477735",
                "ATIVO",
                null,
                null
        );

        when(repository.findById(1L)).thenReturn(Optional.of(existente)); // ✅ importante
        when(repository.update(any(Cliente.class))).thenReturn(atualizado);

        // When
        Cliente resultado = useCase.update(1L, atualizado);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getEmail()).isEqualTo("sabinewren@mandalore.com");
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).update(any(Cliente.class));
    }

}
