CREATE TABLE url
(
    hash VARCHAR(62) PRIMARY KEY,
    url VARCHAR(500) NOT NULL,
    created_at timestamp
);

CREATE TABLE hash (
    hash VARCHAR(62) PRIMARY KEY
)