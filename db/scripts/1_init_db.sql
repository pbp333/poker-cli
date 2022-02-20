-- Schema creation

CREATE TABLE cliuser (
	id INT NOT NULL AUTO_INCREMENT,
	name VARCHAR(100) NOT NULL,
	balance DECIMAL(6, 2),
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
	buy_in INT NOT NULL,
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
	user_id INT NOT NULL,
	current_player_pot INT NOT NULL,
	card1 VARCHAR(3),
    card2 VARCHAR(3),
    status tinyint(1) NOT NULL DEFAULT 0,
	PRIMARY KEY (id),
	FOREIGN KEY (game_id) REFERENCES game(id),
	FOREIGN KEY (user_id) REFERENCES cliuser(id)
);

CREATE TABLE game_round (
    id INT NOT NULL AUTO_INCREMENT,
    game_id INT NOT NULL,
    table_pot INT NOT NULL DEFAULT 0,
    card1 VARCHAR(3),
    card2 VARCHAR(3),
    card3 VARCHAR(3),
    card4 VARCHAR(3),
    card5 VARCHAR(3),
    PRIMARY KEY (id),
    FOREIGN KEY (game_id) REFERENCES game(id)
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

