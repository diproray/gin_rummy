import com.example.Card;
import com.example.StrategyOne;
import com.example.StrategyThree;
import com.example.StrategyTwo;

import java.lang.reflect.Array;
import java.util.ArrayList;
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
      new Player(STRATEGY_THREE_FOR_SAMPLE_PLAYER, EMPTY_CARDS_LIST);
  public static final ArrayList<Card> EMPTY_ARRAY_LIST_OF_CARDS = new ArrayList<Card>();
  public static final ArrayList<Card> EMPTY_ARRAY_LIST_OF_CARDS_TWO = new ArrayList<Card>();
  public static final Pile SAMPLE_PILE = new Pile(EMPTY_ARRAY_LIST_OF_CARDS);
  public static final Deck SAMPLE_DECK = new Deck(EMPTY_ARRAY_LIST_OF_CARDS_TWO);
  public static final StrategyThree SAMPLE_STRATEGY_THREE = new StrategyThree();
  public static final StrategyTwo SAMPLE_STRATEGY_TWO = new StrategyTwo();
  public static final StrategyOne SAMPLE_STRATEGY_ONE = new StrategyOne();
  public static final StrategyOne SAMPLE_STRATEGY_ONE_DUPLICATE = new StrategyOne();
}
