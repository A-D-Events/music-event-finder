DROP TABLE IF EXISTS artists;

CREATE TABLE artists (
    id TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    popularity INTEGER,
    genre TEXT,
    followers INTEGER
);