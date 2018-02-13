package com.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A com.example.RunMeld represents a run of three or more cards of the same suit but sequential rank. Ace is a low card only.
 * {4 of Spades, 5 of Spades, 6 of Spades} is a valid com.example.RunMeld.
 * {Ace of Diamonds, 2 of Diamonds, 3 of Diamonds, 4 of Diamonds} is a valid com.example.RunMeld.
 * {Queen of Clubs, King of Clubs, Ace of Clubs} is not a valid com.example.RunMeld.
 * {6 of Hearts, 7 of Hearts, 8 of Diamonds} is not a valid com.example.RunMeld.
 */
public class RunMeld extends Meld {
    public static final int MIN_CARDS = 3;
    public static final int MAX_CARDS = 10;

    private List<Card> cardsInMeld;

    protected RunMeld(Card[] initialCards) {
        super();

        cardsInMeld = new ArrayList<Card>(Arrays.asList(initialCards));
    }

    @Override
    public boolean containsCard(Card cardToCheck) {
        return cardsInMeld.contains(cardToCheck);
    }

    @Override
    public boolean canAppendCard(Card newCard) {
        if (cardsInMeld.size() >= MAX_CARDS || cardsInMeld.contains(newCard)) {
            return false;
        }

        if (newCard.getSuit() != cardsInMeld.get(0).getSuit()) {
            return false;
        }

        int firstCardRankValue = cardsInMeld.get(0).getRankValue();
        int lastCardRankValue = cardsInMeld.get(cardsInMeld.size() - 1).getRankValue();
        int newRankValue = newCard.getRankValue();

        return (newRankValue == firstCardRankValue - 1) || (newRankValue == lastCardRankValue + 1);
    }

    @Override
    public void appendCard(Card newCard) {
        if (!canAppendCard(newCard)) {
            throw new IllegalMeldModificationException();
        }

        int lastCardRankValue = cardsInMeld.get(cardsInMeld.size() - 1).getRankValue();
        int newRankValue = newCard.getRankValue();

        if (lastCardRankValue < newRankValue) {
            cardsInMeld.add(newCard);
        } else {
            cardsInMeld.add(0, newCard);
        }
    }

    @Override
    public boolean canRemoveCard(Card cardToRemove) {
        if (cardsInMeld.size() <= MIN_CARDS || !cardsInMeld.contains(cardToRemove)) {
            return false;
        }

        int indexOfCard = cardsInMeld.indexOf(cardToRemove);
        return (indexOfCard == 0) || (indexOfCard == cardsInMeld.size() - 1);
    }

    @Override
    public void removeCard(Card cardToRemove) {
        if (!canRemoveCard(cardToRemove)) {
            throw new IllegalMeldModificationException();
        }

        cardsInMeld.remove(cardToRemove);
    }

    @Override
    public Card[] getCards() {
        Card[] arrayToReturn = new Card[cardsInMeld.size()];
        return cardsInMeld.toArray(arrayToReturn);
    }
}
