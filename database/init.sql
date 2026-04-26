-- =========================================================
-- 1. TIPOS ELEMENTALES
-- =========================================================
CREATE TABLE tipo_elemento (
    id          SERIAL PRIMARY KEY,
    nombre      VARCHAR(15) NOT NULL UNIQUE
);

-- Datos iniciales (los 7 tipos del juego)
INSERT INTO tipo_elemento (nombre) VALUES
    ('FUEGO'), ('AGUA'), ('PLANTA'), ('ELECTRICO'),
    ('TIERRA'), ('HIELO'), ('NORMAL');

-- =========================================================
-- 2. MOVIMIENTOS (predefinidos)
-- =========================================================
CREATE TABLE movimiento (
    id              SERIAL PRIMARY KEY,
    nombre          VARCHAR(30) NOT NULL UNIQUE,
    tipo_id         INT NOT NULL REFERENCES tipo_elemento(id),
    categoria       VARCHAR(10) NOT NULL CHECK (categoria IN ('FISICO','ESPECIAL','ESTADO')),
    poder           INT NOT NULL CHECK (poder >= 0),
    precision       INT NOT NULL CHECK (precision >= 0 AND precision <= 100),
    pp_max          INT NOT NULL CHECK (pp_max > 0)
);

-- Movimientos existentes en el código
INSERT INTO movimiento (nombre, tipo_id, categoria, poder, precision, pp_max) VALUES
    ('Placaje',       (SELECT id FROM tipo_elemento WHERE nombre='NORMAL'),    'FISICO',   35, 95,  35),
    ('Ascua',         (SELECT id FROM tipo_elemento WHERE nombre='FUEGO'),   'ESPECIAL', 40, 100, 25),
    ('Llamarada',     (SELECT id FROM tipo_elemento WHERE nombre='FUEGO'),   'ESPECIAL', 90, 85,   5),
    ('Pistola Agua',  (SELECT id FROM tipo_elemento WHERE nombre='AGUA'),    'ESPECIAL', 40, 100, 25),
    ('Hidrobomba',    (SELECT id FROM tipo_elemento WHERE nombre='AGUA'),    'ESPECIAL', 90, 80,   5),
    ('Hoja Afilada',  (SELECT id FROM tipo_elemento WHERE nombre='PLANTA'),  'FISICO',   55, 95,  25),
    ('Drenaje',       (SELECT id FROM tipo_elemento WHERE nombre='PLANTA'),  'ESPECIAL', 75, 100, 10),
    ('Impactrueno',   (SELECT id FROM tipo_elemento WHERE nombre='ELECTRICO'),'ESPECIAL',40, 100, 30),
    ('Rayo',          (SELECT id FROM tipo_elemento WHERE nombre='ELECTRICO'),'ESPECIAL',90, 100, 10),
    ('Ventisca',      (SELECT id FROM tipo_elemento WHERE nombre='HIELO'),    'ESPECIAL', 65, 90,  10),
    ('Terremoto',     (SELECT id FROM tipo_elemento WHERE nombre='TIERRA'),   'FISICO',   80, 100, 10),
    ('Trituracion',   (SELECT id FROM tipo_elemento WHERE nombre='NORMAL'),   'FISICO',   80, 100, 15);

-- =========================================================
-- 3. ESPECIES DE CRIATURAS
-- =========================================================
CREATE TABLE especie_criatura (
    id                  SERIAL PRIMARY KEY,
    nombre              VARCHAR(30) NOT NULL UNIQUE,
    tipo_id             INT NOT NULL REFERENCES tipo_elemento(id),
    hp_base             INT NOT NULL,
    ataque_base         INT NOT NULL,
    defensa_base        INT NOT NULL,
    velocidad_base      INT NOT NULL,
    tasa_captura        DECIMAL(3,2) NOT NULL CHECK (tasa_captura > 0 AND tasa_captura <= 1)
);

