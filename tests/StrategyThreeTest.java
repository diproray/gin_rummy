import com.example.Card;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * . Testing class for StrategyThree.java
 *
 * @author diproray
 */
public class StrategyThreeTest {

  ArrayList<Card> list = new ArrayList<Card>(Card.getAllCards());

  @Test
  public void receiveInitialHand() {
    Sample.SAMPLE_STRATEGY_THREE.receiveInitialHand(list);
    assertEquals(list, Sample.SAMPLE_STRATEGY_THREE.getHand());
  }

  @Test
  public void knock() {
    assertTrue(Sample.SAMPLE_STRATEGY_THREE.knock());
  }

  @Test
  public void getMelds() {
    assertEquals(null, Sample.SAMPLE_STRATEGY_THREE.getMelds());
  }

  @Test
  public void reset() {
    Sample.SAMPLE_STRATEGY_THREE.reset();
    assertEquals(0, Sample.SAMPLE_STRATEGY_THREE.getHand().size());
  }
}
