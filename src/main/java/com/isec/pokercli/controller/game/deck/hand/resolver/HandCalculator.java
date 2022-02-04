package com.isec.pokercli.controller.game.deck.hand.resolver;

import com.isec.pokercli.controller.game.deck.DeckCard;
import com.isec.pokercli.controller.game.deck.card.Rank;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HandCalculator {

    public static HandResult score(List<DeckCard> hand) {

        var possibleHands = HandUtils.breakIntoPossibleHands(hand);

        var bestHand = calculateBestHand(possibleHands);

        return bestHand;
    }

    private static HandResult calculateBestHand(List<List<DeckCard>> possibleHands) {

        if (possibleHands.size() <= 1) {
            return calculateResult(possibleHands.get(0));
        }

        var results = possibleHands.stream().map(HandCalculator::calculateResult)
                .collect(Collectors.toList());

        return results.stream().sorted(Comparator.comparing(HandResult::calculateScore, Comparator.naturalOrder()))
                .findFirst().orElse(null);
    }


    private static HandResult calculateResult(List<DeckCard> hand) {

        var isStraight = isStraight(hand);
        var isFlush = isFlush(hand);

        if (isStraight && isFlush) {
            return new HandResult(PokerResult.STRAIGHT_FLUSH, hand);
        }

        var isFourOfAKind = isFourOfAKind(hand);

        if (isFourOfAKind) {
            return new HandResult(PokerResult.FOUR_OF_A_KIND, hand);
        }

        var isFullHouse = isFullHouse(hand);

        if (isFullHouse) {
            return new HandResult(PokerResult.FULL_HOUSE, hand);
        }

        if (isFlush) {
            return new HandResult(PokerResult.FLUSH, hand);
        }

        if (isStraight) {
            return new HandResult(PokerResult.STRAIGHT, hand);
        }

        var isThreeOfAKind = isThreeOfAKind(hand);

        if (isThreeOfAKind) {
            return new HandResult(PokerResult.THREE_OF_A_KIND, hand);
        }

        var isTwoPair = isTwoPair(hand);

        if (isTwoPair) {
            return new HandResult(PokerResult.TWO_PAIR, hand);
        }

        var isPair = isPair(hand);

        if (isPair) {
            return new HandResult(PokerResult.ONE_PAIR, hand);
        }

        return new HandResult(PokerResult.HIGH_CARD, hand);
    }

    private static boolean isStraight(List<DeckCard> hand) {

        var ranks = hand.stream().map(DeckCard::getRank).collect(Collectors.toList());

        var containsAce = ranks.stream().anyMatch(rank -> rank.equals(Rank.ACE));

        if (containsAce) {

            var isHighStraight = ranks.containsAll(
                    Arrays.asList(Rank.ACE, Rank.KING, Rank.QUEEN, Rank.JACK, Rank.TEN)
            );
            var isLowStraight = ranks.containsAll(
                    Arrays.asList(Rank.ACE, Rank.TWO, Rank.THREE, Rank.FOUR, Rank.FIVE)
            );

            return isHighStraight || isLowStraight;

        }

        var sortedRanks = hand.stream().map(DeckCard::getRank)
                .sorted(Comparator.comparing(Rank::getScore, Comparator.reverseOrder()))
                .collect(Collectors.toList());

        for (int i = 1; i < sortedRanks.size(); i++) {
            if (sortedRanks.get(i - 1).getScore() + 1 != sortedRanks.get(i).getScore()) {
                return false;
            }
        }

        return true;
    }

    private static boolean isFlush(List<DeckCard> hand) {

        return hand.stream().map(DeckCard::getSuit).collect(Collectors.toSet()).size() == 1;
    }

    private static boolean isFourOfAKind(List<DeckCard> hand) {

        var differentRanks = hand.stream().map(DeckCard::getRank).collect(Collectors.toSet());

        for (Rank rank : differentRanks) {
            var count = hand.stream().map(DeckCard::getRank).filter(rk -> rk.getScore() == rank.getScore()).count();

            if (count == 4) {
                return true;
            }
        }

        return false;
    }

    private static boolean isFullHouse(List<DeckCard> hand) {

        var differentRanks = hand.stream().map(DeckCard::getRank).collect(Collectors.toSet());

        for (Rank rank : differentRanks) {
            var count = hand.stream().map(DeckCard::getRank).filter(rk -> rk.getScore() == rank.getScore()).count();

            if (count == 3) {

                return isPair(hand.stream().filter(hd -> hd.getRank() != rank).collect(Collectors.toList()));
            }
        }

        return false;
    }

    private static boolean isThreeOfAKind(List<DeckCard> hand) {

        var differentRanks = hand.stream().map(DeckCard::getRank).collect(Collectors.toSet());

        for (Rank rank : differentRanks) {
            var count = hand.stream().map(DeckCard::getRank).filter(rk -> rk.getScore() == rank.getScore()).count();

            if (count == 3) {
                return true;
            }
        }

        return false;
    }

    private static boolean isTwoPair(List<DeckCard> hand) {

        var differentRanks = hand.stream().map(DeckCard::getRank).collect(Collectors.toSet());

        for (Rank rank : differentRanks) {
            var count = hand.stream().map(DeckCard::getRank).filter(rk -> rk.getScore() == rank.getScore()).count();

            if (count == 2) {

                return isPair(hand.stream().filter(hd -> hd.getRank() != rank).collect(Collectors.toList()));
            }
        }

        return false;
    }

    private static boolean isPair(List<DeckCard> hand) {

        var differentRanks = hand.stream().map(DeckCard::getRank).collect(Collectors.toSet());

        for (Rank rank : differentRanks) {
            var count = hand.stream().map(DeckCard::getRank).filter(rk -> rk.getScore() == rank.getScore()).count();

            if (count == 2) {
                return true;
            }
        }

        return false;
    }

}
