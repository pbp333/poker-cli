package com.isec.pokercli.controller.game.deck.hand.scoring.scorers;

import com.isec.pokercli.model.entity.game.card.DeckCard;
import com.isec.pokercli.model.entity.game.card.Rank;
import com.isec.pokercli.controller.game.deck.hand.resolver.PokerResult;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class OnePairScorer extends HandScorer {

    @Override
    protected int resolve(List<DeckCard> cards) {

        var pairRank = findThePairRank(cards);

        var pokerScore = PokerResult.ONE_PAIR.getScore() * pairRank.getScore();
        var rankScore = cards.stream().map(DeckCard::getRank).map(Rank::getScore).reduce(Integer::sum).orElse(0);

        return pokerScore + rankScore;
    }

    private Rank findThePairRank(List<DeckCard> cards) {

        var countByRank = cards.stream().map(DeckCard::getRank)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return countByRank.keySet().stream().filter(key -> countByRank.get(key).equals(2L)).findFirst()
                .orElseThrow(() -> new IllegalStateException("No rank was found, should not happen"));

    }
}
