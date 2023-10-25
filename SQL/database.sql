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
    name            varchar NOT NULL UNIQUE
);

CREATE TABLE game
(
    game_id         serial  NOT NULL PRIMARY KEY ,
    name            varchar NOT NULL
);

CREATE TABLE session
(
    session_id      serial  NOT NULL PRIMARY KEY ,
    state           varchar NOT NULL ,
    game_id         int     NOT NULL REFERENCES game(game_id)
);

CREATE TABLE session_participant
(
    session_participant_id  serial  NOT NULL PRIMARY KEY ,
    session_id              int     NOT NULL REFERENCES session(session_id),
    account_id              int     NOT NULL REFERENCES account(account_id)
);

/************************
 * test data
 ************************/

INSERT INTO account (name) VALUES
    ('BenDover'),
    ('Maja123'),
    ('Minii❤'),
    ('xdxd_2fast4u_xdxd');

INSERT INTO game(name) VALUES
    ('TicTacToe'),
    ('Stratego');



