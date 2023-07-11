-- Auto-generated definition of the database schema for PostgreSQL

create table if not exists sources
(
    id   bigserial
        primary key,
    name varchar(255)
        constraint unique_source_name
            unique
);

create table if not exists quotes
(
    id        bigserial
        primary key,
    source_id bigint
        constraint fk_source_id
            references sources,
    text      text
);
