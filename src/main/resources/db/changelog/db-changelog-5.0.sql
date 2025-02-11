-- --liquibase formatted sql

--changeset artem:1
create table user_result(
    id bigserial primary key ,
    user_id bigint not null references users(id),
    quest_id bigint not null references quest(id),
    result int not null default 0,
    constraint unique_user_quest unique (user_id, quest_id)
);
