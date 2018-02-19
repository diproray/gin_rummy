package com.example;

import java.util.Random;

public class GameEngine {

  private Deck deck;
  private Pile stockPile;
  private Pile discardPile;
  private Player playerOne;
  private Player playerTwo;
  private boolean playerOneTurn;
  private boolean playerTwoTurn;

  public GameEngine(PlayerStrategy strategyOne, PlayerStrategy strategyTwo) {

    this.deck = new Deck();
    this.stockPile = new Pile();
    this.discardPile = new Pile();

    this.playerOne = new Player(strategyOne);
    this.playerTwo = new Player(strategyTwo);

    playerOne.setInitialHand(deck.getInitialHand());
    playerTwo.setInitialHand(deck.getInitialHand());

    discardPile.add(deck.getTopCard());
    stockPile.getPile().addAll(deck.getPile());

    Random random = new Random();
    int whichPlayer = random.nextInt(2) + 1;

    if (whichPlayer == 1) {

      playerOneTurn = true;
      playerTwoTurn = false;

    } else {

      playerTwoTurn = true;
      playerOneTurn = false;
    }

    this.initiate();
  }
  
}