-- Datos extraídos de las clases concretas (Ignis, Aqua, etc.)
INSERT INTO especie_criatura (nombre, tipo_id, hp_base, ataque_base, defensa_base, velocidad_base, tasa_captura) VALUES
    ('Ignis',   (SELECT id FROM tipo_elemento WHERE nombre='FUEGO'),   39, 52, 43, 45, 0.30),
    ('Aqua',    (SELECT id FROM tipo_elemento WHERE nombre='AGUA'),    44, 48, 50, 43, 0.30),
    ('Herba',   (SELECT id FROM tipo_elemento WHERE nombre='PLANTA'),  41, 49, 48, 47, 0.30),
    ('Voltix',  (SELECT id FROM tipo_elemento WHERE nombre='ELECTRICO'),38, 55, 40, 50, 0.35),
    ('Fumeo',   (SELECT id FROM tipo_elemento WHERE nombre='NORMAL'),  50, 38, 45, 32, 0.35),
    ('Glacius', (SELECT id FROM tipo_elemento WHERE nombre='HIELO'),   42, 50, 45, 44, 0.28),
    ('Terron',  (SELECT id FROM tipo_elemento WHERE nombre='TIERRA'),  48, 44, 52, 30, 0.28),
    ('Gorgon',  (SELECT id FROM tipo_elemento WHERE nombre='PLANTA'),  45, 46, 42, 48, 0.30);

-- Movimientos que aprende cada especie por defecto
CREATE TABLE especie_movimiento (
    especie_id  INT NOT NULL REFERENCES especie_criatura(id) ON DELETE CASCADE,
    movimiento_id INT NOT NULL REFERENCES movimiento(id) ON DELETE CASCADE,
    slot        INT NOT NULL CHECK (slot BETWEEN 1 AND 4),   -- 1..4 como en el array
    minimo_nivel INT NOT NULL DEFAULT 1,                     -- nivel a partir del cual lo aprende
    PRIMARY KEY (especie_id, slot)
);

-- =========================================================
-- 4. CRIATURAS INDIVIDUALES
-- =========================================================
CREATE TABLE criatura (
    id                  SERIAL PRIMARY KEY,
    especie_id          INT NOT NULL REFERENCES especie_criatura(id),
    nivel               INT NOT NULL CHECK (nivel > 0),
    hp_actual           INT NOT NULL,
    hp_max              INT NOT NULL,
    ataque              INT NOT NULL,
    defensa             INT NOT NULL,
    velocidad           INT NOT NULL,
    exp_total           INT NOT NULL DEFAULT 0,
    exp_siguiente_nivel INT NOT NULL,
    -- Las criaturas siempre están ligadas a un entrenador (jugador o NPC)
    entrenador_id       INT REFERENCES entrenador(id) ON DELETE CASCADE,
    guardada_caja       BOOLEAN NOT NULL DEFAULT FALSE   -- TRUE si está en la caja de capturadas
);

CREATE TABLE criatura_movimiento (
    criatura_id INT NOT NULL REFERENCES criatura(id) ON DELETE CASCADE,
    movimiento_id INT NOT NULL REFERENCES movimiento(id) ON DELETE CASCADE,
    pp_actual   INT NOT NULL,
    slot        INT NOT NULL CHECK (slot BETWEEN 1 AND 4),
    PRIMARY KEY (criatura_id, slot)
);

-- =========================================================
-- 5. ENTRENADORES (clase base)
-- =========================================================
CREATE TABLE entrenador (
    id                      SERIAL PRIMARY KEY,
    nombre                  VARCHAR(30) NOT NULL,
    indice_criatura_activa  INT NOT NULL DEFAULT 0
    -- No FK a criatura porque la criatura activa se calcula en la app
);

-- =========================================================
-- 6. JUGADOR
-- =========================================================
CREATE TABLE jugador (
    id_entrenador   INT PRIMARY KEY REFERENCES entrenador(id) ON DELETE CASCADE,
    dinero          INT NOT NULL DEFAULT 500,
    medallas        INT NOT NULL DEFAULT 0
);

-- =========================================================
-- 7. NPC (Entrenador no jugable)
-- =========================================================
CREATE TABLE npc (
    id_entrenador   INT PRIMARY KEY REFERENCES entrenador(id) ON DELETE CASCADE,
    dialogo         TEXT NOT NULL,
    dificultad      VARCHAR(10) NOT NULL CHECK (dificultad IN ('FACIL','MEDIO','DIFICIL','EXPERTO')),
    recompensa      INT NOT NULL,
    derrotado       BOOLEAN NOT NULL DEFAULT FALSE
);

