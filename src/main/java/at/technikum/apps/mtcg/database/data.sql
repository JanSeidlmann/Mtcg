CREATE TABLE IF NOT EXISTS users (
                       id VARCHAR(255) PRIMARY KEY,
                       username VARCHAR(255) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       coins INTEGER

);

CREATE TABLE IF NOT EXISTS cards (
                        package_id VARCHAR(255),
                        card_id VARCHAR(255) PRIMARY KEY,
                        name VARCHAR(255),
                        damage INTEGER,
                        type VARCHAR(255),
                        isSpell BOOLEAN
);

CREATE TABLE IF NOT EXISTS packages (
                                package_id VARCHAR(255) PRIMARY KEY,
                                bought BOOLEAN
);