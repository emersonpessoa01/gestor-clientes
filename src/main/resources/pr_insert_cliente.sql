CREATE OR REPLACE PROCEDURE pr_insert_cliente(
    IN p_nome     TEXT,
    IN p_email    TEXT,
    IN p_telefone TEXT,
    IN p_cpf      TEXT,
    IN p_status   TEXT,
    OUT o_id      BIGINT
)
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO cliente (nome, email, telefone, cpf, status)
    VALUES (p_nome, p_email, p_telefone, p_cpf, p_status)
    RETURNING id INTO o_id;
END;
$$;

CALL pr_insert_cliente(
'Luke Skywalker',
'lukeskywalker@email.com',
'+55(11) 99999-9999',
'648.989.910-84',
'ATIVO',
o_id := NULL
);
