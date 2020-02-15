package org.leanpoker.player;

import org.junit.jupiter.api.Test;
import org.leanpoker.player.model.Card;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChenScoreTest {
    @Test
    public void ChenOfAcePairIs20() {
        var ace1 = Card.builder().rank("A").suit("x").build();
        var ace2 = Card.builder().rank("A").suit("not x").build();

        assertEquals(20, PlayerService.chenPreflopScore(ace1, ace2));
    }

    @Test
    public void ChenOfAKSameSuitIs12() {
        var ace = Card.builder().rank("A").suit("x").build();
        var king = Card.builder().rank("K").suit("x").build();

        assertEquals(12, PlayerService.chenPreflopScore(ace, king));
    }

    @Test
    public void ChenOfAKDiffSuitIs10() {
        var ace = Card.builder().rank("A").suit("x").build();
        var king = Card.builder().rank("K").suit("not x").build();

        assertEquals(10, PlayerService.chenPreflopScore(ace, king));
    }

    @Test
    public void ChenOfTenPairIs10() {
        assertEquals(10, PlayerService.chenPreflopScore(Card.builder().rank("10").suit("x").build(), Card.builder().rank("10").suit("not x").build()));
    }

    @Test
    public void ChenOf57SameSuitIs6() {
        assertEquals(6, PlayerService.chenPreflopScore(Card.builder().rank("5").suit("x").build(), Card.builder().rank("7").suit("x").build()));
    }
}
