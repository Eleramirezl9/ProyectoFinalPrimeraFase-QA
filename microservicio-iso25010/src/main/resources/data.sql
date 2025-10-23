-- =====================================================
-- DATOS INICIALES PARA MICROSERVICIO ISO/IEC 25010
-- CON AUTENTICACIÓN Y AUTORIZACIÓN
-- =====================================================
-- Este archivo contiene datos de prueba para poblar la base de datos
-- Se ejecuta automáticamente al iniciar la aplicación
-- Codificación: UTF-8 para soporte completo de caracteres especiales

-- Configuración inicial para UTF-8
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

-- =====================================================
-- PERMISOS (Granular Permissions)
-- =====================================================

INSERT INTO permissions (name, description) VALUES
('USUARIO_READ', 'Leer información de usuarios'),
('USUARIO_CREATE', 'Crear nuevos usuarios'),
('USUARIO_UPDATE', 'Actualizar información de usuarios'),
('USUARIO_DELETE', 'Eliminar usuarios'),
('PRODUCTO_READ', 'Leer información de productos'),
('PRODUCTO_CREATE', 'Crear nuevos productos'),
('PRODUCTO_UPDATE', 'Actualizar información de productos'),
('PRODUCTO_DELETE', 'Eliminar productos'),
('PEDIDO_READ', 'Leer información de pedidos'),
('PEDIDO_CREATE', 'Crear nuevos pedidos'),
('PEDIDO_UPDATE', 'Actualizar estado de pedidos'),
('PEDIDO_DELETE', 'Eliminar pedidos');

-- =====================================================
-- ROLES (User Roles)
-- =====================================================

INSERT INTO roles (name, description) VALUES
('ADMIN', 'Administrador con acceso total al sistema'),
('MANAGER', 'Gestor con permisos de administración limitada'),
('CLIENTE', 'Cliente con permisos para consultar y realizar pedidos');

-- =====================================================
-- ASIGNACIÓN ROLES -> PERMISOS (role_permissions)
-- =====================================================

-- ADMIN: Todos los permisos
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.name = 'ADMIN';

-- MANAGER: Permisos de lectura y escritura, sin eliminación
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.name = 'MANAGER'
  AND p.name IN (
    'USUARIO_READ', 'USUARIO_CREATE', 'USUARIO_UPDATE',
    'PRODUCTO_READ', 'PRODUCTO_CREATE', 'PRODUCTO_UPDATE',
    'PEDIDO_READ', 'PEDIDO_CREATE', 'PEDIDO_UPDATE'
);

-- CLIENTE: Solo lectura y creación de pedidos
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.name = 'CLIENTE'
  AND p.name IN (
    'USUARIO_READ',
    'PRODUCTO_READ',
    'PEDIDO_READ', 'PEDIDO_CREATE'
);

-- =====================================================
-- USUARIOS CON AUTENTICACIÓN
-- =====================================================
-- Passwords (todos son "password123")
-- Hash BCrypt generado con: new BCryptPasswordEncoder().encode("password123")
-- Nota: El hash $2a$10$N9qo8uLOickgx2ZMRZoMyeJJ6/lKTCrFzFTQkNbLZNvN6dJkZGj6e corresponde a "password123"

INSERT INTO usuarios (nombre, apellido, username, email, password, telefono, activo, cuenta_no_expirada, cuenta_no_bloqueada, credenciales_no_expiradas, fecha_creacion) VALUES
-- Administradores
('Admin', 'Sistema', 'admin', 'admin@sistema.com', '$2a$10$Wk6.xosJh4tgCWsZ28nHYuytrAUWHrbKT2CLya1DjPXz7wnwJnMfy', '50212345678', true, true, true, true, DATEADD('DAY', -90, CURRENT_TIMESTAMP)),
('Super', 'Admin', 'superadmin', 'superadmin@sistema.com', '$2a$10$Wk6.xosJh4tgCWsZ28nHYuytrAUWHrbKT2CLya1DjPXz7wnwJnMfy', '50298765432', true, true, true, true, DATEADD('DAY', -85, CURRENT_TIMESTAMP)),

