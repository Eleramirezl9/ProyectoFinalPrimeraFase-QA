-- =====================================================
-- DATOS INICIALES PARA MICROSERVICIO ISO/IEC 25010
-- =====================================================
-- Este archivo contiene datos de prueba para poblar la base de datos
-- Se ejecuta automáticamente al iniciar la aplicación
-- Codificación: UTF-8 para soporte completo de caracteres especiales

-- Configuración inicial para UTF-8
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

-- =====================================================
-- USUARIOS DE PRUEBA
-- =====================================================

INSERT INTO usuarios (nombre, apellido, email, telefono, activo, fecha_creacion) VALUES
('Juan Carlos', 'García López', 'juan.garcia@email.com', '50212345678', true, DATEADD('DAY', -30, CURRENT_TIMESTAMP)),
('María Elena', 'Rodríguez Pérez', 'maria.rodriguez@email.com', '50298765432', true, DATEADD('DAY', -25, CURRENT_TIMESTAMP)),
('Carlos Alberto', 'Martínez Silva', 'carlos.martinez@email.com', '50234567890', true, DATEADD('DAY', -20, CURRENT_TIMESTAMP)),
('Ana Sofía', 'López Hernández', 'ana.lopez@email.com', '50287654321', true, DATEADD('DAY', -18, CURRENT_TIMESTAMP)),
('Luis Fernando', 'Morales Castro', 'luis.morales@email.com', '50245678901', true, DATEADD('DAY', -15, CURRENT_TIMESTAMP)),
('Patricia Isabel', 'Vásquez Ruiz', 'patricia.vasquez@email.com', '50276543210', false, DATEADD('DAY', -12, CURRENT_TIMESTAMP)),
('Roberto Miguel', 'Jiménez Torres', 'roberto.jimenez@email.com', '50256789012', true, DATEADD('DAY', -10, CURRENT_TIMESTAMP)),
('Carmen Rosa', 'Flores Mendoza', 'carmen.flores@email.com', '50265432109', true, DATEADD('DAY', -8, CURRENT_TIMESTAMP)),
('Diego Alejandro', 'Castillo Vargas', 'diego.castillo@email.com', '50267890123', true, DATEADD('DAY', -5, CURRENT_TIMESTAMP)),
('Lucía Fernanda', 'Ramírez Aguilar', 'lucia.ramirez@email.com', '50254321098', true, DATEADD('DAY', -2, CURRENT_TIMESTAMP));

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
('Monitor Gaming 27 pulgadas', 'Monitor curvo con resolución 2K, 144Hz, tiempo de respuesta 1ms', 1899.99, 0, 'Electrónicos', 'ASUS', true, DATEADD('DAY', -1, CURRENT_TIMESTAMP)),
('Silla Ergonómica de Oficina', 'Silla con soporte lumbar, reposabrazos ajustables, base de aluminio', 1299.99, 0, 'Hogar y Jardín', 'Herman Miller', false, DATEADD('HOUR', -3, CURRENT_TIMESTAMP));

-- =====================================================
-- PEDIDOS DE PRUEBA
-- =====================================================

-- Pedidos pendientes (recientes, sin fecha_actualizacion)
INSERT INTO pedidos (usuario_id, producto_id, cantidad, precio_unitario, total, estado, observaciones, fecha_pedido) VALUES
(1, 1, 2, 2499.99, 4999.98, 'PENDIENTE', 'Pedido para regalo de cumpleaños', DATEADD('HOUR', -6, CURRENT_TIMESTAMP)),
(2, 5, 1, 5999.99, 5999.99, 'PENDIENTE', 'Entrega preferiblemente en horario matutino', DATEADD('HOUR', -12, CURRENT_TIMESTAMP)),
(3, 12, 1, 299.99, 299.99, 'PENDIENTE', NULL, DATEADD('DAY', -1, CURRENT_TIMESTAMP));

-- Pedidos confirmados (con fecha_actualizacion diferente)
INSERT INTO pedidos (usuario_id, producto_id, cantidad, precio_unitario, total, estado, observaciones, fecha_pedido, fecha_actualizacion) VALUES
(4, 2, 1, 4299.99, 4299.99, 'CONFIRMADO', 'Laptop para trabajo remoto', DATEADD('DAY', -3, CURRENT_TIMESTAMP), DATEADD('DAY', -2, CURRENT_TIMESTAMP)),
(5, 7, 1, 2799.99, 2799.99, 'CONFIRMADO', 'Aspiradora para casa nueva', DATEADD('DAY', -2, CURRENT_TIMESTAMP), DATEADD('HOUR', -18, CURRENT_TIMESTAMP)),
(1, 15, 3, 199.99, 599.97, 'CONFIRMADO', 'Esterillas para clases de yoga', DATEADD('HOUR', -30, CURRENT_TIMESTAMP), DATEADD('HOUR', -8, CURRENT_TIMESTAMP));

