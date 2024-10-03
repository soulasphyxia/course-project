create table if not exists public.tags
(
    id         bigserial primary key,
    name      varchar not null
);
create table if not exists public.tags_in_videos
(
    id         bigserial primary key,
    video_id      bigserial not null,
    tag_id bigserial not null,
    CONSTRAINT fk_video FOREIGN KEY(video_id) REFERENCES video(id),
    CONSTRAINT fk_tags FOREIGN KEY(tag_id) REFERENCES tags(id) ON DELETE CASCADE
);