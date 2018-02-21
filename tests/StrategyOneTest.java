import com.example.Card;
import com.example.Meld;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
/** . Testing class for StrategyOne.java */
public class StrategyOneTest {

  ArrayList<Card> list = new ArrayList<Card>(Card.getAllCards());

  @Test
  public void receiveInitialHand() {
    Sample.SAMPLE_STRATEGY_ONE.receiveInitialHand(list);
    assertEquals(list, Sample.SAMPLE_STRATEGY_ONE.getHand());
  }

  @Test
  public void willTakeTopDiscardFalse() {
    Sample.SAMPLE_STRATEGY_ONE.receiveInitialHand(list);
    assertFalse(Sample.SAMPLE_STRATEGY_ONE.willTakeTopDiscard(list.get(list.size() - 1)));
  }

  @Test
  public void drawAndDiscard() {
    ArrayList<Card> listCopy = new ArrayList<>(Card.getAllCards());
    Sample.SAMPLE_STRATEGY_ONE_DUPLICATE.receiveInitialHand(listCopy);
    assertEquals(
        listCopy.get(0), Sample.SAMPLE_STRATEGY_ONE_DUPLICATE.drawAndDiscard(listCopy.get(0)));
  }

  @Test
  public void getMelds() {
    Sample.SAMPLE_STRATEGY_ONE.receiveInitialHand(list);
    List<Meld> listOfMelds = Sample.SAMPLE_STRATEGY_ONE.getMelds();
    assertEquals(13, listOfMelds.size());
  }

  @Test
  public void knock() {
    assertTrue(Sample.SAMPLE_STRATEGY_ONE.knock());
  }

  @Test
  public void getMeldCards() {
    Sample.SAMPLE_STRATEGY_ONE.receiveInitialHand(list);
    List<Meld> listOfMelds = Sample.SAMPLE_STRATEGY_ONE.getMelds();
    ArrayList<Card> cards = Sample.SAMPLE_STRATEGY_ONE.getMeldCards(listOfMelds);
    assertEquals(list.size(), cards.size());
  }

  @Test
  public void reset() {
    Sample.SAMPLE_STRATEGY_ONE.reset();
    assertEquals(0, Sample.SAMPLE_STRATEGY_ONE.getHand().size());
  }
}
