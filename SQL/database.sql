-- !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
-- RYDDER ALT EKSISTERENDE DATA I boardgames SCHEMA.
-- !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

DROP SCHEMA IF EXISTS boardgames CASCADE;
CREATE SCHEMA boardgames;
SET SEARCH_PATH = "boardgames";

--------------------------------
-- tabeller
--------------------------------
CREATE TABLE account
(
    account_id              serial  NOT NULL PRIMARY KEY ,
    username                varchar NOT NULL UNIQUE ,
    first_name              varchar NOT NULL ,
    last_name               varchar NOT NULL ,
    email                   varchar NOT NULL ,
    description             varchar NOT NULL DEFAULT '',
    registration_datetime   timestamp NOT NULL ,
    status                  int     NOT NULL CHECK (status IN (0, 1, 2, 3, 4)) , -- NOTE(rune): Se konstanter i Account.java
    hashed_password         varchar NOT NULL,
    profile_picture         varchar NULL, -- NOTE(rune): Som Base 64.
    profile_picture_type    varchar NULL, -- NOTE(rune): MIME content type.
    created_on              timestamp NOT NULL DEFAULT now(),
    is_admin                bool    NOT NULL DEFAULT false
);

CREATE TABLE game
(
    game_id         serial      NOT NULL PRIMARY KEY ,
    name            varchar     NOT NULL ,
    created_on      timestamp   NOT NULL DEFAULT now()
);

CREATE TABLE match
(
    match_id        serial      NOT NULL PRIMARY KEY ,
    data            varchar     NOT NULL ,
    owner_id        int         NOT NULL REFERENCES account(account_id),
    game_id         int         NOT NULL REFERENCES game(game_id) ,
    status          int         NOT NULL ,  -- NOTE(rune): Se konstanter i Match.java
    started_on      timestamp   NULL ,
    finished_on     timestamp   NULL ,
    created_on      timestamp   NOT NULL DEFAULT now()
);

CREATE TABLE participant
(
    participant_id      serial  NOT NULL PRIMARY KEY ,
    match_id            int     NOT NULL REFERENCES match(match_id) ,
    account_id          int     NOT NULL REFERENCES account(account_id) ,
    status              int     NOT NULL CHECK (status IN (0, 1, 2, 3, 4    )) , -- NOTE(rune): Se konstanter i Particpant.java
    created_on          timestamp   NOT NULL DEFAULT now(),
    score               int     NOT NULL DEFAULT 0
);

--------------------------------
-- test data
--------------------------------

INSERT INTO account
    (username, first_name, last_name, email, registration_datetime, hashed_password, status, is_admin)
VALUES
    ('BenDover',            'Julie', 'Bramsen', 'julie@juliemail.dk',   now(), 'b025079c90813d4669136b2ed07512204ee05522ba3e647935f1a88daf00fd43',  2, false),    -- password = 'julie'
    ('Maja123',             'Maja', 'Brixen', 'maja@majamail.dk',       now(),'f29e94153eb385ba00ebb23aca2deaa24222e449584d1d91af4ff2ccc92c8ba5',   2, false),    -- password = 'maja'
    ('Minii',              'Simon', 'Banh', 'simon@simonmail.dk',      now(),'0a5d17d3b19f82f8340d3977609aa9e86b4ad8b9bd71bd9eced9271f1d5b2e4a',    2, false),    -- password = 'simon'
    ('rune',                'Rune', 'Lauridsen', 'rune@runemail.dk',   now(),'bab432c8f61b2be04113e49c595cc29a0ed57179a105fa098c270cf0dff3ddee',    2, false),    -- password = 'runerune'
    ('Bob',                'Bob', 'III', 'bobsep3bob@gmail.com',        now(),'6340a7a742bb4e89ff6e7b106548ba4f9fa29b44d7cd205f5f490ffb01215f7d',   2, true),     -- password = 'bobersej'
    ('Sundar',             'Sundar', 'Pichai', 'sundar@gmail.com',        now(),'89beafb3790f2fb5917de6baea168ad7d4b9bfa9a4e8961997cb532f561b28d7', 1, true);    -- password = 'sundar'

INSERT INTO game
    (name)
VALUES
    ('TicTacToe'),
    ('Stratego');
