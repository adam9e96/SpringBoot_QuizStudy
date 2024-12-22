create table quiz
(
    id       serial
        primary key,
    question text        not null,
    answer   boolean     not null,
    author   varchar(20) not null
);