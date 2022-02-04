package com.isec.pokercli.controller.game.deck.hand.scoring.scorers;

import com.isec.pokercli.controller.game.deck.DeckCard;
import com.isec.pokercli.controller.game.deck.card.Rank;
import com.isec.pokercli.controller.game.deck.hand.resolver.PokerResult;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TwoPairScorer extends HandScorer {

    @Override
    protected int resolve(List<DeckCard> cards) {

        var pairRank = findTheHighestPairRank(cards);

        var pokerScore = PokerResult.TWO_PAIR.getScore() * pairRank.getScore();
        var rankScore = cards.stream().map(DeckCard::getRank).map(Rank::getScore).reduce(Integer::sum).orElse(0);

        return pokerScore + rankScore;
    }

    private Rank findTheHighestPairRank(List<DeckCard> cards) {

        var countByRank = cards.stream().map(DeckCard::getRank)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return countByRank.keySet().stream().filter(key -> countByRank.get(key).equals(2L))
                .sorted(Comparator.comparing(Rank::getScore, Comparator.naturalOrder())).findFirst()
                .orElseThrow(() -> new IllegalStateException("No rank was found, should not happen"));

    }
}
