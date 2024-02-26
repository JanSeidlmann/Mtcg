CREATE TABLE IF NOT EXISTS users (
                       id VARCHAR(255) PRIMARY KEY,
                       Username VARCHAR(255) UNIQUE NOT NULL,
                       Password VARCHAR(255) NOT NULL,
                       Name Varchar(255),
                       coins INTEGER,
                       Bio VARCHAR(255),
                       Image VARCHAR(255)

);

CREATE TABLE IF NOT EXISTS cards (
                        Id VARCHAR(255) PRIMARY KEY,
                        Name VARCHAR(255),
                        Damage INTEGER,
                        Type VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS packages (
                                package_id SERIAL PRIMARY KEY,
                                card1 VARCHAR(255) REFERENCES cards(Id),
                                card2 VARCHAR(255) REFERENCES cards(Id),
                                card3 VARCHAR(255) REFERENCES cards(Id),
                                card4 VARCHAR(255) REFERENCES cards(Id),
                                card5 VARCHAR(255) REFERENCES cards(Id)
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

Create TABLE IF NOT EXISTS trade (
                                    trade_id VARCHAR(255) PRIMARY KEY,
                                    card_id VARCHAR(255),
                                    type VARCHAR(255),
                                    damage INTEGER,
                                    sellerUsername VARCHAR(255)
);

Create TABLE IF NOT EXISTS stats (
                                     username VARCHAR(255) PRIMARY KEY,
                                     totalGames INTEGER,
                                     gamesWon INTEGER,
                                     gamesLost INTEGER,
                                     elo INTEGER
);