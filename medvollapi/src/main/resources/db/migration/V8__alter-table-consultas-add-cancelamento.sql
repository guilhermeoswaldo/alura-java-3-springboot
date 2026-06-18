alter table consultas
    add column status varchar(100) not null default 'AGENDADO';

alter table consultas
    add column motivo_cancelamento varchar(100);
