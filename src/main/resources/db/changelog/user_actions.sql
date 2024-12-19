create table if not exists  public.user_actions
(
    id     bigserial
        primary key,
    action varchar not null,
    datetime   timestamp    not null,
    user_ip varchar    not null
);


