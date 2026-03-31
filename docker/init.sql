CREATE TABLE IF NOT EXISTS franquicias (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS sucursales (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    franquicia_id BIGINT REFERENCES franquicias(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS productos (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    stock INTEGER NOT NULL DEFAULT 0,
    sucursal_id BIGINT REFERENCES sucursales(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_sucursales_franquicia ON sucursales(franquicia_id);
CREATE INDEX IF NOT EXISTS idx_productos_sucursal ON productos(sucursal_id);
