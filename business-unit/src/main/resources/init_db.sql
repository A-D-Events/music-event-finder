DROP TABLE IF EXISTS artists;
DROP TABLE IF EXISTS events;

CREATE TABLE artists (
    id text PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    popularity INT,
    genres TEXT,
    followers INT,
    image_url VARCHAR(255)
);

CREATE TABLE events (
    id text PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    url VARCHAR(255) NOT NULL,
    date VARCHAR(50),
    venue VARCHAR(255),
    city VARCHAR(100),
    image VARCHAR(255)
);