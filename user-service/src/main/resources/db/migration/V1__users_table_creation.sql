create sequence users_id_sequence start with 1 increment by 1;

create table users
(
    user_id  bigint default nextval('users_id_sequence'),
    username varchar(50) unique not null ,
    password varchar(255) not null,
    role     varchar(20),
    primary key (user_id),
    constraint allowed_roles check (role in ('USER', 'MANAGER', 'ADMIN'))
);

