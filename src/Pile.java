
import com.example.Card;

import java.util.ArrayList;
import java.util.Collections;

/**
 * . Class that models a pile of cards.
 *
 * @author diproray
 */
public class Pile {

  // Instance member.
  // The pile of cards contained in a pile object.
  protected ArrayList<Card> pile;

  /**
   * . Constructor for a pile object.
   * @param pile the list of cards to be part of the pile.
   */
  public Pile(ArrayList<Card> pile) {
    this.pile = pile;
  }

  /**
   * . Default constructor
   */
  public Pile() {
    this.pile = new ArrayList<Card>();
  }

  /**
   * . Function that shuffles the cards in the pile.
   */
  public void shuffle() {
    Collections.shuffle(this.pile);
  }

  /**
   * . Function that adds a card to the top of the pile.
   * @param cardToBeAdded
   */
  public void add(Card cardToBeAdded) {
    this.pile.add(0, cardToBeAdded);
  }

  /**
   * . Function that gets the top card of a pile.
   * @return the top Card of the pile
   */
  public Card getTopCard() {
    Card topCard = pile.get(0);
    return topCard;
  }

  /**
   * . Function that gets a list of cards within the pile.
   * @return the list of cards within the pile
   */
  public ArrayList<Card> getPile() {
    return pile;
  }
}
