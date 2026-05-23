-- Usuarios adicionales para pruebas de seguridad
INSERT INTO users (username, password) VALUES ('jose_arquitecto', '$2a$12$41m/m45W1h6fCB.fwziG2enauLiX8gaaj5Dk3HtIcGjq8x9zs6b2q'); -- pass: 1234
INSERT INTO users (username, password) VALUES ('test_user_3', '$2a$12$41m/m45W1h6fCB.fwziG2enauLiX8gaaj5Dk3HtIcGjq8x9zs6b2q');

-- Asignar roles (1: USER, 2: ADMIN)
INSERT INTO users_roles (user_id, role_id) VALUES (3, 2); -- Jose es ADMIN
INSERT INTO users_roles (user_id, role_id) VALUES (4, 1); -- Test user es USER
