create table if not exists public.tag
(
    id    bigserial primary key,
    title varchar not null
);


create table if not exists public.video
(
    id         bigserial primary key,
    title      varchar   not null,
    content    varchar,
    created_at date      not null default now(),
    url        varchar,
    visibility varchar   not null default 'PUBLIC',
    thumbnail  varchar   not null,
    likes      bigint    not null default 0,
    dislikes   bigint    not null default 0,
    tag_id     bigserial not null,
    FOREIGN KEY (tag_id) REFERENCES tag(id)
);