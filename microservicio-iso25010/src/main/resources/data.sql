-- =====================================================
-- DATOS INICIALES PARA MICROSERVICIO ISO/IEC 25010
-- =====================================================
-- Este archivo contiene datos de prueba para poblar la base de datos
-- Se ejecuta automáticamente al iniciar la aplicación

-- =====================================================
-- USUARIOS DE PRUEBA
-- =====================================================

INSERT INTO usuarios (nombre, apellido, email, telefono, activo, fecha_creacion) VALUES
('Juan Carlos', 'García López', 'juan.garcia@email.com', '50212345678', true, CURRENT_TIMESTAMP),
('María Elena', 'Rodríguez Pérez', 'maria.rodriguez@email.com', '50298765432', true, CURRENT_TIMESTAMP),
('Carlos Alberto', 'Martínez Silva', 'carlos.martinez@email.com', '50234567890', true, CURRENT_TIMESTAMP),
('Ana Sofía', 'López Hernández', 'ana.lopez@email.com', '50287654321', true, CURRENT_TIMESTAMP),
('Luis Fernando', 'Morales Castro', 'luis.morales@email.com', '50245678901', true, CURRENT_TIMESTAMP),
('Patricia Isabel', 'Vásquez Ruiz', 'patricia.vasquez@email.com', '50276543210', false, CURRENT_TIMESTAMP),
('Roberto Miguel', 'Jiménez Torres', 'roberto.jimenez@email.com', '50256789012', true, CURRENT_TIMESTAMP),
('Carmen Rosa', 'Flores Mendoza', 'carmen.flores@email.com', '50265432109', true, CURRENT_TIMESTAMP),
('Diego Alejandro', 'Castillo Vargas', 'diego.castillo@email.com', '50267890123', true, CURRENT_TIMESTAMP),
('Lucía Fernanda', 'Ramírez Aguilar', 'lucia.ramirez@email.com', '50254321098', true, CURRENT_TIMESTAMP);

-- =====================================================
-- PRODUCTOS DE PRUEBA
-- =====================================================

-- Categoría: Electrónicos
INSERT INTO productos (nombre, descripcion, precio, stock, categoria, marca, activo, fecha_creacion) VALUES
('Smartphone Samsung Galaxy A54', 'Teléfono inteligente con pantalla AMOLED de 6.4 pulgadas, cámara triple de 50MP, 128GB de almacenamiento y 6GB de RAM', 2499.99, 25, 'Electrónicos', 'Samsung', true, CURRENT_TIMESTAMP),
('Laptop Dell Inspiron 15', 'Laptop con procesador Intel Core i5, 8GB RAM, 512GB SSD, pantalla de 15.6 pulgadas Full HD', 4299.99, 15, 'Electrónicos', 'Dell', true, CURRENT_TIMESTAMP),
('Tablet iPad Air', 'Tablet Apple con chip M1, pantalla Liquid Retina de 10.9 pulgadas, 64GB de almacenamiento', 3899.99, 12, 'Electrónicos', 'Apple', true, CURRENT_TIMESTAMP),
('Auriculares Sony WH-1000XM4', 'Auriculares inalámbricos con cancelación de ruido, batería de 30 horas, Bluetooth 5.0', 1899.99, 30, 'Electrónicos', 'Sony', true, CURRENT_TIMESTAMP),
('Smart TV LG 55 pulgadas', 'Televisor 4K UHD con webOS, HDR10, Dolby Vision, Wi-Fi integrado', 5999.99, 8, 'Electrónicos', 'LG', true, CURRENT_TIMESTAMP),

-- Categoría: Hogar y Jardín
('Cafetera Nespresso Vertuo', 'Cafetera de cápsulas con tecnología Centrifusion, prepara café y espresso', 1299.99, 20, 'Hogar y Jardín', 'Nespresso', true, CURRENT_TIMESTAMP),
('Aspiradora Dyson V11', 'Aspiradora inalámbrica con tecnología ciclónica, batería de 60 minutos', 2799.99, 10, 'Hogar y Jardín', 'Dyson', true, CURRENT_TIMESTAMP),
('Microondas Panasonic 1.2 cu ft', 'Horno microondas con tecnología inverter, 1200W de potencia, panel digital', 899.99, 18, 'Hogar y Jardín', 'Panasonic', true, CURRENT_TIMESTAMP),
('Juego de Sartenes Tefal', 'Set de 3 sartenes antiadherentes con recubrimiento titanium, aptas para inducción', 599.99, 25, 'Hogar y Jardín', 'Tefal', true, CURRENT_TIMESTAMP),
('Purificador de Aire Xiaomi', 'Purificador con filtro HEPA, cobertura de 48m², control por app móvil', 1199.99, 15, 'Hogar y Jardín', 'Xiaomi', true, CURRENT_TIMESTAMP),

-- Categoría: Deportes y Fitness
('Bicicleta de Montaña Trek', 'Bicicleta MTB con marco de aluminio, suspensión delantera, 21 velocidades', 3499.99, 6, 'Deportes y Fitness', 'Trek', true, CURRENT_TIMESTAMP),
('Banda Elástica de Ejercicio', 'Set de 5 bandas de resistencia con diferentes niveles, incluye accesorios', 299.99, 40, 'Deportes y Fitness', 'Fitness Pro', true, CURRENT_TIMESTAMP),
('Reloj Deportivo Garmin', 'Smartwatch con GPS, monitor de frecuencia cardíaca, resistente al agua', 1999.99, 22, 'Deportes y Fitness', 'Garmin', true, CURRENT_TIMESTAMP),
('Mancuernas Ajustables', 'Par de mancuernas con peso ajustable de 2.5kg a 25kg cada una', 899.99, 12, 'Deportes y Fitness', 'PowerBlock', true, CURRENT_TIMESTAMP),
('Esterilla de Yoga Premium', 'Esterilla antideslizante de 6mm de grosor, material eco-friendly', 199.99, 35, 'Deportes y Fitness', 'Manduka', true, CURRENT_TIMESTAMP),

