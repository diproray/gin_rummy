import com.example.Card;

import java.util.ArrayList;

/**
 * . Class that models a deck of cards. Naturally, a deck is a pile of cards. Since Pile class
 * models a pile of cards, Deck inherits from Pile.
 *
 * @author diproray
 */
public class Deck extends Pile {

  /** . Constructor for Deck */
  public Deck() {
    // Get a standard set of 52 cards and shuffle them and store them.
    this.pile = new ArrayList<Card>(Card.getAllCards());
    super.shuffle();
  }

  /**
   * . Function that deals an initial hand to a Player.
   *
   * @return a hand - an ArrayList<Card> containing 10 cards
   */
  public ArrayList<Card> getInitialHand() {

    // Get top 10 cards from the shuffled pile of the deck.
    ArrayList<Card> hand = new ArrayList<Card>(pile.subList(0, 10));

    // Remove the cards from the pile of the deck.
    pile.removeAll(hand);

    // Return those cards.
    return hand;
  }
}
