-- Crear la base de datos si no existe y usarla
CREATE DATABASE IF NOT EXISTS appRecetas;
USE appRecetas;

-- Crear la tabla de usuarios
CREATE TABLE IF NOT EXISTS usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(64) NOT NULL
);

-- Insertar usuario de prueba con contraseña hasheada previamente
INSERT INTO usuarios (email, password) VALUES
('prueba@gmail.com', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd1c54ca5f9a6a0ab58');

-- Crear la tabla de recetas
CREATE TABLE IF NOT EXISTS recetas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    userId INT NOT NULL,
    name VARCHAR(500) NOT NULL,
    descripcion VARCHAR(500) NOT NULL,
    ingredientes VARCHAR(500) NOT NULL,
    calories VARCHAR(500) NOT NULL
);

-- Insertar recetas de prueba
INSERT INTO recetas (userId, name, descripcion, ingredientes, calories) VALUES
(1, 'paella', 'Paella con mariscos y vegetales.', 'Arroz, mariscos, vegetales', '400 kcal'),
(1, 'Risotto de Setas', 'Arroz cremoso con setas y queso parmesano.', 'Arroz, setas, caldo, parmesano, vino blanco', '500 kcal');
