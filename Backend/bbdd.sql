-- Dumping database structure for bbdd
DROP DATABASE IF EXISTS `bbdd`;
CREATE DATABASE IF NOT EXISTS `bbdd` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;
USE `bbdd`;

-- Dumping structure for table bbdd.Usuari
DROP TABLE IF EXISTS user;
CREATE TABLE IF NOT EXISTS user (
  id VARCHAR(100) DEFAULT NULL,
  nombre VARCHAR(50) DEFAULT NULL,
  contraseña VARCHAR(50) DEFAULT NULL,
  saldo INT NOT NULL,
  perfil VARCHAR(50) DEFAULT '1',
  PRIMARY KEY (id),
  UNIQUE (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Tabla que guarda la lista de usuarios registrados';

DROP TABLE IF EXISTS product;
CREATE TABLE IF NOT EXISTS product (
  id VARCHAR(100) DEFAULT NULL,
  nombre VARCHAR(100) DEFAULT NULL,
  precio INT NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Tabla que guarda la lista de productos disponibles';

DROP TABLE IF EXISTS purchase;
CREATE TABLE IF NOT EXISTS purchase (
  id INT AUTO_INCREMENT, -- Identificador único para cada compra
  idU VARCHAR(100) NOT NULL, -- ID del usuario que realizó la compra
  idP VARCHAR(100) NOT NULL, -- ID del producto comprado
  cantidad INT NOT NULL, -- Cantidad del producto comprado
  PRIMARY KEY (id), -- Clave primaria de la tabla
  FOREIGN KEY (idU) REFERENCES user(id) ON DELETE CASCADE ON UPDATE CASCADE, -- Relación con la tabla user
  FOREIGN KEY (idP) REFERENCES product(id) ON DELETE CASCADE ON UPDATE CASCADE -- Relación con la tabla product
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Tabla que guarda las compras realizadas por los usuarios';

-- Dumping data for table user: ~3 rows (approximately)
INSERT INTO user (id, nombre, contraseña, saldo) VALUES
    ('u1', 'Adria', '111', 0),
    ('u2', 'Joaquin', '222', 100),
    ('uu3', 'David', '333', 0);

INSERT INTO product (id, nombre, precio) VALUES
  ('p1', 'Perfil1', 10),
  ('p2', 'Perfil2', 15),
  ('p3', 'Vida Extra', 35);

INSERT INTO purchase (idU, idP, cantidad) VALUES
  ('u1', 'p1', 1),
  ('u2', 'p2', 2),
  ('uu3', 'p3', 3);
