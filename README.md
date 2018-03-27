# ginrummy-diproray
ginrummy-diproray created by GitHub Classroom

# Gin Rummy
[Redone for Spring Break Assignment] Coding Assignment 5 for [CS 126](https://courses.engr.illinois.edu/cs126/sp2018/): a Java program that competes two player 
strategies against each other multiple times, and displays results.

# Design | Object Decomposition [Top-Down order]
  ## Summary:
`Userinterface.java` models the user interface - guides games to be played by players and publishes a summary of results. `Player.java` represents a player, who plays the game. `GameEngine.java` actually plays the game between two players, according to the rules of the game. Game Elements - like cards, melds etc. - are modelled by `Meld.java`, `Card.java`, `Pile.java`, `Deck.java` etc.

## Detailed Explanation:
   ### The Interface
   * `Userinterface.java` 
      * Its `main` function is the function to be run.
      * Its `main()` calls a helper function `getSummary()` which competes the strategies against each other and displays results. 
   ### The Engine for the Game
   * `GameEngine.java` - the engine for the game.
   * `GameEngineUtils.java` - class the contains helper functions for `GameEngine`
   ### The Player of the Game (and his/her Strategy)
   * `Player.java` - a class modelling a player, with a `PlayerStrategy`, for the game
      * `PlayerStrategy.java` - an interface for implementing a player's strategy.
   ### Player Strategies
      * Implementations of `PlayerStrategy`:
      * `StrategyOne.java` - the best player strategy for the game, based on minimizing deadwood points and tracking opponent's cards.
      * `StrategyTwo.java` - an average player strategy for the game, based solely on minimzing sum of rank values
      * `StrategyThree.java` - a fairly random player strategy for the game
   ### Cards, Melds etc. for the Game
   * `Pile.java` - a class modelling a pile of cards for Gin Rummy game.
      * `Deck.java` - a subclass of `Pile`, that models a standard deck of 52 cards for the game.
   * `Meld.java` - an abstract class modelling a meld for Gin Rummy.
      * `SetMeld.java` - models a set meld.
      * `RunMeld.java` - models a run meld.
   * `Card.java` - a class modelling a card for Gin Rummy.

