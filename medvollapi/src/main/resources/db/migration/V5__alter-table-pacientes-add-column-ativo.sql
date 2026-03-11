alter table pacientes
    add ativo boolean;
update pacientes
set ativo = true;
alter table pacientes
    modify column ativo boolean not null;
