create table usuario_perfis
(
    usuario_id       bigint      not null,
    perfil_permissao varchar(50) not null,
    primary key (usuario_id, perfil_permissao),
    constraint fk_usuario_perfis_usuario_id foreign key (usuario_id) references usuarios (id)
);

-- Atribuindo a ROLE_USER padrão para todos os usuários já existentes no banco
insert into usuario_perfis (usuario_id, perfil_permissao)
select id, 'ROLE_USER'
from usuarios;