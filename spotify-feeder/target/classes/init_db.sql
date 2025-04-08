DROP TABLE IF EXISTS artists;
DROP TABLE IF EXISTS events;
DROP TABLE IF EXISTS artist_event;

CREATE TABLE artists (
    id TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    popularity INTEGER,
    genre TEXT,
    followers INTEGER
);

CREATE TABLE events (
    id TEXT PRIMARY KEY,
    name TEXT NOT NULL,       
    date TEXT,                 
    venue TEXT,              
    city TEXT,                 
    country TEXT,              
    url TEXT 
);

CREATE TABLE artist_event (
    artist_id TEXT,
    event_id TEXT,
    PRIMARY KEY (artist_id, event_id),
    FOREIGN KEY (artist_id) REFERENCES artists(id),
    FOREIGN KEY (event_id) REFERENCES events(id)
);
