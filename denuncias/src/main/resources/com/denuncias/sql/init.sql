CREATE TABLE IF NOT EXISTS usuarios (
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(255) NOT NULL,
  email VARCHAR(255),
  senha VARCHAR(20) NOT NULL,
  tipo ENUM('ADMIN', 'MODERADOR', 'ALUNO') NOT NULL,
  ativo BOOLEAN NOT NULL DEFAULT 1,
  UNIQUE (email)
);


CREATE TABLE IF NOT EXISTS denuncias (
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  alunoId INT NOT NULL,
  titulo VARCHAR(45) NOT NULL,
  descricao TEXT NOT NULL,
  tipo ENUM('BULLYING', 'ASSEDIO', 'PRECONCEITO', 'CONFLITO', 'ROUBO', 'VANDALISMO', 'COMPORTAMENTO_INDISCIPLINADO', 'DROGA', 'OUTROS') NOT NULL,
  local VARCHAR(45) NOT NULL,
  status ENUM('REGISTRADO', 'INVESTIGANDO', 'ENCERRADO') NOT NULL,
  data DATE NOT NULL,
  hora TIME NOT NULL,
  FOREIGN KEY (alunoId) REFERENCES usuarios(id)
);


CREATE TABLE IF NOT EXISTS comentarios (
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  moderadorId INT NOT NULL,
  denunciaId INT NOT NULL,
  conteudo TEXT NOT NULL,
  status ENUM('REGISTRADO', 'INVESTIGANDO', 'ENCERRADO') NOT NULL,
  data DATE NOT NULL,
  hora TIME NOT NULL,
  FOREIGN KEY (moderadorId) REFERENCES usuarios(id),
  FOREIGN KEY (denunciaId) REFERENCES denuncias(id)
);


DELIMITER $$
DROP TRIGGER IF EXISTS atualizarStatus $$
CREATE TRIGGER atualizarStatus
AFTER INSERT ON comentarios 
FOR EACH ROW
BEGIN
	UPDATE denuncias SET status = NEW.status WHERE denuncias.id=NEW.denunciaId;
END $$
DELIMITER ;


DELIMITER $$
DROP FUNCTION IF EXISTS padronizarNome $$
CREATE FUNCTION padronizarNome(texto VARCHAR(255))
RETURNS VARCHAR(255)
BEGIN
    DECLARE texto_maiusculo VARCHAR(255);
    DECLARE palavra VARCHAR(255);
    DECLARE i INT DEFAULT 1;
    
    SET texto_maiusculo = LOWER(texto);
    
    WHILE i <= LENGTH(texto) DO
        IF i = 1 OR SUBSTRING(texto, i-1, 1) = ' ' THEN
            SET palavra = UPPER(SUBSTRING(texto, i, 1));
            SET texto_maiusculo = CONCAT(
                LEFT(texto_maiusculo, i-1),
                palavra,
                SUBSTRING(texto_maiusculo, i+1));
        END IF;
        SET i = i + 1;
    END WHILE;
    
    RETURN texto_maiusculo;
END $$
DELIMITER ;


DELIMITER $$
DROP TRIGGER IF EXISTS inserirNome $$
CREATE TRIGGER inserirNome
BEFORE INSERT ON usuarios
FOR EACH ROW
BEGIN
    DECLARE retorno TEXT;
    
    SET retorno = padronizarNome(NEW.nome);
	SET NEW.nome = retorno;
END $$
DELIMITER ;


DELIMITER $$
DROP FUNCTION IF EXISTS validarGmail $$
CREATE FUNCTION validarGmail(email TEXT)
RETURNS TINYINT
BEGIN
	DECLARE valido TINYINT;

	SELECT 1 into valido
	   WHERE email REGEXP '^[a-zA-Z0-9][a-zA-Z0-9.!#$%&\'*+-/=?^_`{|}~]*?[a-zA-Z0-9._-]?@gmail\.com$'
	   and email not like '%[^a-z0-9@._-]%' -- not allow characters differents: a-z 0-9 @ . _ -
	   and email not like '%@%@%' -- not allow  two @
	   and email not like '%.@%' -- not allow  .@
	   and email not like '%..%' -- not allow  ..
	   and email not like '%.' -- not allow  . (dot) at end
	   and email like '%_@_%_.__%' -- not allow short, i.e.,  a@a.com
	   and email not LIKE '%^%' -- not allow character ^
	   and email not LIKE '%\%%' -- not allow character %
	;
    
	IF valido IS NULL THEN
		SET valido = 0;
    END IF;
    
	RETURN valido;
END $$
DELIMITER ;


DELIMITER $$
DROP TRIGGER IF EXISTS validarEmail $$
CREATE TRIGGER validarEmail
BEFORE INSERT ON usuarios
FOR EACH ROW
BEGIN
    DECLARE resposta TINYINT;
    
    SET resposta = validarGmail(NEW.email);

    IF resposta = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Endereço de e-mail inválido. O e-mail deve pertencer ao domínio do Gmail.';
    END IF;
END $$	
DELIMITER ;


DELIMITER $$
DROP PROCEDURE IF EXISTS recuperarConta $$
CREATE PROCEDURE recuperarConta(IN email_entrada TEXT, OUT nome_saida VARCHAR(255), OUT senha_saida VARCHAR(20))
BEGIN
	DECLARE email_aux TEXT;
    DECLARE nome_aux VARCHAR(255);
    DECLARE senha_aux VARCHAR(20);
        
	SELECT email, nome, senha INTO email_aux, nome_aux, senha_aux FROM usuarios WHERE email_entrada=usuarios.email;
        
	IF email_aux IS NULL THEN
		SET nome_saida = 'Inválido.';
        SET senha_saida = 'Inválida.';
	ELSE 
		SET nome_saida = nome_aux;
        SET senha_saida = senha_aux;
	END IF;
END $$
DELIMITER ;


DELIMITER $$
DROP PROCEDURE IF EXISTS trocarSenha $$
CREATE PROCEDURE trocarSenha(IN codigo INT, IN nova_senha VARCHAR(20), OUT resposta TINYINT)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
        
	BEGIN
		SET resposta = 1;
		ROLLBACK;
	END;
      
	DECLARE EXIT HANDLER FOR SQLWARNING
	BEGIN
		SET resposta = 2;
		ROLLBACK;
	END;
      
	START TRANSACTION;
        
	UPDATE usuarios SET senha=nova_senha WHERE id=codigo;
        
	SET resposta = 0;
	COMMIT;
END $$
DELIMITER ;


INSERT INTO usuarios(nome, email, senha, tipo) VALUES('nome do admin aqui', 'admin@gmail.com', '12345678', 'ADMIN');