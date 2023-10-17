CREATE TABLE IF NOT EXISTS usuarios (
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(255) NOT NULL,
  email TEXT NOT NULL,
  senha VARCHAR(20) NOT NULL,
  tipo ENUM('ADMIN', 'MODERADOR', 'ALUNO'),
  ativo BOOLEAN NOT NULL DEFAULT 1,
  UNIQUE (email)
);


CREATE TABLE IF NOT EXISTS denuncias (
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  alunoId INT NOT NULL,
  titulo VARCHAR(45) NOT NULL,
  descricao TEXT NOT NULL,
  tipo ENUM('BULLYING', 'ASSEDIO', 'PRECONCEITO', 'CONFLITO', 'ROUBO', 'VANDALISMO', 'COMPORTAMENTO_INDISCIPLINADO') NOT NULL,
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
  status VARCHAR(45) NOT NULL,
  data DATE NOT NULL,
  hora TIME NOT NULL,
  FOREIGN KEY (moderadorId) REFERENCES usuarios(id),
  FOREIGN KEY (denunciaId) REFERENCES denuncias(id)
);