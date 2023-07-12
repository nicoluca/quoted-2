-- Auto-generated definition of the database schema for PostgreSQL

drop table if exists quotes;
drop table if exists sources;


create table sources
(
    id   bigserial
        primary key,
    name varchar(255)
        constraint unique_source_name
            unique
);

create table quotes
(
    id        bigserial
        primary key,
    source_id bigint
        constraint fk_source_id
            references sources,
    datetime_created timestamp with time zone default now(),
    text      text
);
