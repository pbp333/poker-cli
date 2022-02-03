package com.isec.pokercli.controller.game.deck.hand.resolver;

import com.isec.pokercli.controller.game.deck.DeckCard;
import org.paukov.combinatorics3.Generator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HandUtils {

    public static List<List<DeckCard>> breakIntoPossibleHands(List<DeckCard> hand) {

        if (hand.size() <= 5) {
            List<List<DeckCard>> result = new ArrayList<>();

            result.add(hand);

            return result;
        }

        var indexes = buildPossibleIndexes(hand.size(), new ArrayList<>());

        var i = 0;

        return Generator.combination(indexes).simple(5).stream()
                .map(comb -> comb.stream().map(ind -> hand.get(ind)).collect(Collectors.toList()))
                .collect(Collectors.toList());

    }

    private static List<Integer> buildPossibleIndexes(int numberOfIndexes, List<Integer> indexes) {

        if (numberOfIndexes == 0) {
            return indexes;
        }

        indexes.add(--numberOfIndexes);

        return buildPossibleIndexes(numberOfIndexes, indexes);

    }

}
