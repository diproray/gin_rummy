import com.example.Card;
import com.example.Meld;
import com.example.RunMeld;
import com.example.SetMeld;
import org.paukov.combinatorics3.Generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GameEngineUtils {


  /**
   * . Functions returns a list of deadwood cards for a Player.
   *
   * @param player the player
   * @return sum of deadwood points for the cards
   */
  public static ArrayList<Card> getDeadwoodCardsList(Player player) {

    ArrayList<Meld> listOfPlayerMelds = getListOfMelds(player);
    ArrayList<Card> listOfMeldCards =  new ArrayList<>();

    for (Meld meld: listOfPlayerMelds) {
      Card[] arrayOfCardsInMeld = meld.getCards();
      listOfMeldCards.addAll(Arrays.asList(arrayOfCardsInMeld));
    }

    ArrayList deadwoodCards = new ArrayList(player.getHand());
    deadwoodCards.removeAll(listOfMeldCards);

    return deadwoodCards;
  }

  /**
   * . Function returns a list of melds of the current player
   *
   * @param player the player
   * @return a list of melds
   */
  public static ArrayList<Meld> getListOfMelds(Player player) {

    ArrayList<Meld> listOfMelds = new ArrayList<>();
    ArrayList<Card> playersHandCopy = new ArrayList<>(player.getHand());


    for (List<Card> listOfThreeCards: (Generator.combination(playersHandCopy)
        .simple(3)
        .stream()
        .collect(Collectors.toCollection(ArrayList::new)))) {

      SetMeld setMeld = Meld.buildSetMeld(listOfThreeCards);
      RunMeld runMeld = Meld.buildRunMeld(listOfThreeCards);

      if (setMeld != null) {
        listOfMelds.add(setMeld);
        playersHandCopy.removeAll(Arrays.asList(setMeld.getCards()));
      } else if (runMeld != null) {
        listOfMelds.add(runMeld);
        playersHandCopy.removeAll(Arrays.asList(runMeld.getCards()));
      }
    }

    return listOfMelds;
  }

  /**
   * . Getter for deadwood points for a player
   *
   * @param player the player
   * @return an int value
   */
  public static int getDeadwoodPoints(Player player) {

    ArrayList<Card> deadwoodCardsList = GameEngineUtils.getDeadwoodCardsList(player);
    int deadwoodPoints = 0;
    for (Card card : deadwoodCardsList) {
      deadwoodPoints += card.getPointValue();
    }

    return deadwoodPoints;
  }
}
