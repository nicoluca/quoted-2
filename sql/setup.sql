-- Auto-generated definition of the database schema for PostgreSQL

drop table if exists quotes;
drop table if exists sources;
drop table if exists users;

create table users
(
    id       uuid
        primary key
            default uuid_generate_v4(),
    email    varchar(255)
        constraint unique_email
            unique
);


create table sources
(
    id   bigserial
        primary key,
    name varchar(255)
        constraint unique_source_name
            unique,
    user_id uuid
        constraint fk_user_id
            references users
);

create table quotes
(
    id        bigserial
        primary key,
    source_id bigint
        constraint fk_source_id
            references sources,
    datetime_created timestamp with time zone default now(),
    text      text,
    user_id   uuid
        constraint fk_user_id
            references users
);
