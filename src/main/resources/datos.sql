-- Inserción en la tabla usuario
INSERT INTO usuario (nombre, email, contrasena, rol, telefono) VALUES
('Leandro', 'leandro@example.com', '1234', 'ADMIN', '123456789'),
('Maria', 'maria@example.com', '1234', 'USER', '987654321');

-- Inserción en la tabla vehiculo
INSERT INTO vehiculo (id_usuario, marca, modelo, matricula) VALUES
(1, 'Toyota', 'Corolla', 'ABC1234'),
(2, 'Honda', 'Civic', 'XYZ5678');

-- Inserción en la tabla servicio
INSERT INTO servicio (nombre, descripcion, precio) VALUES
('LAVADO_ASPIRADO_COCHE_PEQUENO', 'Lavado y aspirado para coche pequeño (Duración: 1h)', 30.00),
('LAVADO_ASPIRADO_CAMIONETA', 'Lavado y aspirado para camioneta (Duración: 1h 15min)', 40.00),
('LAVADO_TAPICERIA_COCHE_PEQUENO', 'Lavado de tapicería para coche pequeño (Duración: 3h 30min)', 100.00),
('PULIDO_FAROS', 'Pulido de faros para mejorar la visibilidad (Duración: 2h)', 50.00),
('DETALLADO_COMPLETO', 'Detallado interior, exterior, ruedas y motor (Duración: 3h 30min)', 130.00),
('TRATAMIENTO_CERAMICO', 'Tratamiento cerámico + detallado completo + motor (Duración: 5h 30min)', 260.00);


-- Inserción en la tabla cita
-- Primero las citas, id_pago puede ser 0 como valor temporal
INSERT INTO cita (id_vehiculo, id_servicio, id_pago, fecha, hora, estado)
VALUES
(1, 1, 0, '2025-03-26', '09:00:00', 'PAGADA'),
(2, 2, 0, '2025-03-27', '11:00:00', 'PAGADA');

-- Inserción en la tabla pago
-- Relacionamos los pagos con las citas
INSERT INTO pago (id_cita, monto, metodo_pago)
VALUES
(1, 30.00, 'TARJETA'),
(2, 35.24, 'EFECTIVO');

-- Actualización de citas para asociar los pagos creados
UPDATE cita SET id_pago = 1 WHERE id = 1;
UPDATE cita SET id_pago = 2 WHERE id = 2;

-- Inserción en la tabla servicita
INSERT INTO servicita (id_servicio, id_cita, comentario, valoracion) VALUES
(1, 1, 'Excelente servicio, muy rápido.', 3),
(2, 2, 'Buen trabajo, pero podrían mejorar.', 4.5);

