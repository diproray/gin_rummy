import com.example.Card;
import com.example.Meld;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/** . Testing class for StrategyTwo.java */
public class StrategyTwoTest {
  ArrayList<Card> list = new ArrayList<Card>(Card.getAllCards());

  @Test
  public void receiveInitialHand() {
    Sample.SAMPLE_STRATEGY_TWO.receiveInitialHand(list);
    assertEquals(list, Sample.SAMPLE_STRATEGY_TWO.getHand());
  }

  @Test
  public void willTakeTopDiscardFalse() {
    Sample.SAMPLE_STRATEGY_TWO.receiveInitialHand(list);
    assertFalse(Sample.SAMPLE_STRATEGY_TWO.willTakeTopDiscard(list.get(list.size() - 1)));
  }

  @Test
  public void willTakeTopDiscardTrue() {
    Sample.SAMPLE_STRATEGY_TWO.receiveInitialHand(list);
    assertTrue(Sample.SAMPLE_STRATEGY_TWO.willTakeTopDiscard(list.get(0)));
  }

  @Test
  public void drawAndDiscard() {
    Sample.SAMPLE_STRATEGY_TWO.receiveInitialHand(list);
    Card card = list.get(list.size() - 1);
    assertEquals(list.get(list.size() - 1), Sample.SAMPLE_STRATEGY_TWO.drawAndDiscard(list.get(0)));
  }

  @Test
  public void knock() {
    assertTrue(Sample.SAMPLE_STRATEGY_TWO.knock());
  }

  @Test
  public void getMelds() {
    Sample.SAMPLE_STRATEGY_TWO.receiveInitialHand(list);
    List<Meld> listOfMelds = Sample.SAMPLE_STRATEGY_TWO.getMelds();
    assertEquals(13, listOfMelds.size());
  }

  @Test
  public void reset() {
    Sample.SAMPLE_STRATEGY_TWO.reset();
    assertEquals(0, Sample.SAMPLE_STRATEGY_TWO.getHand().size());
  }
}
