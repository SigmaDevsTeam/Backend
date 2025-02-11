-- --liquibase formatted sql

--changeset artem:1
create table room(
    id bigserial primary key ,
    quest_id bigint not null references quest(id),
    code text not null
);


