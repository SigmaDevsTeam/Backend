-- --liquibase formatted sql

--changeset artem:1
create table comment
(
    id       bigserial primary key,
    title    text not null,
    user_id  bigint references users (id) not null ,
    quest_id bigint references quest (id) not null
);