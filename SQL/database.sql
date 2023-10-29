-- !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
-- RYDDER ALT EKSISTERENDE DATA I boardgames SCHEMA.
-- !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

DROP SCHEMA IF EXISTS boardgames CASCADE;
CREATE SCHEMA boardgames;
SET SEARCH_PATH = "boardgames";

/************************
 * tabeller
 ************************/

-- TODO(rune): Hashed+salted password.
CREATE TABLE account
(
    account_id      serial  NOT NULL PRIMARY KEY ,
    username        varchar NOT NULL UNIQUE
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
    (username)
VALUES
    ('BenDover'),
    ('Maja123'),
    ('Minii❤'),
    ('xdxd_2fast4u_xdxd');

INSERT INTO game
    (name)
VALUES
    ('TicTacToe'),
    ('Stratego');

INSERT INTO match
    (state, owner_id, game_id)
VALUES
    ('.........', 4, 1)