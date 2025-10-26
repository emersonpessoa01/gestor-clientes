-- Procedimento para popular cliente
/* CREATE OR REPLACE PROCEDURE pr_insert_cliente(
    IN  p_nome     VARCHAR,
    IN  p_email    VARCHAR,
    IN  p_telefone VARCHAR,
    IN  p_cpf      VARCHAR,
    IN  p_status   VARCHAR,
    OUT o_id       INTEGER
)
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO cliente (nome, email, telefone, cpf, status)
    VALUES (p_nome, p_email, p_telefone, p_cpf, p_status)
    RETURNING id INTO o_id;
END;
$$;


-- Chamar a procedure pr_insert_cliente
CALL pr_insert_cliente(
'obi-wan kenobi teste',
'obiwankenobiteste@gmail.com',
'+5591 92234-5679',
'578.322.720-07',
'ATIVO',
o_id := NULL
);

*/

ALTER TABLE cliente ALTER COLUMN id TYPE BIGINT;

-- Função fn_insert_cliente
CREATE OR REPLACE FUNCTION fn_insert_cliente(
    p_nome VARCHAR,
    p_email VARCHAR,
    p_telefone VARCHAR,
    p_cpf VARCHAR,
    p_status VARCHAR
) RETURNS BIGINT
LANGUAGE plpgsql
AS $$
DECLARE
    o_id BIGINT;
BEGIN
    INSERT INTO cliente (nome, email, telefone, cpf, status)
    VALUES (p_nome, p_email, p_telefone, p_cpf, p_status)
    RETURNING id INTO o_id;

    RETURN o_id;
END;
$$;

SELECT fn_insert_cliente('Teste Nome2', 'teste2@email.com', '1111-2223', '000.000.000-02', 'ATIVO');
SELECT fn_insert_cliente('Obi-wan Kenobi Teste5', 'obi5@mail.com', '9999-9999', '123.456.789-00', 'ATIVO');



-- Busca por duplicidade
-- \df+ pr_insert_cliente

/*
-- Remover duplicidade
SELECT proname, oid, proargnames, proargtypes
FROM pg_proc
WHERE proname = 'pr_insert_cliente';

-- dropar as duplicadas:
DROP PROCEDURE IF EXISTS pr_insert_cliente(
	VARCHAR,
	VARCHAR,
	VARCHAR,
	VARCHAR,
	VARCHAR,
	OUT INTEGER
	);

*/


-- Função para contar clientes ativos
CREATE OR REPLACE FUNCTION fn_count_clientes_ativos()
RETURNS BIGINT AS $$
BEGIN
    RETURN (
        SELECT COUNT(*)
        FROM cliente
        WHERE status = 'ATIVO'
    );
END;
$$ LANGUAGE plpgsql;

SELECT fn_count_clientes_ativos()

-- Função para contar clientes inativos
CREATE OR REPLACE FUNCTION fn_count_clientes_inativos()
RETURNS BIGINT AS $$
BEGIN
    RETURN (
        SELECT COUNT(*)
        FROM cliente
        WHERE status = 'INATIVO'
    );
END;
$$ LANGUAGE plpgsql;

SELECT fn_count_clientes_inativos()


-- Função para buscartodos os clientes
CREATE OR REPLACE FUNCTION fn_get_clientes()
RETURNS TABLE (
    id BIGINT,
    nome VARCHAR(255),
    email VARCHAR(255),
    telefone VARCHAR(20),
    cpf VARCHAR(14),
    status VARCHAR(20),
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP
)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    SELECT
        c.id,
        c.nome,
        c.email,
        c.telefone,
        c.cpf,
        c.status,
        c.criado_em,
        c.atualizado_em
    FROM cliente c
	ORDER BY id ASC;
END;
$$;

-- Função para buscar clientes inativos
CREATE OR REPLACE FUNCTION fn_get_clientes_ativos()
RETURNS TABLE (
    id BIGINT,
    nome VARCHAR(255),
    email VARCHAR(255),
    telefone VARCHAR(20),
    cpf VARCHAR(14),
    status VARCHAR(20),
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP
)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    SELECT
        c.id,
        c.nome,
        c.email,
        c.telefone,
        c.cpf,
        c.status,
        c.criado_em,
        c.atualizado_em
    FROM cliente c
    WHERE c.status = 'ATIVO'
	ORDER BY id ASC;
END;
$$;


-- Chamar função fn_get_clientes_ativos()
SELECT * FROM fn_get_clientes_ativos();

-- Função para buscar clientes inativos
CREATE OR REPLACE FUNCTION fn_get_clientes_inativos()
RETURNS TABLE (
    id BIGINT,
    nome VARCHAR(255),
    email VARCHAR(255),
    telefone VARCHAR(20),
    cpf VARCHAR(14),
    status VARCHAR(20),
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP
)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    SELECT
        c.id,
        c.nome,
        c.email,
        c.telefone,
        c.cpf,
        c.status,
        c.criado_em,
        c.atualizado_em
    FROM cliente c
    WHERE c.status = 'INATIVO'
	ORDER BY id ASC;
END;
$$;

-- chamar função fn_get_clientes_inativos()
SELECT * FROM fn_get_clientes_inativos();

-- procedure para ativar cliente pelo id

CREATE OR REPLACE PROCEDURE pr_ativar_cliente(p_id BIGINT)
LANGUAGE plpgsql
AS $$
BEGIN
	UPDATE cliente
	SET status = 'ATIVO',
		atualizado_em = CURRENT_TIMESTAMP
	WHERE id = p_id;
END;
$$;

-- procedure para inativar clientes pelo id
CREATE OR REPLACE PROCEDURE pr_inativar_cliente(p_id BIGINT)
LANGUAGE plpgsql
AS $$
BEGIN
	UPDATE cliente
	SET status = 'INATIVO',
		atualizado_em = CURRENT_TIMESTAMP
	WHERE id = p_id;
END;
$$;

CREATE OR REPLACE FUNCTION fn_get_cliente_por_cpf(p_cpf VARCHAR)
RETURNS TABLE(
    id BIGINT,
    nome VARCHAR,
    email VARCHAR,
    telefone VARCHAR,
    cpf VARCHAR,
    status VARCHAR,
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        c.id,
        c.nome,
        c.email,
        c.telefone,
        c.cpf,
        c.status,
        c.criado_em,
        c.atualizado_em
    FROM cliente c
    WHERE c.cpf = p_cpf;
END;
$$ LANGUAGE plpgsql;


SELECT * FROM fn_get_cliente_por_cpf('111.444.777-35');

ALTER TABLE cliente
ADD CONSTRAINT chk_status CHECK (status IN ('ATIVO','INATIVO','PROSPECT'));



