import com.example.Card;
import com.example.Meld;
import com.example.RunMeld;
import com.example.SetMeld;
import org.paukov.combinatorics3.Generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * . Class that contains functions for building melds and getting deadwood cards for players.
 *
 * @author diproray
 */
public class GameEngineUtils {

  /**
   * . Functions returns a list of deadwood cards for a Player.
   *
   * @param player the player
   * @return the list of deadwood cards of the player
   */
  public static ArrayList<Card> getDeadwoodCardsList(Player player) {

    // Get all the cards which are present in melds, as a list.

    ArrayList<Meld> listOfPlayerMelds = getListOfMelds(player);
    ArrayList<Card> listOfMeldCards = new ArrayList<>();

    for (Meld meld : listOfPlayerMelds) {
      Card[] arrayOfCardsInMeld = meld.getCards();
      listOfMeldCards.addAll(Arrays.asList(arrayOfCardsInMeld));
    }

    // Since deadwood cards of a player are all the cards in his/her hand
    // that are NOT present in melds,
    // We get a list of deadwood cards by removing all the meld cards from the player's hand cards.

    ArrayList deadwoodCards = new ArrayList(player.getHand());
    deadwoodCards.removeAll(listOfMeldCards);

    // Return the list of deadwood cards.
    return deadwoodCards;
  }

  /**
   * . Getter for deadwood points for a player.
   *
   * @param player the player
   * @return an int value - the sum of the points of the deadwood cards of the player
   */
  public static int getDeadwoodPoints(Player player) {

    // Get a list of deadwood cards for the player.
    ArrayList<Card> deadwoodCardsList = GameEngineUtils.getDeadwoodCardsList(player);

    // Compute the sum of the values of the deadwood cards.

    int deadwoodPoints = 0;
    for (Card card : deadwoodCardsList) {
      deadwoodPoints += card.getPointValue();
    }

    // Return the total deadwood points value
    return deadwoodPoints;
  }

  /**
   * . Function returns a list of melds of the current player
   *
   * @param player the player
   * @return a list of melds for the player
   */
  public static ArrayList<Meld> getListOfMelds(Player player) {

    ArrayList<Meld> listOfMelds = new ArrayList<>();

    // Get a copy of the player's hand.
    // (A copy is required because we need to modify it, but we must not lose the original data.)
    ArrayList<Card> playersHandCopy = new ArrayList<>(player.getHand());

    // The piece of code
    // (Generator.combination(playersHandCopy).simple(3).stream().collect(Collectors.toCollection(ArrayList::new)))
    // makes use of the Generator class in the combinatoricslib3 library which is publicly
    // available.
    //
    // The piece of code, in a general sense, does something like the following:
    // Input to the code [the list passed to it]: [1, 2, 3]
    // Output from the code: [[1, 2], [1, 3], [2, 3]]
    //
    // In our case, it takes a ArrayList<Card> and returns a List of all possible combinations of 3
    // Cards from the List.
    //
    // For each possible group of 3 cards,
    // try building a set meld or a run meld.
    // If it is possible, add the meld to the list of melds.
    //
    // We compute all possible combinations again and again because playersHandCopy keeps changing.

    for (List<Card> listOfThreeCards :
        (Generator.combination(playersHandCopy)
            .simple(3)
            .stream()
            .collect(Collectors.toCollection(ArrayList::new)))) {

      // Try building a run meld and a set meld.

      SetMeld setMeld = Meld.buildSetMeld(listOfThreeCards);
      RunMeld runMeld = Meld.buildRunMeld(listOfThreeCards);

      // If a meld is built, add the meld to the list of melds.
      // Then, remove those cards from the player's hand cards list.
      // This needs to be done to prevent those same cards, already used up in a meld, from being
      // used again in a different meld.

      if (setMeld != null) {

        listOfMelds.add(setMeld);
        playersHandCopy.removeAll(Arrays.asList(setMeld.getCards()));

      } else if (runMeld != null) {

        listOfMelds.add(runMeld);
        playersHandCopy.removeAll(Arrays.asList(runMeld.getCards()));

      }
      
    }

    // Return the list of melds.
    return listOfMelds;
  }
}
