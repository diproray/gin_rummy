import org.junit.Test;

import static org.junit.Assert.*;

/**
 * . Test class for Deck.java
 *
 * @author diproray
 */
public class DeckTest {

  @Test
  public void getInitialHand() {

    try {
      Sample.SAMPLE_DECK.getInitialHand();
    } catch (IndexOutOfBoundsException e) {
      assertEquals("toIndex = 10", e.getMessage());
    }
  }
}
