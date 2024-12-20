create sequence account_id_seq start with 1 increment by 1;

create table accounts
(
    account_id bigint not null unique primary key default nextval('account_id_seq')
);