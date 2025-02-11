-- --liquibase formatted sql

--changeset artem:1
create table message(
    id bigserial primary key ,
    room_id bigint not null references room(id),
    text text not null ,
    author_id bigint references users(id)
);


