package com.example;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("unused")
public class Card implements Comparable<Card> {
    public enum CardSuit {DIAMONDS, HEARTS, SPADES, CLUBS}
    public enum CardRank {ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING}

    private CardSuit suit;
    private CardRank rank;

    private Card(CardSuit suit, CardRank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public CardSuit getSuit() {
        return this.suit;
    }

    public CardRank getRank() {
        return this.rank;
    }

    /**
     * Gets the value of this card based on the following mapping:
     * Ace -> 1
     * Number card -> Number value
     * Face card -> 10
     * @return The points value of this card
     */
    public int getPointValue() {
        // ordinal() gets the index of this card's rank in order of its declaration in the CardRank enum
        // CardRank.ACE -> 0, CardRank.TWO -> 1, ..., CardRank.KING -> 12
        int rankIndex = rank.ordinal();

        boolean isAceOrNumberCard = (rankIndex < CardRank.JACK.ordinal());
        if (isAceOrNumberCard) {
            return rankIndex + 1;
        }

        return 10;
    }

    /**
     * Gets the rank value of the card
     * @return The rank value of this card
     */
    public int getRankValue() {
        return rank.ordinal();
    }

    /**
     * Retrieves all the Cards that can exist, without imposing any particular order on the data structure returned.
     * @return A Set of all possible Cards that can be made with the CardSuits and CardRanks declared in this class
     */
    public static Set<Card> getAllCards() {
        Set<Card> allCards = new HashSet<>();

        for (CardSuit suit : EnumSet.allOf(CardSuit.class)) {
            for (CardRank rank : EnumSet.allOf(CardRank.class)) {
                allCards.add(new Card(suit, rank));
            }
        }

        return allCards;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (other == null || !(other instanceof Card)) {
            return false;
        }

        Card otherCard = (Card) other;
        return (this.rank == otherCard.rank) && (this.suit == otherCard.suit);
    }

    @Override
    public int hashCode() {
        int result = suit.hashCode();
        result = 31 * result + rank.hashCode();
        return result;
    }

    @Override
    public int compareTo(Card otherCard) {
        if (this.rank.ordinal() < otherCard.rank.ordinal()) {
            return -1;
        }

        if (this.rank.ordinal() > otherCard.rank.ordinal()) {
            return 1;
        }

        return 0;
    }
}