-- !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
-- RYDDER ALT EKSISTERENDE DATA I boardgames SCHEMA.
-- !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

DROP SCHEMA IF EXISTS boardgames CASCADE;
CREATE SCHEMA boardgames;
SET SEARCH_PATH = "boardgames";

/************************
 * tabeller
 ************************/

CREATE TABLE account
(
    account_id      serial  NOT NULL PRIMARY KEY ,
    username        varchar NOT NULL UNIQUE ,
    hashed_password varchar NOT NULL
);

CREATE TABLE game
(
    game_id         serial  NOT NULL PRIMARY KEY ,
    name            varchar NOT NULL
);

CREATE TABLE match
(
    match_id        serial  NOT NULL PRIMARY KEY ,
    state           varchar NOT NULL ,
    owner_id        int     NOT NULL REFERENCES account(account_id),
    game_id         int     NOT NULL REFERENCES game(game_id)
);

CREATE TABLE participant
(
    participant_id  serial  NOT NULL PRIMARY KEY ,
    match_id        int     NOT NULL REFERENCES match(match_id),
    account_id      int     NOT NULL REFERENCES account(account_id),
    accepted        bool    NOT NULL,
    rejected        bool    NOT NULL
);

/************************
 * test data
 ************************/

INSERT INTO account
    (username, hashed_password)
VALUES
    ('BenDover',            'b025079c90813d4669136b2ed07512204ee05522ba3e647935f1a88daf00fd43'),    -- password = 'julie'
    ('Maja123',             'f29e94153eb385ba00ebb23aca2deaa24222e449584d1d91af4ff2ccc92c8ba5'),    -- password = 'maja'
    ('Minii❤',             '0a5d17d3b19f82f8340d3977609aa9e86b4ad8b9bd71bd9eced9271f1d5b2e4a'),    -- password = 'simon'
    ('xdxd_2fast4u_xdxd',   'dcd69bed70a827d5fdda1d28272d508c795fb32cebab243d5208ec9ef89f6453');    -- password = 'rune'

INSERT INTO game
    (name)
VALUES
    ('TicTacToe'),
    ('Stratego');

INSERT INTO match
    (state, owner_id, game_id)
VALUES
    ('.........', 4, 1)
