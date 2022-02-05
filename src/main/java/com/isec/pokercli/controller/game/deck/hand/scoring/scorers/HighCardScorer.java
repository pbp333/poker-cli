package com.isec.pokercli.controller.game.deck.hand.scoring.scorers;

import com.isec.pokercli.model.entity.game.card.DeckCard;
import com.isec.pokercli.model.entity.game.card.Rank;

import java.util.List;

public class HighCardScorer extends HandScorer {

    @Override
    protected int resolve(List<DeckCard> cards) {
        
        return cards.stream().map(DeckCard::getRank).map(Rank::getScore).reduce(Integer::sum).orElse(0);
    }
}
