create table if not exists  public.user_actions
(
    id     bigserial
        primary key,
    action varchar not null,
    time   time    not null,
    date   date    not null
);


