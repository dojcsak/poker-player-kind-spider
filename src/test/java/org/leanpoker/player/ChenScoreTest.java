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
}
