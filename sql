-- ==========================================================
-- BASE DE DATOS: sistema_coworking
-- Generado a partir de los DTOs del paquete org.coworking.domain.dto
-- Compatible con MariaDB
-- ==========================================================
DROP DATABASE if EXISTS sistema_coworking;
CREATE DATABASE IF NOT EXISTS sistema_coworking;
USE sistema_coworking;

-- ==========================================================
-- TABLA: USUARIO
-- ==========================================================
CREATE TABLE usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    telefono VARCHAR(20),
    rol ENUM('ADMIN', 'MIEMBRO', 'VISITANTE') NOT NULL,
    estado BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ==========================================================
-- TABLA: RECURSO
-- ==========================================================
CREATE TABLE recurso (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    tipo VARCHAR(100) NOT NULL,
    ubicacion VARCHAR(150),
    capacidad INT NOT NULL,
    precio_por_hora DECIMAL(10,2) NOT NULL,
    estado ENUM('DISPONIBLE', 'OCUPADO', 'MANTENIMIENTO') DEFAULT 'DISPONIBLE',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ==========================================================
-- TABLA: SUSCRIPCION
-- ==========================================================
CREATE TABLE suscripcion (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    tipo ENUM('BASICO', 'PREMIUM') NOT NULL,
    estado ENUM('ACTIVA', 'VENCIDA', 'CANCELADA') NOT NULL,
    precio_mensual DECIMAL(10,2) NOT NULL,
    fecha_inicio DATETIME NOT NULL,
    fecha_fin DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- ==========================================================
-- TABLA: RECURSO vs RESERVA
-- ==========================================================
CREATE TABLE reserva (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    recurso_id BIGINT NOT NULL,
    fecha_inicio DATETIME NOT NULL,
    fecha_fin DATETIME NOT NULL,
    estado ENUM('PENDIENTE', 'ACTIVA', 'CANCELADA', 'COMPLETADA') NOT NULL,
    notas TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (recurso_id) REFERENCES recurso(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- ==========================================================
-- TABLA: PAGO
-- ==========================================================
CREATE TABLE pago (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    reserva_id BIGINT NOT NULL UNIQUE,
    usuario_id BIGINT NOT NULL,
    descripcion TEXT,
    monto DECIMAL(10,2) NOT NULL,
    metodo_pago ENUM('EFECTIVO', 'TARJETA_CREDITO', 'TARJETA_DEBITO', 'TRANSFERENCIA', 'PAYPAL') NOT NULL,
    estado ENUM('PENDIENTE', 'COMPLETADO', 'FALLIDO', 'REEMBOLSADO') NOT NULL,
    referencia_externa VARCHAR(150),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (reserva_id) REFERENCES reserva(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- ==========================================================
-- TABLA: NOTIFICACION
-- ==========================================================
CREATE TABLE notificacion (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    tipo ENUM('CONFIRMACION', 'RECORDATORIO', 'CANCELACION') NOT NULL,
    titulo VARCHAR(150) NOT NULL,
    mensaje TEXT NOT NULL,
    estado ENUM('PENDIENTE', 'ENVIADA', 'FALLIDA', 'LEIDA') NOT NULL DEFAULT 'PENDIENTE',
    fecha_envio DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- ==========================================================
-- TABLA: REPORTE
-- ==========================================================
CREATE TABLE reporte (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    titulo VARCHAR(255) NOT NULL,
    descripcion TEXT,
    tipo ENUM('USO_RECURSOS', 'INGRESOS', 'OCUPACION', 'USUARIOS') NOT NULL,
    cantidad_registros INT DEFAULT 0,
    contenido TEXT NOT NULL,
    fecha_generacion DATETIME NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- ==========================================================
-- RELACIONES ENTRE TABLAS
-- usuario (1) → suscripcion (N)
-- usuario (1) → reserva (N)
-- usuario (1) → pago (N)
-- usuario (1) → notificacion (N)
-- usuario (1) → reporte (N)
-- recurso (1) → reserva (N)
-- reserva (1) → pago (1)
-- ==========================================================


USE sistema_coworking;

-- ==========================================================
-- USUARIOS
-- ==========================================================
INSERT INTO usuario (nombre, email, telefono, rol, estado)
VALUES 
('Juan Pérez', 'juan.perez@email.com', '+57 300 111 2233', 'MIEMBRO', TRUE),
('María Gómez', 'maria.gomez@email.com', '+57 301 222 3344', 'ADMIN', TRUE),
('Carlos Ruiz', 'carlos.ruiz@email.com', '+57 302 333 4455', 'VISITANTE', FALSE),
('Carlos Gómez', 'carlosgomez@email.com', '3201112233', 'VISITANTE', TRUE),
('Mariana Torres', 'marianatorres@email.com', '3112223344', 'MIEMBRO', TRUE),
('Andrés López', 'andreslopez@email.com', '3005556677', 'VISITANTE', FALSE),
('Valentina Ruiz', 'valenruiz@email.com', '3019998888', 'MIEMBRO', TRUE),
('Juan Herrera', 'juanherrera@email.com', '3124445566', 'MIEMBRO', FALSE),
('Sofía Martínez', 'sofia.martinez@email.com', '3177772233', 'VISITANTE', TRUE),
('Pedro Sánchez', 'pedrosanchez@email.com', '3168883344', 'MIEMBRO', TRUE),
('Laura Ríos', 'laura.rios@email.com', '3145557788', 'MIEMBRO', FALSE),
('Camilo Díaz', 'camilodiaz@email.com', '3102229933', 'MIEMBRO', TRUE),
('Natalia Pérez', 'nataliaperez@email.com', '3134449911', 'MIEMBRO', TRUE);


-- ==========================================================
-- RECURSOS
-- ==========================================================
INSERT INTO recurso (nombre, descripcion, tipo, ubicacion, capacidad, precio_por_hora, estado)
VALUES
('Sala de Reuniones A', 'Sala equipada con proyector y pizarra', 'SALA_REUNION', 'Piso 2, Oficina 203', 10, 50.00, 'DISPONIBLE'),
('Oficina Privada 1', 'Espacio cerrado para trabajo individual', 'OFICINA', 'Piso 1, Oficina 101', 2, 30.00, 'DISPONIBLE'),
('Auditorio Principal', 'Espacio amplio con capacidad para 50 personas', 'AUDITORIO', 'Piso 3', 50, 120.00, 'MANTENIMIENTO');

-- ==========================================================
-- SUSCRIPCIONES
-- ==========================================================
INSERT INTO suscripcion (usuario_id, tipo, estado, precio_mensual, fecha_inicio, fecha_fin)
VALUES
(1, 'PREMIUM', 'ACTIVA', 120000.00, '2025-01-01 00:00:00', '2025-12-31 23:59:59'),
(2, 'BASICO', 'ACTIVA', 60000.00, '2025-02-01 00:00:00', '2026-01-31 23:59:59'),
(3, 'BASICO', 'VENCIDA', 60000.00, '2024-01-01 00:00:00', '2024-12-31 23:59:59');

-- ==========================================================
-- RESERVAS
-- ==========================================================
INSERT INTO reserva (usuario_id, recurso_id, fecha_inicio, fecha_fin, estado, notas)
VALUES
(1, 1, '2025-10-15 09:00:00', '2025-10-15 17:00:00', 'ACTIVA', 'Reunión de equipo comercial'),
(2, 2, '2025-10-20 08:00:00', '2025-10-20 12:00:00', 'PENDIENTE', 'Sesión de capacitación'),
(1, 3, '2025-10-25 10:00:00', '2025-10-25 18:00:00', 'CANCELADA', 'Evento de networking');

-- ==========================================================
-- PAGOS
-- ==========================================================
INSERT INTO pago (reserva_id, usuario_id, descripcion, monto, metodo_pago, estado, referencia_externa)
VALUES
(1, 1, 'Pago por reserva de Sala de Reuniones A', 400000.00, 'TARJETA_CREDITO', 'COMPLETADO', 'TXN-20251015001'),
(2, 2, 'Pago por reserva de Oficina Privada 1', 120000.00, 'TRANSFERENCIA', 'PENDIENTE', 'TXN-20251020002');

-- ==========================================================
-- NOTIFICACIONES
-- ==========================================================
INSERT INTO notificacion (usuario_id, tipo, titulo, mensaje, estado, fecha_envio)
VALUES
(1, 'CONFIRMACION', 'Reserva Confirmada', 'Su reserva de la Sala de Reuniones A ha sido confirmada.', 'ENVIADA', '2025-10-14 08:00:00'),
(2, 'RECORDATORIO', 'Recuerde su reserva', 'Tiene una reserva programada para el 20 de octubre.', 'PENDIENTE', NULL),
(1, 'CANCELACION', 'Reserva Cancelada', 'Su reserva del Auditorio Principal ha sido cancelada.', 'ENVIADA', '2025-10-10 09:00:00');

-- ==========================================================
-- REPORTES
-- ==========================================================
INSERT INTO reporte (usuario_id, titulo, descripcion, tipo, cantidad_registros, contenido, fecha_generacion)
VALUES
(2, 'Reporte de Uso de Recursos - Octubre 2025', 'Estadísticas de ocupación de recursos durante octubre.', 'USO_RECURSOS', 120, '{"ocupacionPromedio":85}', '2025-10-31 23:59:59'),
(1, 'Reporte de Ingresos Mensuales', 'Detalle de ingresos por reservas y pagos realizados.', 'INGRESOS', 45, '{"totalIngresos":520000}', '2025-10-31 23:59:59');

SELECT * FROM usuario;
