SELECT * FROM public.cliente
ORDER BY id ASC

-- Função para contar clientes ativos
CREATE OR REPLACE FUNCTION public.contar_clientes_ativos()
RETURNS INTEGER AS $$
DECLARE
    total INTEGER;
BEGIN
    SELECT COUNT(*) INTO total
    FROM cliente
    WHERE status = 'ATIVO';
    RETURN total;
END;
$$ LANGUAGE plpgsql;


SELECT public.contar_clientes_ativos();


SELECT *
FROM pg_proc
WHERE proname = 'contar_clientes_ativos';

