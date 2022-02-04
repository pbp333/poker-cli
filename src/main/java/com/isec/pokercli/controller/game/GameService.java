package com.isec.pokercli.controller.game;

import java.math.BigDecimal;

public interface GameService {

    long createGame(String type, int maxNumberOfPlayers, BigDecimal minimalBuyIn);

    void deleteGame(long id);

}