-- Gestores/Managers
('María Elena', 'Rodríguez Pérez', 'mrodriguez', 'maria.rodriguez@empresa.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeJJ6/lKTCrFzFTQkNbLZNvN6dJkZGj6e', '50234567890', true, true, true, true, DATEADD('DAY', -60, CURRENT_TIMESTAMP)),
('Carlos Alberto', 'Martínez Silva', 'cmartinez', 'carlos.martinez@empresa.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeJJ6/lKTCrFzFTQkNbLZNvN6dJkZGj6e', '50287654321', true, true, true, true, DATEADD('DAY', -55, CURRENT_TIMESTAMP)),

-- Usuarios regulares
('Juan Carlos', 'García López', 'jgarcia', 'juan.garcia@email.com', '$2a$10$Wk6.xosJh4tgCWsZ28nHYuytrAUWHrbKT2CLya1DjPXz7wnwJnMfy', '50245678901', true, true, true, true, DATEADD('DAY', -30, CURRENT_TIMESTAMP)),
('Ana Sofía', 'López Hernández', 'alopez', 'ana.lopez@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeJJ6/lKTCrFzFTQkNbLZNvN6dJkZGj6e', '50276543210', true, true, true, true, DATEADD('DAY', -25, CURRENT_TIMESTAMP)),
('Luis Fernando', 'Morales Castro', 'lmorales', 'luis.morales@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeJJ6/lKTCrFzFTQkNbLZNvN6dJkZGj6e', '50256789012', true, true, true, true, DATEADD('DAY', -20, CURRENT_TIMESTAMP)),
('Patricia Isabel', 'Vásquez Ruiz', 'pvasquez', 'patricia.vasquez@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeJJ6/lKTCrFzFTQkNbLZNvN6dJkZGj6e', '50265432109', true, true, true, true, DATEADD('DAY', -18, CURRENT_TIMESTAMP)),
('Roberto Miguel', 'Jiménez Torres', 'rjimenez', 'roberto.jimenez@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeJJ6/lKTCrFzFTQkNbLZNvN6dJkZGj6e', '50267890123', true, true, true, true, DATEADD('DAY', -15, CURRENT_TIMESTAMP)),
('Carmen Rosa', 'Flores Mendoza', 'cflores', 'carmen.flores@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeJJ6/lKTCrFzFTQkNbLZNvN6dJkZGj6e', '50254321098', true, true, true, true, DATEADD('DAY', -12, CURRENT_TIMESTAMP)),

-- Usuario inactivo (para testing)
('Usuario', 'Inactivo', 'inactivo', 'inactivo@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeJJ6/lKTCrFzFTQkNbLZNvN6dJkZGj6e', '50200000000', false, true, true, true, DATEADD('DAY', -100, CURRENT_TIMESTAMP));

-- =====================================================
-- ASIGNACIÓN USUARIOS -> ROLES (usuario_roles)
-- =====================================================

-- Asignar rol ADMIN
INSERT INTO usuario_roles (usuario_id, role_id)
SELECT u.id, r.id
FROM usuarios u
CROSS JOIN roles r
WHERE u.username IN ('admin', 'superadmin')
  AND r.name = 'ADMIN';

-- Asignar rol MANAGER
INSERT INTO usuario_roles (usuario_id, role_id)
SELECT u.id, r.id
FROM usuarios u
CROSS JOIN roles r
WHERE u.username IN ('mrodriguez', 'cmartinez')
  AND r.name = 'MANAGER';

-- Asignar rol CLIENTE a clientes regulares
INSERT INTO usuario_roles (usuario_id, role_id)
SELECT u.id, r.id
FROM usuarios u
CROSS JOIN roles r
WHERE u.username IN ('jgarcia', 'alopez', 'lmorales', 'pvasquez', 'rjimenez', 'cflores', 'inactivo')
  AND r.name = 'CLIENTE';

-- =====================================================
-- PRODUCTOS DE PRUEBA
-- =====================================================

