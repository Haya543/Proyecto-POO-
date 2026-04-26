DROP TABLE IF EXISTS battles CASCADE;
DROP TABLE IF EXISTS trainer_items CASCADE;
DROP TABLE IF EXISTS trainer_pokemon CASCADE;
DROP TABLE IF EXISTS items CASCADE;
DROP TABLE IF EXISTS pokemon CASCADE;
DROP TABLE IF EXISTS trainers CASCADE;


-- Entrenadores
CREATE TABLE trainers (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    region TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Pokémon
CREATE TABLE pokemon (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    type TEXT NOT NULL,
    base_hp INT NOT NULL,
    base_attack INT NOT NULL,
    base_defense INT NOT NULL
);

-- Equipo del entrenador
CREATE TABLE trainer_pokemon (
    id SERIAL PRIMARY KEY,
    trainer_id INT REFERENCES trainers(id) ON DELETE CASCADE,
    pokemon_id INT REFERENCES pokemon(id) ON DELETE CASCADE,
    level INT DEFAULT 1,
    current_hp INT,
    is_active BOOLEAN DEFAULT true
);

-- Items
CREATE TABLE items (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    effect TEXT
);

-- Inventario
CREATE TABLE trainer_items (
    id SERIAL PRIMARY KEY,
    trainer_id INT REFERENCES trainers(id) ON DELETE CASCADE,
    item_id INT REFERENCES items(id) ON DELETE CASCADE,
    quantity INT DEFAULT 1
);

-- Batallas
CREATE TABLE battles (
    id SERIAL PRIMARY KEY,
    trainer1_id INT REFERENCES trainers(id),
    trainer2_id INT REFERENCES trainers(id),
    winner_id INT REFERENCES trainers(id),
    battle_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO trainers (name, region) VALUES
('Ash', 'Kanto'),
('Gary', 'Kanto');

INSERT INTO pokemon (name, type, base_hp, base_attack, base_defense) VALUES
('Pikachu', 'Electric', 35, 55, 40),
('Charmander', 'Fire', 39, 52, 43),
('Bulbasaur', 'Grass', 45, 49, 49);

INSERT INTO items (name, effect) VALUES
('Potion', 'Heal 20 HP'),
('Super Potion', 'Heal 50 HP');

INSERT INTO trainer_pokemon (trainer_id, pokemon_id, level, current_hp) VALUES
(1, 1, 5, 35),
(2, 2, 5, 39);
