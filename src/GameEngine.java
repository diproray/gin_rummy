import com.example.*;
import java.util.*;

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

    // The top card from the pile which the player has chosen to take a card from.
    Card topCardOfChosenPile = chosenPile.getTopCard();

    // Remove this card from the respective pile.
    chosenPile.getPile().remove(topCardOfChosenPile);

    // Find out which card the player wants to discard.
    Card cardToBeDiscarded = currentPlayer.getStrategy().drawAndDiscard(topCardOfChosenPile);

    // Add the top card of the chosen pile to the player's hand.
    currentPlayer.addToHand(topCardOfChosenPile);

    // Remove the card to be discard from the player's hand.
    currentPlayer.removeFromHand(cardToBeDiscarded);

    // Add the discarded card to the discard pile.
    discardPile.add(cardToBeDiscarded);
  }

  /**
   * . Function executes the process of a Player selecting a pile, drawing a card from it, and
   * discarding a card.
   */
  private void makeCurrentPlayerDealACardFromAPile() {

    // Get the top card of the discard pile.
    Card topCardOfDiscardPile = discardPile.getTopCard();

    // Ask the player if it wants to take that top card.
    boolean takeDiscardPileTopCard =
        currentPlayer.getStrategy().willTakeTopDiscard(topCardOfDiscardPile);

    // If the player does want to take it, let it do so.
    // Else, choose the top card from the stock pile.

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

      // Ask current player to deal a card from a pile.
      makeCurrentPlayerDealACardFromAPile();

      // If after the dealing the current player has less than 10 deadwood points, he/she can choose
      // to Knock.
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

    // Get the current player's melds and other player's list of deadwood cards.

    ArrayList<Meld> currentPlayerMelds = GameEngineUtils.getListOfMelds(currentPlayer);
    ArrayList<Card> otherPlayerDeadwoodCards = GameEngineUtils.getDeadwoodCardsList(otherPlayer);
    ArrayList<Card> otherPlayerDeadwoodCardsCopy = new ArrayList(otherPlayerDeadwoodCards);

    // Try adding other Player's deadwood cards to knocker's melds.
    // For each card among the other player's deadwood cards, check to see if it can be added to
    // knocking player's melds. If it can be, add it to the knocking player's melds and remove from
    // other player's deadwood cards list.

    for (Card card : otherPlayerDeadwoodCardsCopy) {
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

    // Once the process of knocing is over, assign scores appropriately.
    assignScores(currentPlayerDeadwoodPoints, otherPlayerDeadwoodPoints, otherPlayer);
  }

  /**
   * . Function that executes the task of assignment of scores after knocking.
   *
   * @param currentPlayerDeadwoodPoints the deadwood points of the current player
   * @param otherPlayerDeadwoodPoints the deadwood points of the other player
   * @param otherPlayer the player who did NOT knock
   */
  private void assignScores(
      int currentPlayerDeadwoodPoints, int otherPlayerDeadwoodPoints, Player otherPlayer) {

    // There are three possible cases during scores assignment.

    // CASE #1:
    // If current player's deadwood points is 0, it is a GIN and 25 points are allotted to the
    // knock accordingly
    // with a bonus for the knocker (whose value is equal to the other player's deadwood points).
    if (currentPlayerDeadwoodPoints == 0
        && otherPlayerDeadwoodPoints > currentPlayerDeadwoodPoints) {

      for (int i = 0; i < arrayOfPlayerScores.length; i++) {
        if (arrayOfPlayers[i] == currentPlayer) {
          arrayOfPlayerScores[i] += (25 + otherPlayerDeadwoodPoints);
          return;
        }
      }
    }

    // CASE #2:
    // If the other player has more deadwood points, (otherPlayerDeadwoodPoints -
    // currentPlayerDeadwoodPoints) points are allotted to the knocker accordingly.
    if (otherPlayerDeadwoodPoints > currentPlayerDeadwoodPoints) {
      for (int i = 0; i < arrayOfPlayerScores.length; i++) {
        if (arrayOfPlayers[i] == currentPlayer) {
          arrayOfPlayerScores[i] += (otherPlayerDeadwoodPoints - currentPlayerDeadwoodPoints);
          return;
        }
      }
    }

    // CASE #3:
    // If the knocker has more deadwood points, 25 points are allotted to the other player with a
    // bonus (whose value is (currentPlayerDeadwoodPoints - otherPlayerDeadwoodPoints))for the
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