INSERT INTO productos (nombre, descripcion, precio, stock, categoria, marca, activo, fecha_creacion) VALUES
('Smartphone Samsung Galaxy A54', 'Teléfono inteligente con pantalla AMOLED de 6.4 pulgadas, cámara triple de 50MP, 128GB de almacenamiento y 6GB de RAM', 2499.99, 25, 'Electrónicos', 'Samsung', true, DATEADD('DAY', -45, CURRENT_TIMESTAMP)),
('Laptop Dell Inspiron 15', 'Laptop con procesador Intel Core i5, 8GB RAM, 512GB SSD, pantalla de 15.6 pulgadas Full HD', 4299.99, 15, 'Electrónicos', 'Dell', true, DATEADD('DAY', -40, CURRENT_TIMESTAMP)),
('Tablet iPad Air', 'Tablet Apple con chip M1, pantalla Liquid Retina de 10.9 pulgadas, 64GB de almacenamiento', 3899.99, 12, 'Electrónicos', 'Apple', true, DATEADD('DAY', -35, CURRENT_TIMESTAMP)),
('Auriculares Sony WH-1000XM4', 'Auriculares inalámbricos con cancelación de ruido, batería de 30 horas, Bluetooth 5.0', 1899.99, 30, 'Electrónicos', 'Sony', true, DATEADD('DAY', -32, CURRENT_TIMESTAMP)),
('Smart TV LG 55 pulgadas', 'Televisor 4K UHD con webOS, HDR10, Dolby Vision, Wi-Fi integrado', 5999.99, 8, 'Electrónicos', 'LG', true, DATEADD('DAY', -28, CURRENT_TIMESTAMP)),
('Cafetera Nespresso Vertuo', 'Cafetera de cápsulas con tecnología Centrifusion, prepara café y espresso', 1299.99, 20, 'Hogar y Jardín', 'Nespresso', true, DATEADD('DAY', -26, CURRENT_TIMESTAMP)),
('Aspiradora Dyson V11', 'Aspiradora inalámbrica con tecnología ciclónica, batería de 60 minutos', 2799.99, 10, 'Hogar y Jardín', 'Dyson', true, DATEADD('DAY', -24, CURRENT_TIMESTAMP)),
('Microondas Panasonic 1.2 cu ft', 'Horno microondas con tecnología inverter, 1200W de potencia, panel digital', 899.99, 18, 'Hogar y Jardín', 'Panasonic', true, DATEADD('DAY', -22, CURRENT_TIMESTAMP)),
('Juego de Sartenes Tefal', 'Set de 3 sartenes antiadherentes con recubrimiento titanium, aptas para inducción', 599.99, 25, 'Hogar y Jardín', 'Tefal', true, DATEADD('DAY', -20, CURRENT_TIMESTAMP)),
('Purificador de Aire Xiaomi', 'Purificador con filtro HEPA, cobertura de 48m², control por app móvil', 1199.99, 15, 'Hogar y Jardín', 'Xiaomi', true, DATEADD('DAY', -18, CURRENT_TIMESTAMP)),
('Bicicleta de Montaña Trek', 'Bicicleta MTB con marco de aluminio, suspensión delantera, 21 velocidades', 3499.99, 6, 'Deportes y Fitness', 'Trek', true, DATEADD('DAY', -16, CURRENT_TIMESTAMP)),
('Banda Elástica de Ejercicio', 'Set de 5 bandas de resistencia con diferentes niveles, incluye accesorios', 299.99, 40, 'Deportes y Fitness', 'Fitness Pro', true, DATEADD('DAY', -14, CURRENT_TIMESTAMP)),
('Reloj Deportivo Garmin', 'Smartwatch con GPS, monitor de frecuencia cardíaca, resistente al agua', 1999.99, 22, 'Deportes y Fitness', 'Garmin', true, DATEADD('DAY', -12, CURRENT_TIMESTAMP)),
('Mancuernas Ajustables', 'Par de mancuernas con peso ajustable de 2.5kg a 25kg cada una', 899.99, 12, 'Deportes y Fitness', 'PowerBlock', true, DATEADD('DAY', -10, CURRENT_TIMESTAMP)),
('Esterilla de Yoga Premium', 'Esterilla antideslizante de 6mm de grosor, material eco-friendly', 199.99, 35, 'Deportes y Fitness', 'Manduka', true, DATEADD('DAY', -8, CURRENT_TIMESTAMP)),
('Libro: Clean Code', 'Guía completa para escribir código limpio y mantenible por Robert C. Martin', 299.99, 50, 'Libros y Educación', 'Prentice Hall', true, DATEADD('DAY', -6, CURRENT_TIMESTAMP)),
('Curso Online: Spring Boot Masterclass', 'Curso completo de desarrollo con Spring Boot, incluye certificado', 499.99, 100, 'Libros y Educación', 'TechAcademy', true, DATEADD('DAY', -4, CURRENT_TIMESTAMP)),
('Calculadora Científica Casio', 'Calculadora programable con pantalla gráfica, ideal para ingeniería', 399.99, 28, 'Libros y Educación', 'Casio', true, DATEADD('DAY', -2, CURRENT_TIMESTAMP)),
('Monitor Gaming 27 pulgadas', 'Monitor curvo con resolución 2K, 144Hz, tiempo de respuesta 1ms', 1899.99, 5, 'Electrónicos', 'ASUS', true, DATEADD('DAY', -1, CURRENT_TIMESTAMP)),
('Silla Ergonómica de Oficina', 'Silla con soporte lumbar, reposabrazos ajustables, base de aluminio', 1299.99, 8, 'Hogar y Jardín', 'Herman Miller', true, DATEADD('HOUR', -3, CURRENT_TIMESTAMP));

