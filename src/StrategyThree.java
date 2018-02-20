package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StrategyThree implements PlayerStrategy {

  ArrayList<Card> hand;
  Random random = new Random();
  /**
   * Called by the game engine for each player at the beginning of each round to receive and
   * process their initial hand dealt.
   *
   * @param hand The initial hand dealt to the player
   */
  @Override
  public void receiveInitialHand(List<Card> hand) {
    this.hand = new ArrayList<>(hand);
  }

  /**
   * Called by the game engine to prompt the player on whether they want to take the top card
   * from the discard pile or from the deck.
   *
   * @param card The card on the top of the discard pile
   * @return whether the user takes the card on the discard pile
   */
  @Override
  public boolean willTakeTopDiscard(Card card) {
    int whatAction = random.nextInt(2);
    return (whatAction == 0);
  }

  /**
   * Called by the game engine to prompt the player to take their turn given a
   * dealt card (and returning their card they've chosen to discard).
   *
   * @param drawnCard The card the player was dealt
   * @return The card the player has chosen to discard
   */
  @Override
  public Card drawAndDiscard(Card drawnCard) {
    int index = random.nextInt(5);
    return hand.get(index);
  }

  /**
   * Called by the game engine to prompt the player is whether they would like to
   * knock.
   *
   * @return True if the player has decided to knock
   */
  @Override
  public boolean knock() {
    return true;
  }

  /**
   * Called by the game engine when the opponent has finished their turn to provide the player
   * information on what the opponent just did in their turn.
   *
   * @param drewDiscard        Whether the opponent took from the discard
   * @param previousDiscardTop What the opponent could have drawn from the discard if they chose to
   * @param opponentDiscarded  The card that the opponent discarded
   */
  @Override
  public void opponentEndTurnFeedback(boolean drewDiscard, Card previousDiscardTop, Card opponentDiscarded) {

  }

  /**
   * Called by the game engine when the round has ended to provide this player strategy
   * information about their opponent's hand and selection of Melds at the end of the round.
   *
   * @param opponentHand  The opponent's hand at the end of the round
   * @param opponentMelds The opponent's Melds at the end of the round
   */
  @Override
  public void opponentEndRoundFeedback(List<Card> opponentHand, List<Meld> opponentMelds) {
  }

  /**
   * Called by the game engine to allow access the player's current list of Melds.
   *
   * @return The player's list of melds.
   */
  @Override
  public List<Meld> getMelds() {
    return null;
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
