CREATE TABLE IF NOT EXISTS users (
                       id VARCHAR(255) PRIMARY KEY,
                       username VARCHAR(255) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       coins INTEGER

);

CREATE TABLE IF NOT EXISTS cards (
                        card_id VARCHAR(255) PRIMARY KEY,
                        name VARCHAR(255),
                        damage INTEGER,
                        type VARCHAR(255),
                        isSpell BOOLEAN
);

CREATE TABLE IF NOT EXISTS packages (
                                package_id SERIAL PRIMARY KEY,
                                card1 VARCHAR(255) REFERENCES cards(card_id),
                                card2 VARCHAR(255) REFERENCES cards(card_id),
                                card3 VARCHAR(255) REFERENCES cards(card_id),
                                card4 VARCHAR(255) REFERENCES cards(card_id),
                                card5 VARCHAR(255) REFERENCES cards(card_id)
);

CREATE TABLE IF NOT EXISTS bought (
                              username VARCHAR(255),
                              card_id VARCHAR(255),
                              PRIMARY KEY (username, card_id)
);

Create TABLE IF NOT EXISTS deck (
                            username VARCHAR(255),
                            card_id VARCHAR(255),
                            PRIMARY KEY (username, card_id)
);