-- =====================================================
-- PEDIDOS DE PRUEBA
-- =====================================================

-- Pedidos pendientes
INSERT INTO pedidos (usuario_id, producto_id, cantidad, precio_unitario, total, estado, observaciones, fecha_pedido) VALUES
(5, 1, 2, 2499.99, 4999.98, 'PENDIENTE', 'Pedido para regalo de cumpleaños', DATEADD('HOUR', -6, CURRENT_TIMESTAMP)),
(6, 5, 1, 5999.99, 5999.99, 'PENDIENTE', 'Entrega preferiblemente en horario matutino', DATEADD('HOUR', -12, CURRENT_TIMESTAMP)),
(7, 12, 1, 299.99, 299.99, 'PENDIENTE', NULL, DATEADD('DAY', -1, CURRENT_TIMESTAMP));

-- Pedidos confirmados
INSERT INTO pedidos (usuario_id, producto_id, cantidad, precio_unitario, total, estado, observaciones, fecha_pedido, fecha_actualizacion) VALUES
(8, 2, 1, 4299.99, 4299.99, 'CONFIRMADO', 'Laptop para trabajo remoto', DATEADD('DAY', -3, CURRENT_TIMESTAMP), DATEADD('DAY', -2, CURRENT_TIMESTAMP)),
(9, 7, 1, 2799.99, 2799.99, 'CONFIRMADO', 'Aspiradora para casa nueva', DATEADD('DAY', -2, CURRENT_TIMESTAMP), DATEADD('HOUR', -18, CURRENT_TIMESTAMP)),
(5, 15, 3, 199.99, 599.97, 'CONFIRMADO', 'Esterillas para clases de yoga', DATEADD('HOUR', -30, CURRENT_TIMESTAMP), DATEADD('HOUR', -8, CURRENT_TIMESTAMP));

-- Pedidos en proceso
INSERT INTO pedidos (usuario_id, producto_id, cantidad, precio_unitario, total, estado, observaciones, fecha_pedido, fecha_actualizacion) VALUES
(6, 4, 2, 1899.99, 3799.98, 'EN_PROCESO', 'Auriculares para oficina', DATEADD('DAY', -4, CURRENT_TIMESTAMP), DATEADD('DAY', -3, CURRENT_TIMESTAMP)),
(7, 9, 1, 599.99, 599.99, 'EN_PROCESO', NULL, DATEADD('DAY', -3, CURRENT_TIMESTAMP), DATEADD('DAY', -2, CURRENT_TIMESTAMP));

