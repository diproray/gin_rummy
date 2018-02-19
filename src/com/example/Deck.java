package com.example;

import java.util.ArrayList;
import java.util.Collections;

public class Deck extends Pile {

  public Deck() {
    this.pile = new ArrayList<Card>(Card.getAllCards());
    super.shuffle();
  }

  public ArrayList<Card> getInitialHand() {
    ArrayList<Card> hand = new ArrayList<Card>(pile.subList(0, 10));
    pile.removeAll(hand);
    return hand;
  }

}
