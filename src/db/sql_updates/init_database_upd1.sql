CREATE DATABASE IF NOT EXISTS online_shop_flowers_db;
USE online_shop_flowers_db;

CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(11) NOT NULL,
    password VARCHAR(255) NOT NULL
);

INSERT INTO users (username, email, phone, password)
VALUES ('admin', 'admin@mail.com', '79991234567', '1234');