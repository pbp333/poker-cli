package com.isec.pokercli.controller.game.deck.hand.resolver;

import com.isec.pokercli.controller.game.deck.DeckCard;
import com.isec.pokercli.controller.game.deck.card.Rank;

import java.util.ArrayList;
import java.util.List;

public class HandResult {

    private final PokerResult pokerResult;
    private final List<DeckCard> cards = new ArrayList<>();

    public HandResult(PokerResult pokerResult, List<DeckCard> cards) {
        this.pokerResult = pokerResult;
        this.cards.addAll(cards);
    }

    public int calculateScore() {

        var cardsScore = cards.stream().map(DeckCard::getRank).map(Rank::getScore).reduce(Integer::sum).orElse(0);
        var resultScore = pokerResult.getScore();

        return cardsScore + resultScore;
    }

    public PokerResult getPokerResult() {
        return pokerResult;
    }

    public List<DeckCard> getCards() {
        return cards;
    }
}
