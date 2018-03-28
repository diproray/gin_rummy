import com.example.Card;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * . Test class added for Pile.java
 *
 * @author diproray
 */
public class PileTest {

  @Test
  public void shuffle() {
    Sample.SAMPLE_PILE.shuffle();
    assertEquals(Sample.EMPTY_ARRAY_LIST_OF_CARDS, Sample.SAMPLE_PILE.getPile());
  }

  @Test
  public void add() {
    Sample.SAMPLE_PILE.add(null);
    assertTrue(Sample.EMPTY_ARRAY_LIST_OF_CARDS == Sample.SAMPLE_PILE.getPile());
  }

  @Test
  public void getTopCard() {
    try {
      Sample.SAMPLE_PILE.getTopCard();
    } catch (IndexOutOfBoundsException e) {
      assertEquals("Index: 0, Size: 0", e.getMessage());
    }
  }

  @Test
  public void getTopCardEmptyInput() {
    Card topCard = Sample.SAMPLE_PILE.getTopCard();
    assertNull(topCard);
  }

  @Test
  public void getPile() {
    assertEquals(Sample.EMPTY_ARRAY_LIST_OF_CARDS, Sample.SAMPLE_PILE.getPile());
  }
}
