-- Schema creation

CREATE TABLE cliuser (
	id INT NOT NULL AUTO_INCREMENT,
	name VARCHAR(100) NOT NULL,
	balance DECIMAL(6, 2),
	virtual_balance DECIMAL(6, 2),
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (id)
);

CREATE TABLE game (
	id INT NOT NULL AUTO_INCREMENT,
	name VARCHAR(15) NOT NULL,
	owner_id INT NOT NULL,
    game_type VARCHAR(15) NOT NULL,
	max_players INT NOT NULL,
	initial_player_pot INT NOT NULL,
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	status VARCHAR(10) NOT NULL, -- 'CREATED', 'ONGOING', 'FINISHED',
	bet INT NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (owner_id) REFERENCES cliuser(id)
);

CREATE TABLE game_user (
	id INT NOT NULL AUTO_INCREMENT,
	game_id INT NOT NULL,
	owner_id INT NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (game_id) REFERENCES game(id),
	FOREIGN KEY (owner_id) REFERENCES cliuser(id)
);

CREATE TABLE message (
	id INT NOT NULL AUTO_INCREMENT,
	from_owner_id INT NOT NULL,
	to_owner_id INT NOT NULL,
	content TEXT,
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	status VARCHAR(10) NOT NULL, -- 'SENT', 'READ'
	PRIMARY KEY (id),
	FOREIGN KEY (from_owner_id) REFERENCES cliuser(id),
	FOREIGN KEY (to_owner_id) REFERENCES cliuser(id)
);

CREATE TABLE audit (
	id INT NOT NULL AUTO_INCREMENT,
	owner_id INT NOT NULL,
	type VARCHAR(20) NOT NULL,
	log TEXT NOT NULL,
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (id)
);

