DROP TABLE IF EXISTS users, participants, chatrooms;

CREATE TABLE users
(
    id         serial primary key,
    name       varchar unique not null check (length(name) <= 12 and length(name) >= 4),
    password   varchar        not null check (length(password) <= 12 and length(password) >= 8),
    created_at timestamp      not null default now()
);

CREATE TABLE chatrooms
(
    id         serial primary key,
    name       varchar   not null check (length(name) <= 12 and length(name) >= 4),
    isPrivate  bool      not null,
    created_at timestamp not null default now()
);

CREATE TABLE participants
(
    user_id     integer   NOT NULL references users (id) ON DELETE CASCADE,
    chatroom_id integer   NOT NULL references chatrooms (id) ON DELETE CASCADE,
    created_at  timestamp not null default now(),
    unique (user_id, chatroom_id)
);

