alter table medicos
    add ativo boolean;
update medicos
set ativo = true;
alter table medicos
    modify column ativo boolean not null;
