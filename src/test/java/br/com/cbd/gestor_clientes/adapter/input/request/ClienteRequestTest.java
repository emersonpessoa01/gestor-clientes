package br.com.cbd.gestor_clientes.adapter.input.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ClienteRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidatorInstance() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Deve gerar violações ao enviar campos obrigatórios vazios")
    void testValidacaoCamposObrigatorios() {
        // Given
        ClienteRequest request = new ClienteRequest();
        request.setNome("");
        request.setEmail("");
        request.setTelefone("");
        request.setCpf("");
        request.setStatus("");

        // When
        Set<ConstraintViolation<ClienteRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations)
                .isNotEmpty()
                .hasSizeGreaterThanOrEqualTo(1);

        violations.forEach(v ->
                System.out.println(v.getPropertyPath() + ": " + v.getMessage())
        );
    }
}
