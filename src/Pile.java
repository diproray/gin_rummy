
import com.example.Card;

import java.util.ArrayList;
import java.util.Collections;

public class Pile {

  public Pile(ArrayList<Card> pile) {
    this.pile = pile;
  }

  protected ArrayList<Card> pile;

  public Pile() {
    this.pile = new ArrayList<Card>();
  }

  public void shuffle() {
    Collections.shuffle(this.pile);
  }

  public void add(Card cardToBeAdded) {
    this.pile.add(0, cardToBeAdded);
  }

  public Card getTopCard() {
    Card topCard = pile.get(0);
    //pile.remove(0);
    return topCard;
  }

  public ArrayList<Card> getPile() {
    return pile;
  }
}
