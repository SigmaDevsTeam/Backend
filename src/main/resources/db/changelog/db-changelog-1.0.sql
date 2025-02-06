-- --liquibase formatted sql

--changeset artem:1
create table users(
                      id bigserial primary key ,
                      username text not null unique ,
                      email text not null unique ,
                      password text  ,
                      image text not null,
                      role text not null default 'USER',
                      sign_up_method text not null
);