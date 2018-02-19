import com.example.*;

import java.util.*;
import java.util.stream.Collectors;

import static com.example.Card.CardSuit.*;

public class GameEngine {

  enum CardSuit {
    DIAMONDS,
    HEARTS,
    SPADES,
    CLUBS
  }

  enum CardRank {
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

  private Deck deck;
  private Pile stockPile;
  private Pile discardPile;
  private Player playerOne;
  private Player playerTwo;
  private Player currentPlayer;
  private Player[] players;
  private int[] playersScores;

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

  public int round() {

    while (true) {
      if (playersScores[0] >= 50) {
        return 1;
      }
      if (playersScores[2] >= 50) {
        return 2;
      }
      setUp();
      initiate();
      game();
    }
  }

  public void setUp() {
    this.deck = new Deck(); // every round
    this.stockPile = new Pile(); // every round
    this.discardPile = new Pile(); // every round

    playerOne.setInitialHand(deck.getInitialHand()); // every round
    playerTwo.setInitialHand(deck.getInitialHand()); // every round

    discardPile.add(deck.getTopCard()); // every round
    stockPile.getPile().addAll(deck.getPile()); // every round

    // every round
    Random random = new Random();
    int whichPlayer = random.nextInt(2);
    currentPlayer = players[whichPlayer];
  }

  public void initiate() {

    Card topCardOfDiscardPile = discardPile.getTopCard();
    boolean takeDiscardPileTopCard =
        currentPlayer.getStrategy().willTakeTopDiscard(topCardOfDiscardPile);

    if (takeDiscardPileTopCard) {
      takeCardFromChosenPileAndDiscardACard(discardPile);
      switchCurrentPlayer();
      return;

    } else {
      switchCurrentPlayer();
    }

    takeDiscardPileTopCard = currentPlayer.getStrategy().willTakeTopDiscard(topCardOfDiscardPile);

    if (takeDiscardPileTopCard) {
      takeCardFromChosenPileAndDiscardACard(discardPile);
      switchCurrentPlayer();
      return;
    } else {
      switchCurrentPlayer();
    }

    takeCardFromChosenPileAndDiscardACard(stockPile);
    switchCurrentPlayer();
    return;
  }

  public void switchCurrentPlayer() {
    for (Player player : players) {
      if (player != currentPlayer) {
        currentPlayer = player;
        break;
      }
    }
  }

  public void takeCardFromChosenPileAndDiscardACard(Pile chosenPile) {

    Card topCardOfChosenPile = chosenPile.getTopCard();

    currentPlayer.addToHand(topCardOfChosenPile);

    Card cardToBeDiscarded = currentPlayer.getStrategy().drawAndDiscard(topCardOfChosenPile);
    currentPlayer.removeFromHand(cardToBeDiscarded);
    discardPile.add(cardToBeDiscarded);
  }

  public void makeCurrentPlayerDealACardFromAPile() {

    Card topCardOfDiscardPile = discardPile.getTopCard();
    boolean takeDiscardPileTopCard =
        currentPlayer.getStrategy().willTakeTopDiscard(topCardOfDiscardPile);

    if (takeDiscardPileTopCard) {
      takeCardFromChosenPileAndDiscardACard(discardPile);
    } else {
      takeCardFromChosenPileAndDiscardACard(stockPile);
    }
  }

  public void game() {

    while (true) {

      if (stockPile.getPile().size() == 0) {
        // New Round - figure out how to reset everything for a new round.
        return;
      }
      // KNOCK or GIN
      makeCurrentPlayerDealACardFromAPile();
      if (getDeadwoodPoints(currentPlayer) <= 10) {
        boolean willKnock = currentPlayer.getStrategy().knock();
        if (willKnock) {
          knocking(currentPlayer);
          return;
        }
      }
      switchCurrentPlayer();
    }
  }

  public int getDeadwoodPoints(Player player) {

    ArrayList<Card> deadwoodCardsList = getDeadwoodCardsList(player);
    int deadwoodPoints = 0;
    for (Card card : deadwoodCardsList) {
      deadwoodPoints += card.getPointValue();
    }

    return deadwoodPoints;
  }

