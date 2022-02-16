package com.isec.pokercli.application.game.deck.hand.scoring.scorers;

import com.isec.pokercli.services.persistence.entity.game.card.DeckCard;
import com.isec.pokercli.services.persistence.entity.game.card.Rank;
import com.isec.pokercli.application.game.deck.hand.resolver.PokerResult;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FourOfAKindScorer extends HandScorer {
    @Override
    protected int resolve(List<DeckCard> cards) {

        var rank = findTheFourOfAKindRank(cards);

        var pokerScore = PokerResult.FOUR_OF_A_KIND.getScore() * rank.getScore();
        var rankScore = cards.stream().map(DeckCard::getRank).map(Rank::getScore).reduce(Integer::sum).orElse(0);

        return pokerScore + rankScore;
    }

    private Rank findTheFourOfAKindRank(List<DeckCard> cards) {

        var countByRank = cards.stream().map(DeckCard::getRank)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return countByRank.keySet().stream().filter(key -> countByRank.get(key).equals(4L)).findFirst()
                .orElseThrow(() -> new IllegalStateException("No rank was found, should not happen"));

    }
}
