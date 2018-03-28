import com.example.*;
import com.example.StrategyOne;
import com.example.StrategyThree;
import com.example.StrategyTwo;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * . Class with constants for testing
 *
 * @author diproray
 */
public class Sample {

  public static final StrategyThree STRATEGY_THREE_FOR_SAMPLE_PLAYER = new StrategyThree();
  public static final ArrayList<Card> EMPTY_CARDS_LIST = new ArrayList<Card>();
  public static final Player SAMPLE_PLAYER =
      new Player(STRATEGY_THREE_FOR_SAMPLE_PLAYER, EMPTY_CARDS_LIST);
  public static final Player SAMPLE_PLAYER_TWO =
      new Player(STRATEGY_THREE_FOR_SAMPLE_PLAYER, null);
  public static final Player SAMPLE_PLAYER_THREE =
      new Player(STRATEGY_THREE_FOR_SAMPLE_PLAYER, new ArrayList<>(Card.getAllCards()));
  public static final ArrayList<Card> EMPTY_ARRAY_LIST_OF_CARDS = new ArrayList<Card>();
  public static final ArrayList<Card> EMPTY_ARRAY_LIST_OF_CARDS_TWO = new ArrayList<Card>();
  public static final Pile SAMPLE_PILE = new Pile(EMPTY_ARRAY_LIST_OF_CARDS);
  public static final StrategyThree SAMPLE_STRATEGY_THREE = new StrategyThree();
  public static final StrategyTwo SAMPLE_STRATEGY_TWO = new StrategyTwo();
  public static final StrategyOne SAMPLE_STRATEGY_ONE = new StrategyOne();
  public static final StrategyOne SAMPLE_STRATEGY_ONE_DUPLICATE = new StrategyOne();

  public static final PlayerStrategy[] arrayOfStrategies =
      new PlayerStrategy[] {SAMPLE_STRATEGY_ONE, SAMPLE_STRATEGY_TWO, SAMPLE_STRATEGY_THREE};
  public static final ArrayList<PlayerStrategy> ARRAY_LIST_OF_PLAYER_STRATEGIES =
      new ArrayList<>(Arrays.asList(arrayOfStrategies));

  public static final ArrayList<Card> SAMPLE_STANDARD_DECK = new ArrayList<>(Card.getAllCards());
  public static final ArrayList<Card> EMPTY_STANDARD_DECK = new ArrayList<>();
}
