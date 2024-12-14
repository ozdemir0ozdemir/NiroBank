create sequence refresh_tokens_id_seq start with 1 increment by 1;

create table refresh_tokens
(
    refresh_token_id bigint not null unique primary key default nextval('refresh_tokens_id_seq'),
    ref_id varchar(100) not null unique,
    username varchar(50) not null,
    refresh_token text not null unique,
    expires_at timestamp not null,
    refresh_token_status varchar(50) not null,
    constraint allowedStatuses check ( refresh_token_status in ('ACCEPTABLE', 'REVOKED'))
);