package com.isec.pokercli.controller.game.deck.hand.scoring;

import com.isec.pokercli.controller.game.deck.hand.resolver.PokerResult;
import com.isec.pokercli.controller.game.deck.hand.scoring.scorers.*;

public class HandScorerFactory {

    public static HandScorer buildHandScorer(PokerResult result) {
        
        switch (result) {
            case STRAIGHT_FLUSH:
                return new StraightFlushScorer();
            case FOUR_OF_A_KIND:
                return new FourOfAKindScorer();
            case FULL_HOUSE:
                return new FullHouseScorer();
            case STRAIGHT:
                return new StraightScorer();
            case FLUSH:
                return new FlushScorer();
            case THREE_OF_A_KIND:
                return new ThreeOfAKindScorer();
            case TWO_PAIR:
                return new TwoPairScorer();
            case ONE_PAIR:
                return new OnePairScorer();
            case HIGH_CARD:
                return new HighCardScorer();
            default:
                throw new IllegalStateException("Poker result is invalid");
        }
    }
}
