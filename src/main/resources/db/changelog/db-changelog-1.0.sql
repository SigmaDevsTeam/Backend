-- --liquibase formatted sql

--changeset artem:1
create table users
(
    id         bigserial primary key,
    username   text not null unique,
    email      text not null unique,
    password   text  ,
    image      text not null,
    role       text not null default 'USER',
    rating     float,
--     room_id    bigint references room(id) on delete set null ,
    users_rated int
);

--changeset artem:2
create table quest
(
    id          bigserial primary key,
    title       text not null,
    description text,
    image       text,
    task_count  int,
    time_limit  time,
    user_id     bigint references users (id),
    rating      float,
    users_rated  int
);


--changeset artem:3
create table room(
                     id bigserial primary key ,
                     title text,
                     is_active boolean not null ,
                     quest_id bigint not null references quest(id),
                    username text
);


--changeset artem:4
alter table users add column room_id bigint references room(id) on delete set null;