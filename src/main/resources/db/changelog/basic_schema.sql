create table if not exists public.video
(
    id         bigserial primary key,
    title      varchar not null,
    content    varchar,
    created_at date not null default now(),
    url varchar
);