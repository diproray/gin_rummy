import com.example.PlayerStrategy;
import org.paukov.combinatorics3.Generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * . Class that contains the "main" function to be run by the user. Its functions execute the
 * playing of Gin Rummy Games between Players, and summarize and display the results.
 *
 * @author diproray
 */
public class UserInterface {

  /**
   * . Main function to be run
   *
   * @param args command line arguments
   */
  public static void main(String[] args) {

    // Declare the PlayerStrategies you want to use.
    com.example.StrategyOne firstStrategy = new com.example.StrategyOne();
    com.example.StrategyTwo secondStrategy = new com.example.StrategyTwo();
    com.example.StrategyThree thirdStrategy = new com.example.StrategyThree();

    // Create an ArrayList of those player strategies.
    PlayerStrategy[] arrayOfStrategies =
        new PlayerStrategy[] {firstStrategy, secondStrategy, thirdStrategy};
    ArrayList<PlayerStrategy> arrayListOfPlayerStrategies =
        new ArrayList<>(Arrays.asList(arrayOfStrategies));

    System.out.println(UserInterface.getSummary(arrayListOfPlayerStrategies, 100));
  }

  /**
   * . Compete all strategies against each other and display summary.
   *
   * <p>Plays each Player in the list of player strategies against every other player strategy in
   * the list and displays results.
   *
   * @param arrayListOfPlayerStrategies the list of player strategies
   * @param numberOfGames number of games to play for each combination
   * @return the stirng containing the players performance summary
   */
  public static String getSummary(
      List<PlayerStrategy> arrayListOfPlayerStrategies, int numberOfGames) {

    if (arrayListOfPlayerStrategies == null
        || arrayListOfPlayerStrategies.size() == 0
        || numberOfGames <= 0) {
      return "INVALID INPUT!";
    }
    // Create an arraylist that will store player scores.
    // The index of a player score in the list of player scores corresponds to the index of a Player
    // in the list of player strategies

    ArrayList<Integer> arrayListOfPlayerScores = new ArrayList<Integer>();
    for (int i = 0; i < arrayListOfPlayerStrategies.size(); i++) {
      arrayListOfPlayerScores.add(0);
    }

    // The piece of code
    // (Generator.combination(playersHandCopy).simple(3).stream().collect(Collectors.toCollection(ArrayList::new)))
    // makes use of the Generator class in the combinatoricslib3 library which is publicly
    // available.
    //
    // The piece of code, in a general sense, does something like the following:
    // Input to the code [the list passed to it]: [1, 2, 3]
    // Output from the code: [[1, 2], [1, 3], [2, 3]]
    //
    // In our case, it takes a ArrayList<PlayerStrategy> and returns a List of all possible
    // combinations of 2
    // Player Strategies from the List.

    ArrayList<List<PlayerStrategy>> arrayListOfAllGroupsOfTwoStrategies =
        Generator.combination(arrayListOfPlayerStrategies)
            .simple(2)
            .stream()
            .collect(Collectors.toCollection(ArrayList::new));

    // For each group of two players,
    // player numberOfGames games between them and update data in arrayListOfPlayersScores.

    for (List<PlayerStrategy> groupOfTwoStrategies : arrayListOfAllGroupsOfTwoStrategies) {

      // Store the two players.
      PlayerStrategy firstCompetitorStrategy = groupOfTwoStrategies.get(0);
      PlayerStrategy secondCompetitorStrategy = groupOfTwoStrategies.get(1);

      // Get their indices in the list of player strategies.

      int firstStrategyIndex = arrayListOfPlayerStrategies.indexOf(firstCompetitorStrategy);
      int secondStrategyIndex = arrayListOfPlayerStrategies.indexOf(secondCompetitorStrategy);

      // For numberOfGames times,
      // Play a game between the two players under consideration.
      // Update their scores

      for (int gameNumber = 0; gameNumber < numberOfGames; gameNumber++) {

        // Play a game between the two players.
        GameEngine ge = new GameEngine(firstCompetitorStrategy, secondCompetitorStrategy);
        boolean isFirstPlayerTheWinner = ge.game();

        // Based on game results, update scores of the players.

        if (isFirstPlayerTheWinner) {

          int newWinningPlayerScore = arrayListOfPlayerScores.get(firstStrategyIndex) + 1;
          arrayListOfPlayerScores.set(firstStrategyIndex, newWinningPlayerScore);

        } else {

          int newWinningPlayerScore = arrayListOfPlayerScores.get(secondStrategyIndex) + 1;
          arrayListOfPlayerScores.set(secondStrategyIndex, newWinningPlayerScore);
        }
      }
    }

    StringBuilder outputStringBuilder = new StringBuilder();

    // Store a summary of performances of each player.

    outputStringBuilder.append("SUMMARY of All Gin Rummy Games: \n");
    for (int index = 0; index < arrayListOfPlayerScores.size(); index++) {

      int playerScore = arrayListOfPlayerScores.get(index);
      int totalNumberOfGamesPlayedByPlayer = (arrayListOfPlayerScores.size() - 1) * numberOfGames;

      outputStringBuilder.append(
          "Player "
              + (index + 1)
              + ": \t"
              + "Games Won (out of "
              + totalNumberOfGamesPlayedByPlayer
              + ") - "
              + playerScore
              + ", \tWin Percentage - "
              + (double) ((playerScore * 100) / totalNumberOfGamesPlayedByPlayer)
              + "%.\n");
    }

    String outputString = outputStringBuilder.toString();
    return outputString;
  }
}