-- Pedidos enviados
INSERT INTO pedidos (usuario_id, producto_id, cantidad, precio_unitario, total, estado, observaciones, fecha_pedido, fecha_actualizacion) VALUES
(8, 13, 1, 1999.99, 1999.99, 'ENVIADO', 'Reloj para maratón', DATEADD('DAY', -5, CURRENT_TIMESTAMP), DATEADD('DAY', -4, CURRENT_TIMESTAMP)),
(9, 16, 1, 299.99, 299.99, 'ENVIADO', 'Libro de programación', DATEADD('DAY', -4, CURRENT_TIMESTAMP), DATEADD('DAY', -3, CURRENT_TIMESTAMP));

-- Pedidos entregados
INSERT INTO pedidos (usuario_id, producto_id, cantidad, precio_unitario, total, estado, observaciones, fecha_pedido, fecha_entrega, fecha_actualizacion) VALUES
(10, 6, 1, 1299.99, 1299.99, 'ENTREGADO', 'Cafetera entregada exitosamente', DATEADD('DAY', -8, CURRENT_TIMESTAMP), DATEADD('DAY', -6, CURRENT_TIMESTAMP), DATEADD('DAY', -6, CURRENT_TIMESTAMP)),
(5, 11, 1, 3499.99, 3499.99, 'ENTREGADO', 'Bicicleta en perfecto estado', DATEADD('DAY', -12, CURRENT_TIMESTAMP), DATEADD('DAY', -9, CURRENT_TIMESTAMP), DATEADD('DAY', -9, CURRENT_TIMESTAMP)),
(7, 14, 2, 899.99, 1799.98, 'ENTREGADO', 'Mancuernas para gimnasio casero', DATEADD('DAY', -7, CURRENT_TIMESTAMP), DATEADD('DAY', -5, CURRENT_TIMESTAMP), DATEADD('DAY', -5, CURRENT_TIMESTAMP)),
(8, 18, 1, 399.99, 399.99, 'ENTREGADO', NULL, DATEADD('DAY', -16, CURRENT_TIMESTAMP), DATEADD('DAY', -13, CURRENT_TIMESTAMP), DATEADD('DAY', -13, CURRENT_TIMESTAMP));

-- Pedidos cancelados
INSERT INTO pedidos (usuario_id, producto_id, cantidad, precio_unitario, total, estado, observaciones, fecha_pedido, fecha_actualizacion) VALUES
(9, 3, 1, 3899.99, 3899.99, 'CANCELADO', 'Cliente cambió de opinión', DATEADD('DAY', -6, CURRENT_TIMESTAMP), DATEADD('DAY', -5, CURRENT_TIMESTAMP)),
(5, 8, 1, 899.99, 899.99, 'CANCELADO', 'Producto no disponible en el color solicitado', DATEADD('DAY', -9, CURRENT_TIMESTAMP), DATEADD('DAY', -8, CURRENT_TIMESTAMP));

-- =====================================================
-- INFORMACIÓN DE CREDENCIALES DE PRUEBA
-- =====================================================

-- ✅ USUARIOS DISPONIBLES:
--
-- 🔴 ADMINISTRADORES (Rol: ADMIN):
--    Username: admin          | Password: password123
--    Username: superadmin     | Password: password123
--
-- 🟡 GESTORES (Rol: MANAGER):
--    Username: mrodriguez     | Password: password123
--    Username: cmartinez      | Password: password123
--
-- 🟢 CLIENTES (Rol: CLIENTE):
--    Username: jgarcia        | Password: password123
--    Username: alopez         | Password: password123
--    Username: lmorales       | Password: password123
--    Username: pvasquez       | Password: password123
--    Username: rjimenez       | Password: password123
--    Username: cflores        | Password: password123
--
-- ⚫ USUARIO INACTIVO (para testing):
--    Username: inactivo       | Password: password123 (cuenta deshabilitada)
--
-- =====================================================
-- RESUMEN DE DATOS
-- =====================================================
--
-- ✅ 12 Permisos granulares
-- ✅ 3 Roles (ADMIN, MANAGER, CLIENTE) con permisos asignados
-- ✅ 11 Usuarios (2 admins, 2 managers, 7 clientes)
-- ✅ 20 Productos en diferentes categorías
-- ✅ 16 Pedidos en todos los estados posibles
-- ✅ Passwords encriptados con BCrypt
-- ✅ Relaciones ManyToMany entre usuarios-roles y roles-permisos
--
-- =====================================================
