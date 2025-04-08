DROP TABLE IF EXISTS artists;

CREATE TABLE artists (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    popularity INT,
    type VARCHAR(50),
    uri VARCHAR(255),
    href VARCHAR(255),
    genres TEXT,
    followers INT,
    image_url VARCHAR(255)
);