  public ArrayList<Card> getDeadwoodCardsList(Player player) {
    ArrayList<Card> playersHand = player.getHand();
    ArrayList<Card> playersHandSortedByRank = playersHand;
    Collections.sort(playersHandSortedByRank);

    for (CardRank rank : EnumSet.allOf(CardRank.class)) {

      List<Card> cardsOfThisRank =
          playersHandSortedByRank
              .stream()
              .filter(c -> rank.ordinal() == c.getRank().ordinal())
              .collect(Collectors.toList());

      SetMeld setMeld = SetMeld.buildSetMeld(cardsOfThisRank);

      if (setMeld != null) {
        playersHandSortedByRank.removeAll(cardsOfThisRank);
      }
    }

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
    Collections.sort(playersHandSortedBySuitAndRank, cardSuitAndRankComparator);

    for (CardSuit suit : EnumSet.allOf(CardSuit.class)) {
      List<Card> cardsOfThisSuit =
          playersHandSortedBySuitAndRank
              .stream()
              .filter(c -> suit.ordinal() == c.getSuit().ordinal())
              .collect(Collectors.toList());

      ArrayList<Card> cardsOfThisSuitCopy = new ArrayList<>(cardsOfThisSuit);

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

  public ArrayList<Meld> getListOfMelds(Player player) {
    ArrayList<Meld> listOfMelds = new ArrayList<>();
    ArrayList<Card> playersHand = player.getHand();
    ArrayList<Card> playersHandSortedByRank = playersHand;
    Collections.sort(playersHandSortedByRank);

    for (CardRank rank : EnumSet.allOf(CardRank.class)) {

      List<Card> cardsOfThisRank =
          playersHandSortedByRank
              .stream()
              .filter(c -> rank.ordinal() == c.getRank().ordinal())
              .collect(Collectors.toList());

      SetMeld setMeld = SetMeld.buildSetMeld(cardsOfThisRank);

      if (setMeld != null) {
        listOfMelds.add(setMeld);
        playersHandSortedByRank.removeAll(cardsOfThisRank);
      }
    }

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
    Collections.sort(playersHandSortedBySuitAndRank, cardSuitAndRankComparator);

    for (CardSuit suit : EnumSet.allOf(CardSuit.class)) {
      List<Card> cardsOfThisSuit =
          playersHandSortedBySuitAndRank
              .stream()
              .filter(c -> suit.ordinal() == c.getSuit().ordinal())
              .collect(Collectors.toList());

      ArrayList<Card> cardsOfThisSuitCopy = new ArrayList<>(cardsOfThisSuit);

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

  public void knocking(Player currentPlayer) {

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

    int otherPlayerDeadwoodPoints = 0;
    for (Card card : otherPlayerDeadwoodCards) {
      otherPlayerDeadwoodPoints += card.getPointValue();
    }

    int currentPlayerDeadwoodPoints = getDeadwoodPoints(currentPlayer);

    if (currentPlayerDeadwoodPoints == 0
        && otherPlayerDeadwoodPoints > currentPlayerDeadwoodPoints) {
      for (int i = 0; i < playersScores.length; i++) {
        if (players[i] == currentPlayer) {
          playersScores[i] += (25 + otherPlayerDeadwoodPoints);
          return;
        }
      }
    }

    if (otherPlayerDeadwoodPoints > currentPlayerDeadwoodPoints) {
      for (int i = 0; i < playersScores.length; i++) {
        if (players[i] == currentPlayer) {
          playersScores[i] += (otherPlayerDeadwoodPoints - currentPlayerDeadwoodPoints);
          return;
        }
      }
    }

    if (otherPlayerDeadwoodPoints < currentPlayerDeadwoodPoints) {
      for (int i = 0; i < playersScores.length; i++) {
        if (players[i] == otherPlayer) {
          playersScores[i] += (25 + currentPlayerDeadwoodPoints - otherPlayerDeadwoodPoints);
          return;
        }
      }
    }

    return;
  }

  // Put everything into one main driver function for each round that calls other small helper
  // functions.
  // Have a GameEngine constructor and a Round Reset function to reset everything.
}
