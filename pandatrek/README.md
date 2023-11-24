## Stack
- Aplicação: Java SE 17
- Banco de Dados: MySQL Server 8.0.35
> Download: https://dev.mysql.com/get/Downloads/MySQLInstaller/mysql-installer-web-community-8.0.35.0.msi
> Conta Root: Usuário: root / Senha: root | Conta de Usuário: Usuário: admin / Senha: Root123#

Criando o schema e tables:

# Criando o banco "pandatrek"
CREATE SCHEMA `pandatrek` ;

# Criando a tabela "organizacao"
CREATE TABLE `organizacao` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `nome` varchar(40) NOT NULL,
  `data_cadastro` datetime NOT NULL,
  `ativo` tinyint unsigned NOT NULL DEFAULT '1',
  `excluido` tinyint unsigned NOT NULL DEFAULT '0',
  `data_exclusao` datetime NULL DEFAULT NULL,
  `descricao` varchar(240) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

# Criando a tabela "evento"
CREATE TABLE `evento` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `nome` varchar(40) NOT NULL,
  `data_cadastro` datetime NOT NULL,
  `ativo` tinyint unsigned NOT NULL DEFAULT '1',
  `excluido` tinyint unsigned NOT NULL DEFAULT '0',
  `data_exclusao` datetime NULL DEFAULT NULL,
  `descricao` varchar(240) DEFAULT NULL,
  `id_organizacao` int unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `id_organizacao_idx` (`id_organizacao`),
  CONSTRAINT `id_organizacao` FOREIGN KEY (`id_organizacao`) REFERENCES `organizacao` (`id`) ON DELETE SET NULL
);

# Populando tabela "organizacao" com 1 registro
INSERT INTO organizacao (nome, data_cadastro, descricao) VALUES ("Trem A Pé", NOW(), "Grupo de trilhas fundado em 2010");

# Populando tabela "evento" com 1 registro
INSERT INTO evento (nome, data_cadastro, descricao) VALUES ("Cachoeira Véu da Noiva", NOW(), "Trilha de bate-volta para cachoeira.");