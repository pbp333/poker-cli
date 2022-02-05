package com.isec.pokercli.controller.game.deck;

import com.isec.pokercli.model.entity.game.card.Rank;
import com.isec.pokercli.model.entity.game.card.Suit;
import com.isec.pokercli.model.entity.game.card.DeckCard;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class DeckCardFactory {

    public static Stack<DeckCard> buildRandomDeck() {

        var spades = buildByType(Suit.SPADES);
        var clubs = buildByType(Suit.CLUBS);
        var hearts = buildByType(Suit.HEARTS);
        var diamonds = buildByType(Suit.DIAMONDS);

        Stack<DeckCard> deck = new Stack<>();

        deck.addAll(spades);
        deck.addAll(clubs);
        deck.addAll(hearts);
        deck.addAll(diamonds);

        Collections.shuffle(deck);

        return deck;
    }

    private static List<DeckCard> buildByType(Suit kind) {

        return Arrays.stream(Rank.values()).map(card -> new DeckCard(card, kind)).collect(Collectors.toList());

    }
}
