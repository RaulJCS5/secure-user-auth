drop schema if exists dbo cascade;

create schema dbo;

create table dbo.Users(
                          id int generated always as identity primary key,
                          username VARCHAR(64) unique not null,
                          password_validation VARCHAR(256) not null
);

create table dbo.Tokens(
                           token_validation VARCHAR(256) primary key,
                           user_id int references dbo.Users(id),
                           created_at bigint not null,
                           last_used_at bigint not null
);
