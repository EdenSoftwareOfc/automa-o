CREATE DATABASE OSIRIS;
USE OSIRIS;

CREATE TABLE reconhecimento (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(55) not null,
    imagem LONGBLOB
);

SELECT * FROM reconhecimento;
DROP TABLE `osiris`.`reconhecimento`;

CREATE TABLE comandos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    caminho VARCHAR(255) NOT NULL
);
SELECT * FROM comandos;
DROP TABLE `osiris`.`comandos`;

CREATE TABLE dados_treinamento (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pergunta_usuario TEXT,
    resposta_assistente TEXT
);

CREATE TABLE feedback_usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pergunta_usuario TEXT,
    resposta_assistente TEXT,
    resposta_util BOOLEAN
);