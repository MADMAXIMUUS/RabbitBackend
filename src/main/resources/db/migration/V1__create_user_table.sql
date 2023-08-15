CREATE TABLE "user"
(
    id              INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name            VARCHAR(255) NOT NULL,
    username        VARCHAR(255) UNIQUE,
    email           VARCHAR(255) NOT NULL UNIQUE,
    birthdate       DATE NOT NULL,
    avatar_url      VARCHAR(255),
    password_salt   VARCHAR NOT NULL,
    password_hash   VARCHAR NOT NULL,
    created_at      TIMESTAMP NOT NULL
);