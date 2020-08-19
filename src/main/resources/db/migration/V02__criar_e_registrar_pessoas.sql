CREATE TABLE IF NOT EXISTS pessoa (
  cpf BIGINT(14) PRIMARY KEY NOT NULL ,
  nome VARCHAR(100) NULL,
  email VARCHAR(50) NULL,
  data_nascimento date NULL,
  sexo VARCHAR(12) NULL,
  naturalidade VARCHAR(15) NULL,
  nacionalidade VARCHAR(15) NULL)
ENGINE = InnoDB default CHARSET = utf8;


insert into pessoa (cpf, nome, email, data_nascimento, sexo, naturalidade, nacionalidade) values ('01345609878', 'Kaio Ismael', 'kaio@gmail.com', '2017-06-10', 'masculino','Ribamar', 'brasil');
insert into pessoa (cpf, nome, email, data_nascimento, sexo, naturalidade, nacionalidade) values ('02345784998','Antônia Pereira', 'antonia@gmail.com', '2017-06-10','femenino', 'Pinheiro', 'brasil');
insert into pessoa (cpf, nome, email, data_nascimento, sexo, naturalidade, nacionalidade) values ('43587698789','Richard Morales',  'rich@gmail.com', '2017-06-10', 'masculino', 'Rio de Janeiro', 'brasil');
insert into pessoa (cpf, nome, email, data_nascimento, sexo, naturalidade, nacionalidade) values ('54356765422','Cesare Battisti',  'cesari@gmail.com', '2017-06-10', 'masculino', 'São Paulo', 'brasil');