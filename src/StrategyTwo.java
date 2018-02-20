package com.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

/** . Class modelling an average Player Strategy for Gin Rummy */
public class StrategyTwo implements PlayerStrategy {

  ArrayList<Card> hand;

  /**
   * Called by the game engine for each player at the beginning of each round to receive and process
   * their initial hand dealt.
   *
   * @param hand The initial hand dealt to the player
   */
  @Override
  public void receiveInitialHand(List<Card> hand) {
    Collections.sort(hand);
    this.hand = new ArrayList<>(hand);
  }

  /**
   * Called by the game engine to prompt the player on whether they want to take the top card from
   * the discard pile or from the deck.
   *
   * @param card The card on the top of the discard pile
   * @return whether the user takes the card on the discard pile
   */
  @Override
  public boolean willTakeTopDiscard(Card card) {

    // Get card of largest rank.
    Card cardOfLargestRankInHand = hand.get(hand.size() - 1);

    // Compare with top card of discard pile and choose what to do.
    if (card.getRank().ordinal() < cardOfLargestRankInHand.getRank().ordinal()) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Called by the game engine to prompt the player to take their turn given a dealt card (and
   * returning their card they've chosen to discard).
   *
   * @param drawnCard The card the player was dealt
   * @return The card the player has chosen to discard
   */
  @Override
  public Card drawAndDiscard(Card drawnCard) {
    // Add the drawn card to Hand.
    hand.add(drawnCard);

    // Sort Hand by rank again.
    Collections.sort(hand);

    // Remove card of largest rank in hand.
    Card cardBeingDiscarded = hand.get(hand.size() - 1);
    hand.remove(cardBeingDiscarded);
    return cardBeingDiscarded;
  }

  /**
   * Called by the game engine to prompt the player is whether they would like to knock.
   *
   * @return True if the player has decided to knock
   */
  @Override
  public boolean knock() {
    // Knock at the first opportunity
    return true;
  }

  /**
   * Called by the game engine when the opponent has finished their turn to provide the player
   * information on what the opponent just did in their turn.
   *
   * @param drewDiscard Whether the opponent took from the discard
   * @param previousDiscardTop What the opponent could have drawn from the discard if they chose to
   * @param opponentDiscarded The card that the opponent discarded
   */
  @Override
  public void opponentEndTurnFeedback(
      boolean drewDiscard, Card previousDiscardTop, Card opponentDiscarded) {}

  /**
   * Called by the game engine when the round has ended to provide this player strategy information
   * about their opponent's hand and selection of Melds at the end of the round.
   *
   * @param opponentHand The opponent's hand at the end of the round
   * @param opponentMelds The opponent's Melds at the end of the round
   */
  @Override
  public void opponentEndRoundFeedback(List<Card> opponentHand, List<Meld> opponentMelds) {}

  /**
   * Called by the game engine to allow access the player's current list of Melds.
   *
   * @return The player's list of melds.
   */
  @Override
  public List<Meld> getMelds() {

    ArrayList<Meld> listOfMelds = new ArrayList<>();
    ArrayList<Card> playersHand = hand;
    ArrayList<Card> playersHandSortedByRank = playersHand;
    // Sort by rank.
    Collections.sort(playersHandSortedByRank);

    for (Card.CardRank rank : EnumSet.allOf(Card.CardRank.class)) {

      // Get a list of Cards of a particular rank.
      List<Card> cardsOfThisRank =
          playersHandSortedByRank
              .stream()
              .filter(c -> rank.ordinal() == c.getRank().ordinal())
              .collect(Collectors.toList());

      // Try to form a Set Meld with it.
      // If successful, add this meld to the list of all melds.
      SetMeld setMeld = SetMeld.buildSetMeld(cardsOfThisRank);

      if (setMeld != null) {
        listOfMelds.add(setMeld);
        playersHandSortedByRank.removeAll(cardsOfThisRank);
      }
    }

    // Comparator to compare Cards based on suit and rank.
    Comparator<Card> cardSuitAndRankComparator =
        (cardOne, cardTwo) -> {
          Card.CardSuit cardOneSuit = cardOne.getSuit();
          Card.CardSuit cardTwoSuit = cardTwo.getSuit();
          int whoHasGreaterRank = cardOne.compareTo(cardTwo);

          if (cardOneSuit.ordinal() < cardTwoSuit.ordinal()) {
            return -1;
          } else if (cardOneSuit.ordinal() > cardTwoSuit.ordinal()) {
            return 1;
          } else {
            return whoHasGreaterRank;
          }
        };

    // Sort cards by suit, AND by rank.
    ArrayList<Card> playersHandSortedBySuitAndRank = playersHandSortedByRank;
    Collections.sort(playersHandSortedBySuitAndRank, cardSuitAndRankComparator);

    for (Card.CardSuit suit : EnumSet.allOf(Card.CardSuit.class)) {

      // Get list of cards of a particular suit.
      List<Card> cardsOfThisSuit =
          playersHandSortedBySuitAndRank
              .stream()
              .filter(c -> suit.ordinal() == c.getSuit().ordinal())
              .collect(Collectors.toList());

      ArrayList<Card> cardsOfThisSuitCopy = new ArrayList<>(cardsOfThisSuit);

      if (cardsOfThisSuitCopy.size() == 0) {
        continue;
      }

      // Try forming Run melds.
      // If successful, add it to the list of melds.
      ArrayList<Card> tempListOfCards = new ArrayList<>();
      tempListOfCards.add(cardsOfThisSuitCopy.get(cardsOfThisSuitCopy.size() - 1));

      Card previousCard = cardsOfThisSuitCopy.get(cardsOfThisSuitCopy.size() - 1);

      for (int i = cardsOfThisSuitCopy.size() - 2; i >= 0; i--) {

        Card currentCard = cardsOfThisSuitCopy.get(i);

        if (currentCard.getRank().ordinal() == (previousCard.getRank().ordinal() - 1)) {
          tempListOfCards.add(currentCard);
        } else {

          RunMeld runMeld = RunMeld.buildRunMeld(tempListOfCards);
          if (runMeld != null) {
            listOfMelds.add(runMeld);
            playersHandSortedBySuitAndRank.removeAll(tempListOfCards);
          }
          tempListOfCards = new ArrayList<>();
          tempListOfCards.add(currentCard);
        }

        previousCard = currentCard;
      }

      RunMeld runMeld = RunMeld.buildRunMeld(tempListOfCards);
      if (runMeld != null) {
        listOfMelds.add(runMeld);
        playersHandSortedBySuitAndRank.removeAll(tempListOfCards);
      }
    }
    return listOfMelds;
  }

  /**
   * Called by the game engine to allow this player strategy to reset its internal state before
   * competing it against a new opponent.
   */
  @Override
  public void reset() {
    hand = new ArrayList<>();
  }
}
