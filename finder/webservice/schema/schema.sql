DROP TABLE IF EXISTS objeto;

CREATE TABLE objeto(
	id serial,
	titulo varchar(100),
	descricao varchar(1000),
	contato varchar(500),
	PRIMARY KEY(id));

