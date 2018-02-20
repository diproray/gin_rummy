import com.example.*;
import com.example.StrategyOne;
import com.example.StrategyThree;
import com.example.StrategyTwo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/*
   CS 126 Assignment 5: Gin Rummy
   Author: Dipro Ray
   Github Link: www.github.com/uiuc-sp18-cs126/ginrummy-diproray

   Special Features used:
   - Lamda functions
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
@SuppressWarnings("unused")
public class GameEngine {

  /**
   * . Main function to be run
   *
   * @param args
   */
  public static void main(String[] args) {

    StrategyOne s1 = new StrategyOne();
    StrategyTwo s2 = new StrategyTwo();
    StrategyThree s3 = new StrategyThree();

    int[] players = new int[3];

    players[0] = 0;
    players[1] = 0;
    players[2] = 0;

    int numberOfGames = 1000;
    for (int i = 0; i < numberOfGames; i++) {
      GameEngine ge1 = new GameEngine(s1, s2);
      GameEngine ge2 = new GameEngine(s2, s3);
      GameEngine ge3 = new GameEngine(s1, s3);

      if (ge1.round() == 1) {
        players[0]++;
      } else {
        players[1]++;
      }

      if (ge2.round() == 1) {
        players[1]++;
      } else {
        players[2]++;
      }

      if (ge3.round() == 1) {
        players[0]++;
      } else {
        players[2]++;
      }
    }

    System.out.println("SUMMARY of All Gin Rummy Games: ");
    System.out.println(
        "Player 1: \t"
            + "Games Won (out of " + 2 * numberOfGames + ") - "
            + players[0]
            + ", \tWin Percentage - "
            + (float) ((players[0] * 100) / (2 * numberOfGames))
            + "%.");
    System.out.println(
        "Player 2: \t"
            + "Games Won (out of "+ 2 * numberOfGames + ") - "
            + players[1]
            + ", \tWin Percentage - "
            + (float) ((players[1] * 100) / (2 * numberOfGames))
            + "%.");
    System.out.println(
        "Player 3: \t"
            + "Games Won (out of "+ 2 * numberOfGames + ") - "
            + players[2]
            + ",    \tWin Percentage - "
            + (float) ((players[2] * 100) / (2 * numberOfGames))
            + "%.");
  }

  // Enums modelling all possible ranks and suits for a standard deck of 52 cards.
  public enum CardSuit {
    DIAMONDS,
    HEARTS,
    SPADES,
    CLUBS
  }

  public enum CardRank {
    ACE,
    TWO,
    THREE,
    FOUR,
    FIVE,
    SIX,
    SEVEN,
    EIGHT,
    NINE,
    TEN,
    JACK,
    QUEEN,
    KING
  }

  // Instance variables/members.

  // A standard deck of 52 cards.
  private Deck deck;

  // 2 piles for the game - a Stock Pile, and a Discard Pile
  private Pile stockPile;
  private Pile discardPile;

  // 2 Players for the game.
  private Player playerOne;
  private Player playerTwo;

  // A Player reference variable pointing to the current player - the players whose turn it
  // currently
  // is.
  private Player currentPlayer;

  // 2 Arrays storing the players and their scores - useful for switching between Players easily.
  private Player[] players;
  private int[] playersScores;

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

    this.players = new Player[2];
    players[0] = playerOne;
    players[1] = playerTwo;

    playersScores = new int[2];
    playersScores[0] = 0;
    playersScores[1] = 0;
  }

  /**
   * . Functions that executes rounds of a game, until the game ends.
   *
   * @return an integer - indicating which player wins
   */
  public int round() {
    int i = 0;
    while (true) {
      i++;
      System.out.print("Round " + i + ": ");
      // End the game if any player won the game (i.e. has >= 50 points).
      if (playersScores[0] >= 50) {
        System.out.println("Game ends now. 1 wins.");
        return 1;
      }
      if (playersScores[1] >= 50) {
        System.out.println("Game ends now. 2 wins.");
        return 2;
      }

      setUp();
      initiate();
      game();

      System.out.print("Player 1: " + playersScores[0] + "; ");
      System.out.println("Player 2: " + playersScores[1]);
    }
  }

  /** . Function that sets up/resets objects for each round. */
  private void setUp() {

    // New deck.
    this.deck = new Deck();

    // New piles.
    this.stockPile = new Pile();
    this.discardPile = new Pile();

    // Reset internal states of Player Strategies.
    playerOne.getStrategy().reset();
    playerTwo.getStrategy().reset();

    // Give Players their Hands.
    playerOne.setInitialHand(deck.getInitialHand());
    playerTwo.setInitialHand(deck.getInitialHand());

    // Start the discard pile with a card.
    discardPile.add(deck.getTopCard());
    stockPile.getPile().addAll(deck.getPile());

    // Choose player who will start the game, at random.
    Random random = new Random();
    int whichPlayer = random.nextInt(2);
    currentPlayer = players[whichPlayer];
  }

  /** . Function that executes the start of a Gin Rummy game. */
  private void initiate() {

    Card topCardOfDiscardPile = discardPile.getTopCard();

    // Check if the current player, choen at random in setUp(), wants to take the top card of the
    // discard pile.

    boolean takeDiscardPileTopCard =
        currentPlayer.getStrategy().willTakeTopDiscard(topCardOfDiscardPile);

    // If s/he does, let him/her take a the card, and discard a card from their hand.
    if (takeDiscardPileTopCard) {
      takeCardFromChosenPileAndDiscardACard(discardPile);
      switchCurrentPlayer();
      return;

    } else {
      // Else, give the other player their turn.
      switchCurrentPlayer();
    }

    // Check if the current player wants to take the top card of the discard pile.
    takeDiscardPileTopCard = currentPlayer.getStrategy().willTakeTopDiscard(topCardOfDiscardPile);

    // If s/he does, let him/her take a the card, and discard a card from their hand.
    if (takeDiscardPileTopCard) {
      takeCardFromChosenPileAndDiscardACard(discardPile);
      switchCurrentPlayer();
      return;
    } else {
      // Else, give the other player their turn.
      switchCurrentPlayer();
    }

    // Now, the initial Player HAS to take a card from the Stock Pile, and discard a card from
    // his/her Hand.
    takeCardFromChosenPileAndDiscardACard(stockPile);
    switchCurrentPlayer();
  }

  /** . Function that switches which player's turn it currently is. */
  private void switchCurrentPlayer() {
    for (Player player : players) {
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
  private void game() {

    while (true) {

      // If the Stock Pile is over, the round ends and no Player gets any points.
      if (stockPile.getPile().size() == 0) {
        return;
      }

      // System.out.println("Stock Pile Size: " + stockPile.getPile().size());
      // If after the dealing the current player has less than 10 deadwood points, he/she can choose
      // to Knock.
      makeCurrentPlayerDealACardFromAPile();
      if (getDeadwoodPoints(currentPlayer) <= 10) {
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
   * . Getter for deadwood points for a player
   *
   * @param player the player
   * @return an int value
   */
  private int getDeadwoodPoints(Player player) {

    ArrayList<Card> deadwoodCardsList = getDeadwoodCardsList(player);
    int deadwoodPoints = 0;
    for (Card card : deadwoodCardsList) {
      deadwoodPoints += card.getPointValue();
    }

    return deadwoodPoints;
  }

  /**
   * . Functions returns a list of deadwood cards for a Player.
   *
   * @param player the player
   * @return sum of deadwood points for the cards
   */
  private ArrayList<Card> getDeadwoodCardsList(Player player) {
    ArrayList<Card> playersHand = player.getHand();
    ArrayList<Card> playersHandSortedByRank = playersHand;
    // Sort by rank.
    Collections.sort(playersHandSortedByRank);

    for (CardRank rank : EnumSet.allOf(CardRank.class)) {

      // Get a list of Cards of a particular rank.
      List<Card> cardsOfThisRank =
          playersHandSortedByRank
              .stream()
              .filter(c -> rank.ordinal() == c.getRank().ordinal())
              .collect(Collectors.toList());

      // Try to form a Set Meld with it.
      // If successful, remove these cards from the list of cards of player.
      SetMeld setMeld = SetMeld.buildSetMeld(cardsOfThisRank);

      if (setMeld != null) {
        playersHandSortedByRank.removeAll(cardsOfThisRank);
      }
    }

    // Comparator to compare Cards based on suit and rank.
    Comparator<Card> cardSuitAndRankComparator =
        (cardOne, cardTwo) -> {
          Card.CardSuit cardOneSuit = cardOne.getSuit();
          Card.CardSuit cardTwoSuit = cardTwo.getSuit();
          int whoHasGreaterRank = cardOne.compareTo(cardTwo);

          if (cardOneSuit.ordinal() < cardTwoSuit.ordinal()) {
            return -1;
          } else if (cardOneSuit.ordinal() > cardTwoSuit.ordinal()) {
            return 1;
          } else {
            return whoHasGreaterRank;
          }
        };

    ArrayList<Card> playersHandSortedBySuitAndRank = playersHandSortedByRank;
    // Sort cards by suit, AND by rank.
    Collections.sort(playersHandSortedBySuitAndRank, cardSuitAndRankComparator);

    for (CardSuit suit : EnumSet.allOf(CardSuit.class)) {
      // Get list of cards of a particular suit.

      ArrayList<Card> cardsOfThisSuitCopy =
          playersHandSortedBySuitAndRank
              .stream()
              .filter(c -> suit.ordinal() == c.getSuit().ordinal())
              .collect(Collectors.toCollection(ArrayList::new));

      if (cardsOfThisSuitCopy.size() == 0) {
        continue;
      }
      // Try forming Run melds.
      // If successful, remove these cards from the list of cards of player.
      ArrayList<Card> tempListOfCards = new ArrayList<>();
      tempListOfCards.add(cardsOfThisSuitCopy.get(cardsOfThisSuitCopy.size() - 1));

      Card previousCard = cardsOfThisSuitCopy.get(cardsOfThisSuitCopy.size() - 1);

      for (int i = cardsOfThisSuitCopy.size() - 2; i >= 0; i--) {

        Card currentCard = cardsOfThisSuitCopy.get(i);

        if (currentCard.getRank().ordinal() == (previousCard.getRank().ordinal() - 1)) {
          tempListOfCards.add(currentCard);
        } else {

          RunMeld runMeld = RunMeld.buildRunMeld(tempListOfCards);
          if (runMeld != null) {
            playersHandSortedBySuitAndRank.removeAll(tempListOfCards);
          }
          tempListOfCards = new ArrayList<>();
          tempListOfCards.add(currentCard);
        }

        previousCard = currentCard;
      }

      RunMeld runMeld = RunMeld.buildRunMeld(tempListOfCards);
      if (runMeld != null) {
        playersHandSortedBySuitAndRank.removeAll(tempListOfCards);
      }
    }

    ArrayList<Card> deadwoodCards = new ArrayList<>(playersHandSortedBySuitAndRank);

    return deadwoodCards;
  }

  /**
   * . Function returns a list of melds of the current player
   *
   * @param player the player
   * @return a list of melds
   */
  private ArrayList<Meld> getListOfMelds(Player player) {

    ArrayList<Meld> listOfMelds = new ArrayList<>();
    ArrayList<Card> playersHand = player.getHand();
    ArrayList<Card> playersHandSortedByRank = playersHand;
    // Sort by rank.
    Collections.sort(playersHandSortedByRank);

    for (CardRank rank : EnumSet.allOf(CardRank.class)) {

      // Code below derived from:
      // Stack Overflow post -
      // https://stackoverflow.com/questions/122105/what-is-the-best-way-to-filter-a-java-collection
      // Makes use of Lambda functions and streams, features of Java 8.

      // Get a list of Cards of a particular rank.
      List<Card> cardsOfThisRank =
          playersHandSortedByRank
              .stream()
              .filter(c -> rank.ordinal() == c.getRank().ordinal())
              .collect(Collectors.toList());

      // Try to form a Set Meld with it.
      // If successful, add this meld to the list of all melds.
      SetMeld setMeld = SetMeld.buildSetMeld(cardsOfThisRank);

      if (setMeld != null) {
        listOfMelds.add(setMeld);
        playersHandSortedByRank.removeAll(cardsOfThisRank);
      }
    }

    // Comparator to compare Cards based on suit and rank.
    Comparator<Card> cardSuitAndRankComparator =
        (cardOne, cardTwo) -> {
          Card.CardSuit cardOneSuit = cardOne.getSuit();
          Card.CardSuit cardTwoSuit = cardTwo.getSuit();
          int whoHasGreaterRank = cardOne.compareTo(cardTwo);

          if (cardOneSuit.ordinal() < cardTwoSuit.ordinal()) {
            return -1;
          } else if (cardOneSuit.ordinal() > cardTwoSuit.ordinal()) {
            return 1;
          } else {
            return whoHasGreaterRank;
          }
        };

    // Sort cards by suit, AND by rank.
    ArrayList<Card> playersHandSortedBySuitAndRank = playersHandSortedByRank;
    Collections.sort(playersHandSortedBySuitAndRank, cardSuitAndRankComparator);

    for (CardSuit suit : EnumSet.allOf(CardSuit.class)) {

      // Get list of cards of a particular suit.

      ArrayList<Card> cardsOfThisSuitCopy =
          playersHandSortedBySuitAndRank
              .stream()
              .filter(c -> suit.ordinal() == c.getSuit().ordinal())
              .collect(Collectors.toCollection(ArrayList::new));

      if (cardsOfThisSuitCopy.size() == 0) {
        continue;
      }

      // Try forming Run melds.
      // If successful, add it to the list of melds.
      ArrayList<Card> tempListOfCards = new ArrayList<>();
      tempListOfCards.add(cardsOfThisSuitCopy.get(cardsOfThisSuitCopy.size() - 1));

      Card previousCard = cardsOfThisSuitCopy.get(cardsOfThisSuitCopy.size() - 1);

      for (int i = cardsOfThisSuitCopy.size() - 2; i >= 0; i--) {

        Card currentCard = cardsOfThisSuitCopy.get(i);

        if (currentCard.getRank().ordinal() == (previousCard.getRank().ordinal() - 1)) {
          tempListOfCards.add(currentCard);
        } else {

          RunMeld runMeld = RunMeld.buildRunMeld(tempListOfCards);
          if (runMeld != null) {
            listOfMelds.add(runMeld);
            playersHandSortedBySuitAndRank.removeAll(tempListOfCards);
          }
          tempListOfCards = new ArrayList<>();
          tempListOfCards.add(currentCard);
        }

        previousCard = currentCard;
      }

      RunMeld runMeld = RunMeld.buildRunMeld(tempListOfCards);
      if (runMeld != null) {
        listOfMelds.add(runMeld);
        playersHandSortedBySuitAndRank.removeAll(tempListOfCards);
      }
    }

    return listOfMelds;
  }

  /**
   * . Function that executes Knocking.
   *
   * @param currentPlayer the player whose turn it currently is
   */
  private void knocking(Player currentPlayer) {

    // Get the other player.
    Player otherPlayer = new Player(null);
    for (Player player : players) {
      if (player != currentPlayer) {
        otherPlayer = player;
        break;
      }
    }

    ArrayList<Meld> currentPlayerMelds = getListOfMelds(currentPlayer);
    ArrayList<Card> otherPlayerDeadwoodCards = getDeadwoodCardsList(otherPlayer);
    ArrayList<Card> otherPlayerDeadwoodCardsCopy = new ArrayList<>(otherPlayerDeadwoodCards);

    // Try adding other Player's deadwood cards to knocker's melds.
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
    int currentPlayerDeadwoodPoints = getDeadwoodPoints(currentPlayer);

    // If current player's deadwood points is 0, it is a GIN and points are allotted accordingly
    // with a bonus for the knocker.
    if (currentPlayerDeadwoodPoints == 0
        && otherPlayerDeadwoodPoints > currentPlayerDeadwoodPoints) {
      for (int i = 0; i < playersScores.length; i++) {
        if (players[i] == currentPlayer) {
          playersScores[i] += (25 + otherPlayerDeadwoodPoints);
          return;
        }
      }
    }

    // If the other player has more deadwood points, points are allotted to players accordingly.
    if (otherPlayerDeadwoodPoints > currentPlayerDeadwoodPoints) {
      for (int i = 0; i < playersScores.length; i++) {
        if (players[i] == currentPlayer) {
          playersScores[i] += (otherPlayerDeadwoodPoints - currentPlayerDeadwoodPoints);
          return;
        }
      }
    }

    // If the knocker has more deadwood points, points are allotted to players with a bonus for the
    // other player.
    if (otherPlayerDeadwoodPoints < currentPlayerDeadwoodPoints) {
      for (int i = 0; i < playersScores.length; i++) {
        if (players[i] == otherPlayer) {
          playersScores[i] += (25 + currentPlayerDeadwoodPoints - otherPlayerDeadwoodPoints);
          return;
        }
      }
    }
  }
}