-- Categoría: Libros y Educación
('Libro: Clean Code', 'Guía completa para escribir código limpio y mantenible por Robert C. Martin', 299.99, 50, 'Libros y Educación', 'Prentice Hall', true, CURRENT_TIMESTAMP),
('Curso Online: Spring Boot Masterclass', 'Curso completo de desarrollo con Spring Boot, incluye certificado', 499.99, 100, 'Libros y Educación', 'TechAcademy', true, CURRENT_TIMESTAMP),
('Calculadora Científica Casio', 'Calculadora programable con pantalla gráfica, ideal para ingeniería', 399.99, 28, 'Libros y Educación', 'Casio', true, CURRENT_TIMESTAMP),

-- Productos sin stock para pruebas
('Monitor Gaming 27 pulgadas', 'Monitor curvo con resolución 2K, 144Hz, tiempo de respuesta 1ms', 1899.99, 0, 'Electrónicos', 'ASUS', true, CURRENT_TIMESTAMP),
('Silla Ergonómica de Oficina', 'Silla con soporte lumbar, reposabrazos ajustables, base de aluminio', 1299.99, 0, 'Hogar y Jardín', 'Herman Miller', false, CURRENT_TIMESTAMP);

-- =====================================================
-- PEDIDOS DE PRUEBA
-- =====================================================

-- Pedidos en diferentes estados para demostrar el flujo completo
INSERT INTO pedidos (usuario_id, producto_id, cantidad, precio_unitario, total, estado, observaciones, fecha_pedido) VALUES
-- Pedidos pendientes
(1, 1, 2, 2499.99, 4999.98, 'PENDIENTE', 'Pedido para regalo de cumpleaños', CURRENT_TIMESTAMP),
(2, 5, 1, 5999.99, 5999.99, 'PENDIENTE', 'Entrega preferiblemente en horario matutino', CURRENT_TIMESTAMP),
(3, 12, 1, 299.99, 299.99, 'PENDIENTE', NULL, CURRENT_TIMESTAMP),

-- Pedidos confirmados
(4, 2, 1, 4299.99, 4299.99, 'CONFIRMADO', 'Laptop para trabajo remoto', CURRENT_TIMESTAMP),
(5, 7, 1, 2799.99, 2799.99, 'CONFIRMADO', 'Aspiradora para casa nueva', CURRENT_TIMESTAMP),
(1, 15, 3, 199.99, 599.97, 'CONFIRMADO', 'Esterillas para clases de yoga', CURRENT_TIMESTAMP),

-- Pedidos en proceso
(6, 4, 2, 1899.99, 3799.98, 'EN_PROCESO', 'Auriculares para oficina', CURRENT_TIMESTAMP),
(7, 9, 1, 599.99, 599.99, 'EN_PROCESO', NULL, CURRENT_TIMESTAMP),

-- Pedidos enviados
(8, 13, 1, 1999.99, 1999.99, 'ENVIADO', 'Reloj para maratón', CURRENT_TIMESTAMP),
(2, 16, 1, 299.99, 299.99, 'ENVIADO', 'Libro de programación', CURRENT_TIMESTAMP),

-- Pedidos entregados
(9, 6, 1, 1299.99, 1299.99, 'ENTREGADO', 'Cafetera entregada exitosamente', CURRENT_TIMESTAMP),
(10, 11, 1, 3499.99, 3499.99, 'ENTREGADO', 'Bicicleta en perfecto estado', CURRENT_TIMESTAMP),
(3, 14, 2, 899.99, 1799.98, 'ENTREGADO', 'Mancuernas para gimnasio casero', CURRENT_TIMESTAMP),
(4, 18, 1, 399.99, 399.99, 'ENTREGADO', NULL, CURRENT_TIMESTAMP),

-- Pedidos cancelados
(5, 3, 1, 3899.99, 3899.99, 'CANCELADO', 'Cliente cambió de opinión', CURRENT_TIMESTAMP),
(1, 8, 1, 899.99, 899.99, 'CANCELADO', 'Producto no disponible en el color solicitado', CURRENT_TIMESTAMP);

-- =====================================================
-- COMENTARIOS INFORMATIVOS
-- =====================================================

-- Los datos insertados incluyen:
-- ✅ 10 usuarios (9 activos, 1 inactivo)
-- ✅ 19 productos en 4 categorías diferentes (17 activos, 2 inactivos)
-- ✅ 16 pedidos en todos los estados posibles
-- ✅ Variedad de precios y cantidades para pruebas
-- ✅ Algunos productos sin stock para probar validaciones
-- ✅ Relaciones correctas entre usuarios, productos y pedidos

-- Estos datos permiten probar:
-- 🔍 Búsquedas por diferentes criterios
-- 📊 Estadísticas y reportes
-- ⚡ Validaciones de stock
-- 🔄 Cambios de estado de pedidos
-- 📈 Cálculos de totales y precios
-- 🎯 Filtros por categorías, marcas, etc.

-- Para acceder a la consola H2 y ver los datos:
-- URL: http://localhost:8080/api/h2-console
-- JDBC URL: jdbc:h2:mem:testdb
-- Username: sa
-- Password: password

