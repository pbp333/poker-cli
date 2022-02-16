package com.isec.pokercli.application.game.creator;

import com.isec.pokercli.services.persistence.entity.game.Game;

public interface GameCreator {

    Game.Builder builder();
}
