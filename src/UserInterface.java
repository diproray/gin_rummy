

import com.example.PlayerStrategy;

import java.util.ArrayList;
import java.util.Arrays;

public class UserInterface {

  /**
   * . Main function to be run
   *
   * @param args command line arguments
   */
  public static void main(String[] args) {

    com.example.StrategyOne firstStrategy = new com.example.StrategyOne();
    com.example.StrategyTwo secondStrategy = new com.example.StrategyTwo();
    com.example.StrategyThree thirdStrategy = new com.example.StrategyThree();

    PlayerStrategy[] arrayOfStrategies =
        new PlayerStrategy[] {firstStrategy, secondStrategy, thirdStrategy};
    ArrayList<PlayerStrategy> arrayListOfPlayerStrategies =
        new ArrayList<>(Arrays.asList(arrayOfStrategies));

    UserInterface.getSummary(arrayListOfPlayerStrategies, 1000);

  }

  /**
   * . Compete all strategies against each other and display summary
   *
   * @param arrayListOfPlayerStrategies the list of player strategies
   * @param numberOfGames number of games to play for each combination
   */
  public static void getSummary(
      ArrayList<PlayerStrategy> arrayListOfPlayerStrategies, int numberOfGames) {

    ArrayList<Integer> arrayListOfPlayerScores = new ArrayList<Integer>();

    for (int i = 0; i < arrayListOfPlayerStrategies.size(); i++) {
      arrayListOfPlayerScores.add(0);
    }

    for (int gameNumber = 0; gameNumber < numberOfGames; gameNumber++) {

      for (int i = 0; i < arrayListOfPlayerStrategies.size(); i++) {

        PlayerStrategy firstCompetitorStrategy = arrayListOfPlayerStrategies.get(i);

        for (int j = i + 1; j < arrayListOfPlayerStrategies.size(); j++) {

          PlayerStrategy secondCompetitorStrategy = arrayListOfPlayerStrategies.get(j);

          GameEngine ge = new GameEngine(firstCompetitorStrategy, secondCompetitorStrategy);
          boolean isFirstPlayerTheWinner = ge.game();

          if (isFirstPlayerTheWinner) {

            int newWinningPlayerScore = arrayListOfPlayerScores.get(i) + 1;
            arrayListOfPlayerScores.set(i, newWinningPlayerScore);

          } else {

            int newWinningPlayerScore = arrayListOfPlayerScores.get(j) + 1;
            arrayListOfPlayerScores.set(j, newWinningPlayerScore);
          }
        }
      }
    }

    System.out.println("SUMMARY of All Gin Rummy Games: ");
    for (int index = 0; index < arrayListOfPlayerScores.size(); index++) {

      int playerScore = arrayListOfPlayerScores.get(index);
      int totalNumberOfGamesPlayedByPlayer = (arrayListOfPlayerScores.size() - 1) * numberOfGames;

      System.out.println(
          "Player "
              + (index + 1)
              + ": \t"
              + "Games Won (out of "
              + totalNumberOfGamesPlayedByPlayer
              + ") - "
              + playerScore
              + ", \tWin Percentage - "
              + (double) ((playerScore * 100) / totalNumberOfGamesPlayedByPlayer)
              + "%.");
    }
  }

}
