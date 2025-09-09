-- Função para contar clientes ativos
create or replace function public.contar_clientes_ativos()
RETURNS INTEGER AS $$
declare
    total integer;
begin
    select count(*) into total
    from cliente
    where status = 'ATIVO';
    return total;
end;
$$ LANGUAGE plpgsql;
