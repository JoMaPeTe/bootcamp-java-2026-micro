-- Limpiamos por si reiniciamos (útil en dev)
TRUNCATE TABLE flights RESTART IDENTITY;

-- Insertamos 3 vuelos de prueba para nuestro MVP
INSERT INTO flights (flight_number, origin, destination, departure_time, price, available_seats)
VALUES
    ('IB3040', 'MAD', 'BOG', '2026-06-15 23:55:00', 450.00, 200),
    ('AV123', 'BOG', 'MAD', '2026-06-20 18:30:00', 500.00, 150),
    ('FR098', 'MAD', 'BCN', '2026-05-20 08:00:00', 50.00, 180);