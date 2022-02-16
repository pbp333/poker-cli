package com.isec.pokercli.application.game.deck.hand.resolver;

import com.isec.pokercli.services.persistence.entity.game.card.DeckCard;
import com.isec.pokercli.application.game.deck.hand.scoring.HandScorerFactory;
import com.isec.pokercli.application.game.deck.hand.scoring.scorers.HandScorer;

import java.util.ArrayList;
import java.util.List;

public class HandResult {

    private final PokerResult pokerResult;
    private final List<DeckCard> cards = new ArrayList<>();
    private final HandScorer scorer;

    public HandResult(PokerResult pokerResult, List<DeckCard> cards) {
        this.pokerResult = pokerResult;
        this.cards.addAll(cards);
        this.scorer = HandScorerFactory.buildHandScorer(pokerResult);
    }

    public int calculateScore() {
        return scorer.score(cards);
    }

    public PokerResult getPokerResult() {
        return pokerResult;
    }

    public List<DeckCard> getCards() {
        return cards;
    }
}
