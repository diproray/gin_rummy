import com.example.*;
import java.util.*;

/*
   CS 126 Assignment 5: Gin Rummy
   Author: Dipro Ray
   Github Link: www.github.com/uiuc-sp18-cs126/ginrummy-diproray

   Special Features used:
   - Lambda functions
   - Streams and Filtering Streams

   Publicly - Available Tools Used:
   - Google's gson Library
   Publicly - Available Plugins for Pretty Printing and Code Formatting Used:
   - google-java-reformat Plugin for IntelliJ (https://github.com/google/google-java-format)
   - checkstyle Plugin for Google Style Guide (https://github.com/checkstyle/checkstyle)
*/

/**
 * . Class modelling the game engine for the Gin Rummy card game.
 *
 * @author diproray
 */
public class GameEngine {

  // Instance variables/members.

  // 2 piles for the game - a Stock Pile, and a Discard Pile
  private Pile stockPile;
  private Pile discardPile;

  // 2 Players for the game.
  private Player playerOne;
  private Player playerTwo;

  // A Player reference variable pointing to the current player - the arrayOfPlayers whose turn it
  // currently
  // is.
  private Player currentPlayer;

  // 2 Arrays storing the arrayOfPlayers and their scores - useful for switching between Players
  // easily.
  private Player[] arrayOfPlayers;
  private int[] arrayOfPlayerScores;

  /**
   * . Constructor for Game Engine.java All things that remain constant for a whole game are
   * initialized here.
   *
   * @param strategyOne the PlayerStrategy of the first Player
   * @param strategyTwo the PlayerStrategy of the second Player
   */
  public GameEngine(PlayerStrategy strategyOne, PlayerStrategy strategyTwo) {

    this.playerOne = new Player(strategyOne);
    this.playerTwo = new Player(strategyTwo);

    this.arrayOfPlayers = new Player[2];
    arrayOfPlayers[0] = playerOne;
    arrayOfPlayers[1] = playerTwo;

    this.arrayOfPlayerScores = new int[2];
    arrayOfPlayerScores[0] = 0;
    arrayOfPlayerScores[1] = 0;
  }

  /**
   * . Functions that executes an entire game of Gin Rummy.
   *
   * @return a boolean - indicating whether the first player has won or not
   */
  public boolean game() {

    boolean exitGame = false;

    while (!exitGame) {

      // Set up the games and let rounds be played till the winning score threshold (50 points) is
      // reached.
      setUp();
      initiate();
      round();

      // End the game if any player won the game (i.e. has >= 50 points).
      exitGame = (arrayOfPlayerScores[0] >= 50) || (arrayOfPlayerScores[1] >= 50);
    }

    boolean hasFirstPlayerWon = (arrayOfPlayerScores[0] >= 50);
    return hasFirstPlayerWon;
  }

  /** . Function that sets up objects for each round. */
  private void setUp() {

    // Get a new deck.
    Deck deckOfCards = new Deck();

    // Reset internal states to default for Player Strategies.
    playerOne.getStrategy().reset();
    playerTwo.getStrategy().reset();

    // Give Players their Hands.
    playerOne.setInitialHand(deckOfCards.getInitialHand());
    playerTwo.setInitialHand(deckOfCards.getInitialHand());

    // New stock and discard piles.
    this.stockPile = new Pile();
    this.discardPile = new Pile();

    // Start the discard pile with the top card of the remaining cards of the deck.
    discardPile.add(deckOfCards.getTopCard());
    // Move all remaining cards of the deck to the stock pile.
    stockPile.getPile().addAll(deckOfCards.getPile());

    // Choose player who will start the game, at random.
    Random random = new Random();
    int whichPlayerWillStartGame = random.nextInt(2);
    currentPlayer = arrayOfPlayers[whichPlayerWillStartGame];
  }

  /** . Function that executes the start of a Gin Rummy game. */
  private void initiate() {

    Card topCardOfDiscardPile = discardPile.getTopCard();

    for (int turnNumber = 1; turnNumber <= 3; turnNumber++) {

      if (turnNumber == 3) {
        // Now, at the third turn, according to gin rummy rules, the initial Player HAS to take a
        // card from the Stock Pile, and discard a card from
        // his/her Hand.
        takeCardFromChosenPileAndDiscardACard(discardPile);

      } else {

        // Check if the current player wants to take the top card of the
        // discard pile.
        boolean takeDiscardPileTopCard =
            currentPlayer.getStrategy().willTakeTopDiscard(topCardOfDiscardPile);

        // If s/he does, let him/her take a the card, and discard a card from their hand.
        if (takeDiscardPileTopCard) {
          takeCardFromChosenPileAndDiscardACard(discardPile);
          // Now, give the other player their turn.
          switchCurrentPlayer();
          break;
        }
      }

      // Now, give the other player their turn.
      switchCurrentPlayer();
    }
  }

  /** . Function that switches which player's turn it currently is. */
  private void switchCurrentPlayer() {
    for (Player player : arrayOfPlayers) {
      if (player != currentPlayer) {
        currentPlayer = player;
        break;
      }
    }
  }

