package com.example.minigames.game;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BlackjackEngine {

    public enum Suit { HEARTS, DIAMONDS, CLUBS, SPADES }
    public enum Rank {
        TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10),
        JACK(10), QUEEN(10), KING(10), ACE(11);
        public final int value;
        Rank(int v) { this.value = v; }
    }

    public static class Card implements Serializable {
        public final Rank rank;
        public final Suit suit;
        public Card(Rank rank, Suit suit) { this.rank = rank; this.suit = suit; }
        @Override public String toString() {
            String r = switch (rank) {
                case JACK -> "J"; case QUEEN -> "Q"; case KING -> "K"; case ACE -> "A";
                case TEN -> "10"; default -> String.valueOf(rank.value);
            };
            String s = switch (suit) {
                case HEARTS -> "♥"; case DIAMONDS -> "♦"; case CLUBS -> "♣"; case SPADES -> "♠";
            };
            return r + s;
        }
    }

    private final Random rng;

    public BlackjackEngine() { this(new SecureRandom()); }
    public BlackjackEngine(Random rng) { this.rng = rng; }

    public List<Card> newDeck() {
        List<Card> deck = new ArrayList<>(52);
        for (Suit s : Suit.values())
            for (Rank r : Rank.values())
                deck.add(new Card(r, s));
        Collections.shuffle(deck, rng);
        return deck;
    }

    /** Сумма очков. Туз = 11, при переборе понижается до 1. */
    public static int score(List<Card> hand) {
        int sum = 0;
        int aces = 0;
        for (Card c : hand) {
            sum += c.rank.value;
            if (c.rank == Rank.ACE) aces++;
        }
        while (sum > 21 && aces > 0) {
            sum -= 10;
            aces--;
        }
        return sum;
    }

    public static boolean isBlackjack(List<Card> hand) {
        return hand.size() == 2 && score(hand) == 21;
    }

    /** Дилер набирает до 17 (включительно). Soft 17 — стоит. */
    public void dealerPlay(List<Card> dealer, List<Card> deck) {
        while (score(dealer) < 17) {
            dealer.add(deck.remove(deck.size() - 1));
        }
    }
}