-- =========================================================
-- 8. OBJETOS DE CURACIÓN
-- =========================================================
CREATE TABLE objeto (
    id          SERIAL PRIMARY KEY,
    nombre      VARCHAR(30) NOT NULL UNIQUE,
    descripcion TEXT NOT NULL,
    cura        INT NOT NULL,           -- cantidad de HP que restaura
    precio      INT NOT NULL DEFAULT 0
);

INSERT INTO objeto (nombre, descripcion, cura, precio) VALUES
    ('Pocion',       'Restaura 20 HP', 20, 50),
    ('Super Pocion', 'Restaura 50 HP', 50, 120),
    ('Pocima Cura',  'Restaura 200 HP',200, 500);

-- Inventario del jugador (cantidad de cada objeto)
CREATE TABLE jugador_objeto (
    jugador_id INT NOT NULL REFERENCES jugador(id_entrenador) ON DELETE CASCADE,
    objeto_id  INT NOT NULL REFERENCES objeto(id) ON DELETE CASCADE,
    cantidad   INT NOT NULL CHECK (cantidad >= 0),
    PRIMARY KEY (jugador_id, objeto_id)
);

-- =========================================================
-- 9. ESFERAS
-- =========================================================
CREATE TABLE esfera (
    id          SERIAL PRIMARY KEY,
    nombre      VARCHAR(30) NOT NULL UNIQUE,
    tasa_base   DECIMAL(5,2) NOT NULL CHECK (tasa_base >= 0),
    precio      INT NOT NULL DEFAULT 0
);

INSERT INTO esfera (nombre, tasa_base, precio) VALUES
    ('Esfera Normal', 0.85, 100),
    ('Esfera HP',     1.20, 150),
    ('Esfera Tipo',   1.00, 200),
    ('Super Esfera',  1.50, 400),
    ('Ultra Esfera',  2.00, 800),
    ('Master Esfera', 5.00, 0);    -- no se compra

-- Inventario de esferas (el jugador las tiene como ítems apilables)
CREATE TABLE jugador_esfera (
    jugador_id INT NOT NULL REFERENCES jugador(id_entrenador) ON DELETE CASCADE,
    esfera_id  INT NOT NULL REFERENCES esfera(id) ON DELETE CASCADE,
    cantidad   INT NOT NULL CHECK (cantidad >= 0),
    PRIMARY KEY (jugador_id, esfera_id)
);

-- =========================================================
-- 10. BATALLAS
-- =========================================================
CREATE TABLE batalla (
    id                  SERIAL PRIMARY KEY,
    jugador_id          INT NOT NULL REFERENCES jugador(id_entrenador) ON DELETE CASCADE,
    npc_id              INT REFERENCES npc(id_entrenador) ON DELETE CASCADE,
    criatura_salvaje_id INT REFERENCES criatura(id) ON DELETE CASCADE,
    es_encuentro_salvaje BOOLEAN NOT NULL,
    turno_actual        INT NOT NULL DEFAULT 1,
    activa              BOOLEAN NOT NULL DEFAULT TRUE,
    ganador             VARCHAR(30),
    CHECK ( (es_encuentro_salvaje = TRUE  AND npc_id IS NULL) OR
            (es_encuentro_salvaje = FALSE AND npc_id IS NOT NULL) )
);

-- =========================================================
-- 11. SESIÓN DE JUEGO (una por partida guardada)
-- =========================================================
CREATE TABLE sesion_juego (
    id                  SERIAL PRIMARY KEY,
    jugador_id          INT NOT NULL REFERENCES jugador(id_entrenador) ON DELETE CASCADE,
    fase                VARCHAR(15) NOT NULL CHECK (fase IN ('INICIO','MAPA','BATALLA','JUEGO_TERMINADO')),
    zona                INT NOT NULL DEFAULT 1,
    siguiente_npc_index INT NOT NULL DEFAULT 0,
    puedes_curar        BOOLEAN NOT NULL DEFAULT TRUE,
    batalla_actual_id   INT REFERENCES batalla(id) ON DELETE SET NULL,
    fecha_guardado      TIMESTAMP NOT NULL DEFAULT NOW()
);

-- =========================================================
-- ÍNDICES ÚTILES
-- =========================================================
CREATE INDEX idx_criatura_entrenador ON criatura(entrenador_id);
CREATE INDEX idx_batalla_jugador ON batalla(jugador_id);
CREATE INDEX idx_sesion_jugador ON sesion_juego(jugador_id);
