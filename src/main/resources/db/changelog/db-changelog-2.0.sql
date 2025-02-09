-- --liquibase formatted sql

--changeset artem:1
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

--changeset artem:2
create table task
(
    id          bigserial primary key,
    title       text not null,
    audio       text,
    video       text,
    image       text,
    open_answer text,
    quest_id    bigint references quest (id)
);

--changeset artem:3
create table option
(
    id      bigserial primary key,
    title   text    not null,
    is_true  boolean not null,
    task_id bigint references task (id)
);