-- DROP DATABASE IF EXISTS football;
CREATE DATABASE IF NOT EXISTS football
  CHARACTER SET utf8mb4;

USE football;

CREATE TABLE IF NOT EXISTS competitions (
  id        INT NOT NULL PRIMARY KEY,
  name      VARCHAR(255),
  code      VARCHAR(3) NOT NULL,
  areaName  VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS teams (
  id        INT NOT NULL PRIMARY KEY,
  name      VARCHAR(255),
  tla       VARCHAR(255),
  shortName VARCHAR(255),
  areaName  VARCHAR(255),
  email     VARCHAR(255) DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS players (
  id              INT NOT NULL PRIMARY KEY,
  name            VARCHAR(255),
  position        VARCHAR(255) DEFAULT NULL,
  dateOfBirth     DATE DEFAULT NULL,
  countryOfBirth  VARCHAR(255),
  nationality     VARCHAR(255) 
) ;

CREATE TABLE IF NOT EXISTS competitions_teams (
  id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  competition_id  INT NOT NULL,
  team_id        INT NOT NULL,
  CONSTRAINT fk_competition_comp_teams FOREIGN KEY (competition_id) REFERENCES competitions(id),
  CONSTRAINT fk_team_comp_teams FOREIGN KEY (team_id) REFERENCES teams(id)
);


CREATE TABLE IF NOT EXISTS team_players (
  id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  team_id     INT NOT NULL,
  player_id   INT NOT NULL, 
  CONSTRAINT fk_team_team_players FOREIGN KEY (team_id) REFERENCES teams(id),
  CONSTRAINT fk_players_team_players FOREIGN KEY (player_id) REFERENCES players(id)
)
