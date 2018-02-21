# ginrummy-diproray
ginrummy-diproray created by GitHub Classroom

# Gin Rummy
Coding Assignment 5 for [CS 126](https://courses.engr.illinois.edu/cs126/sp2018/): a Java program that competes two player 
strategies against each other multiple times, and displays results.

# Design

1. Provided code: 
   * `Card.java` - a class modelling a card for Gin Rummy.
   * `Meld.java` - an abstract class modelling a meld for Gin Rummy.
      * `SetMeld.java` - models a set meld.
      * `RunMeld.java` - models a run meld.
   * `PlayerStrategy.java` - an interface for implementing a player's strategy.
2. My Code:
   * `Pile.java` - a class modelling a pile of cards for Gin Rummy game.
      * `Deck.java` - a subclass of `Pile`, that models a standard deck of 52 cards for the game.
   * `Player.java` - a class modelling a player, with a strategy, for the game
   * Implementations of `PlayerStrategy`:
      * `StrategyOne.java` - the best player strategy for the game, based on minimizing deadwood points and tracking opponent's cards.
      * `StrategyTwo.java` - an average player strategy for the game, based solely on minimzing sum of rank values
      * `StrategyThree.java` - a fairly random player strategy for the game
   * `GameEngine.java` - the engine for the game.
      * Its `main()` calls a helper function `getSummary()` which competes the strategies against each other and displays results.
