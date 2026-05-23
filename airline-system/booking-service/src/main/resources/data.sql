-- Creamos la tabla de reservas para el microservicio
CREATE TABLE IF NOT EXISTS bookings (
    id UUID PRIMARY KEY,
    flight_number VARCHAR(20) NOT NULL,
    passenger_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL,
    seats INT NOT NULL
    );

-- Opcional: Alguna reserva de prueba para ver datos al arrancar
INSERT INTO bookings (id, flight_number, passenger_id, status,seats)
 VALUES ('550e8400-e29b-41d4-a716-446655440000', 'IB3040', 1, 'PENDING',2);