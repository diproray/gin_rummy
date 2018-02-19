package com.example;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Player {

  private PlayerStrategy strategy;
  private ArrayList<Card> hand;

  public Player(PlayerStrategy strategy) {
    this.strategy = strategy;
    this.hand = new ArrayList<>();
  }

  public PlayerStrategy getStrategy() {
    return strategy;
  }

  public ArrayList<Card> getHand() {
    return hand;
  }

  public void setInitialHand(ArrayList<Card> hand) {
    this.hand = hand;
    strategy.receiveInitialHand(hand);
  }

  public void setHand(ArrayList<Card> hand) {
    this.hand = hand;
  }

  public void addToHand(Card card) {
    hand.add(card);
  }

  public void removeFromHand(Card card) {
    hand.remove(card);
  }
}
