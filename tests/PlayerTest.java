import com.example.Card;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class PlayerTest {

  @Test
  public void getStrategy() {
    assertEquals(Sample.STRATEGY_THREE_FOR_SAMPLE_PLAYER, Sample.SAMPLE_PLAYER.getStrategy());
  }

  @Test
  public void getHand() {
    assertEquals(Sample.EMPTY_CARDS_LIST, Sample.SAMPLE_PLAYER.getHand());
  }

  @Test
  public void setInitialHand() {
    Sample.SAMPLE_PLAYER.setInitialHand(Sample.EMPTY_CARDS_LIST);
    assertEquals(Sample.EMPTY_CARDS_LIST, Sample.SAMPLE_PLAYER.getHand());
  }


  @Test
  public void setHand() {
    Sample.SAMPLE_PLAYER.setHand(Sample.EMPTY_CARDS_LIST);
    assertEquals(Sample.EMPTY_CARDS_LIST, Sample.SAMPLE_PLAYER.getHand());
  }

  @Test
  public void addToHand() {
    Sample.SAMPLE_PLAYER.addToHand(null);
    assertTrue(Sample.EMPTY_CARDS_LIST == Sample.SAMPLE_PLAYER.getHand());
  }

  @Test
  public void removeFromHand() {
    Sample.SAMPLE_PLAYER.addToHand(null);
    assertTrue(Sample.EMPTY_CARDS_LIST == Sample.SAMPLE_PLAYER.getHand());
  }
}