import org.junit.Test;

import static org.junit.Assert.*;

public class GameEngineUtilsTest {

  @Test
  public void getDeadwoodCardsListNullPlayer() {
    assertNull(GameEngineUtils.getDeadwoodCardsList(null));
  }

  @Test
  public void getDeadwoodCardsListNullHand() {
    assertNull(GameEngineUtils.getDeadwoodCardsList(Sample.SAMPLE_PLAYER_TWO));
  }

  @Test
  public void getDeadwoodCardsListEmptyHand() {
    assertNull(GameEngineUtils.getDeadwoodCardsList(Sample.SAMPLE_PLAYER));
  }

  @Test
  public void getDeadwoodCardsPointsNullPlayer() {
    assertEquals(GameEngineUtils.getDeadwoodPoints(null), 0);
  }

  @Test
  public void getDeadwoodPointsNullHand() {
    assertEquals(GameEngineUtils.getDeadwoodPoints(Sample.SAMPLE_PLAYER_TWO), 0);
  }

  @Test
  public void getDeadwoodPointsEmptyHand() {
    assertEquals(GameEngineUtils.getDeadwoodPoints(Sample.SAMPLE_PLAYER), 0);
  }

  @Test
  public void getDeadwoodPoints() {
    assertEquals(0, GameEngineUtils.getDeadwoodPoints(Sample.SAMPLE_PLAYER_THREE));
  }


  @Test
  public void getListOfMelds() {
    assertEquals(53, GameEngineUtils.getListOfMelds(Sample.SAMPLE_PLAYER_THREE).size());
  }


  @Test
  public void getListOfMeldsNullPlayer() {
    assertNull(GameEngineUtils.getListOfMelds(null));
  }

  @Test
  public void getListOfMeldsNullHand() {
    assertNull(GameEngineUtils.getListOfMelds(Sample.SAMPLE_PLAYER_TWO));
  }

  @Test
  public void getListOfMeldsEmptyHand() {
    assertNull(GameEngineUtils.getListOfMelds(Sample.SAMPLE_PLAYER));
  }
}