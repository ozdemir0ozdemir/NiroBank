create sequence account_id_seq start with 1 increment by 1;

create table accounts
(
    account_id bigint default nextval('account_id_seq'),
    account_number varchar(100) not null unique,
    account_holder_name varchar(100) not null,
    balance numeric(38,2) not null,
    created_at timestamptz not null,
    updated_at timestamptz not null,
    primary key (account_id)
);