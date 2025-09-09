-- Active: 1753386242588@@127.0.0.1@3306

-- duas formas de consultar a function
-- 1.
SELECT public.contar_clientes_ativos()

-- 2.
SELECT * from contar_clientes_ativos()

--1. Consulta de clientes ativos(GET/clientes/ativos)
CREATE OR REPLACE FUNCTION fn_get_clinetes_ativos()
RETURNS TABLE(
	id BIGINT, 
	nome TEXT,
	email TEXT,
	telefone TEXT,
	cpf TEXT,
	status TEXT
)
AS $$
BEGIN
	RETURN QUERY
	SELECT id,nome, email,telefone, cpf, status
	FROM cliente
	WHERE status = 'ATIVO';
END;
$$ LANGUAGE plpgsql;

--2. Consulta de clientes inativos(GET/clientes/inativos)
CREATE OR REPLACE FUNCTION fn_get_clientes_inativos()
RETURNS TABLE(
	id BIGINT,
	nome TEXT,
	email TEXT,
	telefone TEXT,
	cpf TEXT,
	status TEXT
)
BEGIN
	RETURN QUERY 
	SELECT id, nome,email,telefone,cpf,status
	FROM cliente
	WHERE status = 'INATIVO'
END;
$$ LANGUAGE plpgsql;