  /**
   * . Function that executes picking up a card from the chosen pile, and discard a card from the
   * Hand.
   *
   * @param chosenPile the pile the Player has decided to pick up the top card from
   */
  private void takeCardFromChosenPileAndDiscardACard(Pile chosenPile) {

    Card topCardOfChosenPile = chosenPile.getTopCard();
    chosenPile.getPile().remove(topCardOfChosenPile);
    Card cardToBeDiscarded = currentPlayer.getStrategy().drawAndDiscard(topCardOfChosenPile);

    currentPlayer.addToHand(topCardOfChosenPile);

    currentPlayer.removeFromHand(cardToBeDiscarded);
    discardPile.add(cardToBeDiscarded);
  }

  /**
   * . Function executes the process of a Player selecting a pile, drawing a card from it, and
   * discarding a card.
   */
  private void makeCurrentPlayerDealACardFromAPile() {

    Card topCardOfDiscardPile = discardPile.getTopCard();
    boolean takeDiscardPileTopCard =
        currentPlayer.getStrategy().willTakeTopDiscard(topCardOfDiscardPile);

    if (takeDiscardPileTopCard) {
      takeCardFromChosenPileAndDiscardACard(discardPile);
    } else {
      takeCardFromChosenPileAndDiscardACard(stockPile);
    }
  }

  /** . Function that executes alternating Player turns in a round of the game. */
  private void round() {

    while (true) {

      // If the Stock Pile is over, the round ends and no Player gets any points.
      if (stockPile.getPile().size() == 0) {
        return;
      }

      // If after the dealing the current player has less than 10 deadwood points, he/she can choose
      // to Knock.
      makeCurrentPlayerDealACardFromAPile();

      if (GameEngineUtils.getDeadwoodPoints(currentPlayer) <= 10) {

        boolean willKnock = currentPlayer.getStrategy().knock();

        if (willKnock) {
          System.out.println("Knock");
          knocking(currentPlayer);
          return;
        }
      }

      // It is now the other player's turn.
      switchCurrentPlayer();
    }
  }




  /**
   * . Function that executes Knocking.
   *
   * @param currentPlayer the player whose turn it currently is
   */
  private void knocking(Player currentPlayer) {

    // Get the other player.
    Player otherPlayer = new Player(null);
    for (Player player : arrayOfPlayers) {
      if (player != currentPlayer) {
        otherPlayer = player;
        break;
      }
    }

    ArrayList<Meld> currentPlayerMelds = GameEngineUtils.getListOfMelds(currentPlayer);
    ArrayList<Card> otherPlayerDeadwoodCards = GameEngineUtils.getDeadwoodCardsList(otherPlayer);

    // Try adding other Player's deadwood cards to knocker's melds.
    // For each card among the other player's deadwood cards, check to see if it can be added to
    // knocking player's melds. If it can be, add it to the knocking player's melds and remove from
    // other player's deadwood cards list.

    for (Card card : otherPlayerDeadwoodCards) {
      for (Meld meld : currentPlayerMelds) {

        boolean canAppendToMeld = meld.canAppendCard(card);

        if (canAppendToMeld) {
          meld.appendCard(card);
          otherPlayerDeadwoodCards.remove(card);
          break;
        }

      }
    }

    // Calculate other player's deadwood points.
    int otherPlayerDeadwoodPoints = 0;
    for (Card card : otherPlayerDeadwoodCards) {
      otherPlayerDeadwoodPoints += card.getPointValue();
    }

    // Calculate current player's deadwood points.
    int currentPlayerDeadwoodPoints = GameEngineUtils.getDeadwoodPoints(currentPlayer);

    assignScores(currentPlayerDeadwoodPoints, otherPlayerDeadwoodPoints, otherPlayer);
  }

  private void assignScores(int currentPlayerDeadwoodPoints, int otherPlayerDeadwoodPoints, Player otherPlayer) {

    // If current player's deadwood points is 0, it is a GIN and points are allotted accordingly
    // with a bonus for the knocker.
    if (currentPlayerDeadwoodPoints == 0
        && otherPlayerDeadwoodPoints > currentPlayerDeadwoodPoints) {
      for (int i = 0; i < arrayOfPlayerScores.length; i++) {
        if (arrayOfPlayers[i] == currentPlayer) {
          arrayOfPlayerScores[i] += (25 + otherPlayerDeadwoodPoints);
          return;
        }
      }
    }

    // If the other player has more deadwood points, points are allotted to players accordingly.
    if (otherPlayerDeadwoodPoints > currentPlayerDeadwoodPoints) {
      for (int i = 0; i < arrayOfPlayerScores.length; i++) {
        if (arrayOfPlayers[i] == currentPlayer) {
          arrayOfPlayerScores[i] += (otherPlayerDeadwoodPoints - currentPlayerDeadwoodPoints);
          return;
        }
      }
    }

    // If the knocker has more deadwood points, points are allotted to players with a bonus for the
    // other player.
    if (otherPlayerDeadwoodPoints < currentPlayerDeadwoodPoints) {
      for (int i = 0; i < arrayOfPlayerScores.length; i++) {
        if (arrayOfPlayers[i] == otherPlayer) {
          arrayOfPlayerScores[i] += (25 + currentPlayerDeadwoodPoints - otherPlayerDeadwoodPoints);
          return;
        }
      }
    }
  }
}
