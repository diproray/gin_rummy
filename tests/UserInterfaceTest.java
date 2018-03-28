import com.example.PlayerStrategy;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class UserInterfaceTest {

  @Test
  public void getSummaryEmptyList() {
    ArrayList<PlayerStrategy> arrayListOfPlayerStrategies = new ArrayList<>();
    assertEquals(UserInterface.getSummary(arrayListOfPlayerStrategies, 100), "INVALID INPUT!");
  }

  @Test
  public void getSummaryNullList() {
    ArrayList<PlayerStrategy> arrayListOfPlayerStrategies = null;
    assertEquals(UserInterface.getSummary(arrayListOfPlayerStrategies, 100), "INVALID INPUT!");
  }

  @Test
  public void getSummaryNegativeNumberOfGames() {
    ArrayList<PlayerStrategy> arrayListOfPlayerStrategies = new ArrayList<>();
    assertEquals(UserInterface.getSummary(arrayListOfPlayerStrategies, -100), "INVALID INPUT!");
  }

  @Test
  public void getSummaryNormalInput() {
    ArrayList<PlayerStrategy> arrayListOfPlayerStrategies = Sample.ARRAY_LIST_OF_PLAYER_STRATEGIES;
    String expectedOutput = "SUMMARY of";
    assertEquals(
        UserInterface.getSummary(arrayListOfPlayerStrategies, 100).substring(0, 10),
        expectedOutput);
  }
}
