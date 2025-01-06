CREATE TABLE url
(
    hash VARCHAR(6) PRIMARY KEY,
    url VARCHAR(500) NOT NULL,
    created_at timestamp
);

CREATE TABLE hash (
    hash VARCHAR(6) PRIMARY KEY
)