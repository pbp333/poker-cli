package com.isec.pokercli.controller.game.deck.hand.scoring.scorers;

import com.isec.pokercli.model.entity.game.card.DeckCard;
import com.isec.pokercli.model.entity.game.card.Rank;
import com.isec.pokercli.controller.game.deck.hand.resolver.PokerResult;

import java.util.Comparator;
import java.util.List;

public class StraightScorer extends HandScorer {
    @Override
    protected int resolve(List<DeckCard> cards) {
        var rank = findTheHighestRank(cards);

        var pokerScore = PokerResult.STRAIGHT.getScore() * rank.getScore();
        var rankScore = cards.stream().map(DeckCard::getRank).map(Rank::getScore).reduce(Integer::sum).orElse(0);

        return pokerScore + rankScore;
    }

    private Rank findTheHighestRank(List<DeckCard> cards) {

        return cards.stream().map(DeckCard::getRank).filter(rank -> !rank.equals(Rank.ACE))
                .sorted(Comparator.comparing(Rank::getScore, Comparator.naturalOrder())).findFirst()
                .orElseThrow(() -> new IllegalStateException("No rank was found, should not happen"));

    }
}
