package com.isec.pokercli.model.entity.game;

public class GameFactory {
    public Game createGame(GameType type) {
        if(type == GameType.FRIENDLY){
            return new Game.Builder().withMaxPlayers(3).withBet(4).withChips(50).withGameType(type).build();
        } else if(type == GameType.COMPETITIVE){
            return new Game.Builder().withGameType(type).build();
        }
        return null;
    }
}