-- Pedidos en proceso (con fecha_actualizacion diferente)
INSERT INTO pedidos (usuario_id, producto_id, cantidad, precio_unitario, total, estado, observaciones, fecha_pedido, fecha_actualizacion) VALUES
(6, 4, 2, 1899.99, 3799.98, 'EN_PROCESO', 'Auriculares para oficina', DATEADD('DAY', -4, CURRENT_TIMESTAMP), DATEADD('DAY', -3, CURRENT_TIMESTAMP)),
(7, 9, 1, 599.99, 599.99, 'EN_PROCESO', NULL, DATEADD('DAY', -3, CURRENT_TIMESTAMP), DATEADD('DAY', -2, CURRENT_TIMESTAMP));

-- Pedidos enviados (con fecha_actualizacion diferente)
INSERT INTO pedidos (usuario_id, producto_id, cantidad, precio_unitario, total, estado, observaciones, fecha_pedido, fecha_actualizacion) VALUES
(8, 13, 1, 1999.99, 1999.99, 'ENVIADO', 'Reloj para maratón', DATEADD('DAY', -5, CURRENT_TIMESTAMP), DATEADD('DAY', -4, CURRENT_TIMESTAMP)),
(2, 16, 1, 299.99, 299.99, 'ENVIADO', 'Libro de programación', DATEADD('DAY', -4, CURRENT_TIMESTAMP), DATEADD('DAY', -3, CURRENT_TIMESTAMP));

-- Pedidos entregados (con fecha_entrega y fecha_actualizacion diferentes)
INSERT INTO pedidos (usuario_id, producto_id, cantidad, precio_unitario, total, estado, observaciones, fecha_pedido, fecha_entrega, fecha_actualizacion) VALUES
(9, 6, 1, 1299.99, 1299.99, 'ENTREGADO', 'Cafetera entregada exitosamente', DATEADD('DAY', -8, CURRENT_TIMESTAMP), DATEADD('DAY', -6, CURRENT_TIMESTAMP), DATEADD('DAY', -6, CURRENT_TIMESTAMP)),
(10, 11, 1, 3499.99, 3499.99, 'ENTREGADO', 'Bicicleta en perfecto estado', DATEADD('DAY', -12, CURRENT_TIMESTAMP), DATEADD('DAY', -9, CURRENT_TIMESTAMP), DATEADD('DAY', -9, CURRENT_TIMESTAMP)),
(3, 14, 2, 899.99, 1799.98, 'ENTREGADO', 'Mancuernas para gimnasio casero', DATEADD('DAY', -7, CURRENT_TIMESTAMP), DATEADD('DAY', -5, CURRENT_TIMESTAMP), DATEADD('DAY', -5, CURRENT_TIMESTAMP)),
(4, 18, 1, 399.99, 399.99, 'ENTREGADO', NULL, DATEADD('DAY', -16, CURRENT_TIMESTAMP), DATEADD('DAY', -13, CURRENT_TIMESTAMP), DATEADD('DAY', -13, CURRENT_TIMESTAMP));

-- Pedidos cancelados (con fecha_actualizacion diferente)
INSERT INTO pedidos (usuario_id, producto_id, cantidad, precio_unitario, total, estado, observaciones, fecha_pedido, fecha_actualizacion) VALUES
(5, 3, 1, 3899.99, 3899.99, 'CANCELADO', 'Cliente cambió de opinión', DATEADD('DAY', -6, CURRENT_TIMESTAMP), DATEADD('DAY', -5, CURRENT_TIMESTAMP)),
(1, 8, 1, 899.99, 899.99, 'CANCELADO', 'Producto no disponible en el color solicitado', DATEADD('DAY', -9, CURRENT_TIMESTAMP), DATEADD('DAY', -8, CURRENT_TIMESTAMP));

-- =====================================================
-- COMENTARIOS INFORMATIVOS
-- =====================================================

-- Los datos insertados incluyen:
-- ✅ 10 usuarios (9 activos, 1 inactivo) con fechas variadas usando DATEADD
-- ✅ 20 productos en 4 categorías diferentes con fechas escalonadas
-- ✅ 16 pedidos en todos los estados posibles con fechas realistas
-- ✅ Fechas de entrega solo en pedidos ENTREGADOS
-- ✅ Fechas de actualización diferentes para cada estado
-- ✅ Compatible con H2 Database usando DATEADD en lugar de INTERVAL

-- Timeline de pedidos:
-- PENDIENTE: hace 6h, 12h, 1 día (sin fecha_actualizacion)
-- CONFIRMADO: creados hace 3, 2 días y 30h, actualizados después
-- EN_PROCESO: creados hace 4, 3 días, actualizados después  
-- ENVIADO: creados hace 5, 4 días, actualizados después
-- ENTREGADO: creados hace 8-16 días, entregados hace 5-13 días
-- CANCELADO: creados hace 6, 9 días, cancelados después

-- Para acceder a la consola H2 y ver los datos:
-- URL: http://localhost:8080/api/h2-console
-- JDBC URL: jdbc:h2:mem:testdb
-- Username: sa
-- Password: password