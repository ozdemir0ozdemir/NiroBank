create sequence tokens_id_seq start with 1 increment by 1;

create table tokens
(
    id bigint not null unique primary key default nextval('tokens_id_seq'),
    token_id varchar(100) not null unique,
    username varchar(50) not null,
    token text not null unique,
    expires_at timestamp not null,
    token_status varchar(50) not null,
    constraint allowedStatuses check ( token_status in ('ACCEPTABLE', 'REVOKED'))
);