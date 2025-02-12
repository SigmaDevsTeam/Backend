-- --liquibase formatted sql



--changeset artem:1
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

--changeset artem:2
create table option
(
    id      bigserial primary key,
    title   text    not null,
    is_true  boolean not null,
    task_id bigint references task (id)
);