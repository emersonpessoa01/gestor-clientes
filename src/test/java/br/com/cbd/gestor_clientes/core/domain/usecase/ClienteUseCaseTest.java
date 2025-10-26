package br.com.cbd.gestor_clientes.core.domain.usecase;

import br.com.cbd.gestor_clientes.adapter.input.exception.BusinessException;
import br.com.cbd.gestor_clientes.adapter.input.exception.NotFoundException;
import br.com.cbd.gestor_clientes.adapter.output.repository.ClienteRepository;
import br.com.cbd.gestor_clientes.core.domain.model.Cliente;
import br.com.cbd.gestor_clientes.core.usecase.ClienteUseCase;
import br.com.cbd.gestor_clientes.port.output.ClienteOutputPort;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteUseCaseTest {

    @Mock
    private ClienteOutputPort outputPort;

    @Mock
    private ClienteRepository repository;

    @InjectMocks
    private ClienteUseCase useCase;

    private Cliente cliente;

    @BeforeEach
    void setup() {
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Nome Teste");
        cliente.setCpf("11144477735");
        cliente.setEmail("teste@teste.com");
        cliente.setStatus("ATIVO");
    }


    @Test
    @DisplayName("Deve deletar cliente com sucesso")
    void deveDeletarClienteComSucesso() {
        Long id = 1L;
        when(outputPort.findById(id)).thenReturn(Optional.of(cliente));
        when(outputPort.update(any(Cliente.class))).thenReturn(cliente);
        doNothing().when(repository).delete(id);

        useCase.delete(id);

        verify(outputPort).findById(id);
        verify(outputPort).update(any(Cliente.class));
        verify(repository).delete(id);
    }




    @Test
    @DisplayName("Deve buscar cliente por CPF com sucesso")
    void deveBuscarClientePorCpfComSucesso() {
        when(repository.findByCpf("11144477735")).thenReturn(Optional.of(cliente));

        Optional<Cliente> resultado = useCase.buscarPorCpf("11144477735");

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNome()).isEqualTo("Nome Teste");
        verify(repository).findByCpf("11144477735");
    }

    @Test
    @DisplayName("Deve ativar cliente com sucesso")
    void deveAtivarCliente() {
        when(repository.findById(1L)).thenReturn(Optional.of(cliente));
        doNothing().when(repository).ativarCliente(1L);

        useCase.ativarCliente(1L);

        verify(repository).ativarCliente(1L);
    }

    @Test
    @DisplayName("Deve lançar NotFoundException ao ativar cliente inexistente")
    void deveLancarExcecaoAoAtivarClienteNaoExistente() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> useCase.ativarCliente(1L));
    }

    @Test
    @DisplayName("Deve inativar cliente com sucesso")
    void deveInativarCliente() {
        when(repository.findById(1L)).thenReturn(Optional.of(cliente));
        doNothing().when(repository).inativarCliente(1L);

        useCase.inativarCliente(1L);

        verify(repository).inativarCliente(1L);
    }

    @Test
    @DisplayName("Deve lançar NotFoundException ao inativar cliente inexistente")
    void deveLancarExcecaoAoInativarClienteNaoExistente() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> useCase.inativarCliente(1L));
    }

    @Test
    @DisplayName("Deve lançar NotFoundException ao buscar cliente por CPF inexistente")
    void deveLancarExcecaoAoBuscarPorCpfNaoExistente() {
        when(repository.findByCpf("11144477735")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> useCase.buscarPorCpf("11144477735"));
    }

    @Test
    @DisplayName("Deve contar clientes ativos com sucesso")
    void deveContarClientesAtivos() {
        when(repository.contarClientesAtivos()).thenReturn(5);

        int count = useCase.contarClientesAtivos();

        assertEquals(5, count);
    }

    @Test
    @DisplayName("Deve contar clientes inativos com sucesso")
    void deveContarClientesInativos() {
        when(repository.contarClientesInativos()).thenReturn(3);

        int count = useCase.contarClientesInativos();

        assertEquals(3, count);
    }

    @Test
    @DisplayName("Deve listar clientes ativos com sucesso")
    void deveListarClientesAtivos() {
        when(repository.listarAtivos()).thenReturn(List.of(cliente));

        var list = useCase.listarAtivos();

        assertFalse(list.isEmpty());
        assertEquals("Nome Teste", list.get(0).getNome());
    }

    @Test
    @DisplayName("Deve listar clientes inativos com sucesso")
    void deveListarClientesInativos() {
        when(repository.listarInativos()).thenReturn(List.of(cliente));

        var list = useCase.listarInativos();

        assertFalse(list.isEmpty());
        assertEquals("Nome Teste", list.get(0).getNome());
    }

    @Test
    @DisplayName("validarCpf deve aceitar CPF válido")
    void deveAceitarCpfValido() {
        assertTrue(useCase.validarCpf("11144477735"));
    }

    @Test
    @DisplayName("validarCpf deve rejeitar CPF inválido")
    void deveRejeitarCpfInvalido() {
        assertFalse(useCase.validarCpf("12345678900"));
        assertFalse(useCase.validarCpf(null));
        assertFalse(useCase.validarCpf(""));
        assertFalse(useCase.validarCpf("00000000000"));
    }

    // metodo utilitário para invocar metodo privado validarClienteParaCriacao
    private void invokeValidarClienteParaCriacao(ClienteUseCase useCase, Cliente cliente) {
        try {
            var method = ClienteUseCase.class.getDeclaredMethod("validarClienteParaCriacao", Cliente.class);
            method.setAccessible(true);
            method.invoke(useCase, cliente);
        } catch (Exception e) {
            if (e.getCause() instanceof RuntimeException)
                throw (RuntimeException) e.getCause();
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("validarClienteParaCriacao deve lançar BusinessException para cliente inválido")
    void deveLancarExcecaoValidarClienteParaCriacao() {
        Cliente clienteInvalido = new Cliente();
        // deixar dados inválidos intencionalmente
        clienteInvalido.setNome("Jo"); // nome muito curto
        clienteInvalido.setEmail("email@valido.com");
        clienteInvalido.setCpf("11144477735");
        clienteInvalido.setStatus("ATIVO");

        assertThrows(BusinessException.class, () -> invokeValidarClienteParaCriacao(useCase, clienteInvalido));
    }
}
