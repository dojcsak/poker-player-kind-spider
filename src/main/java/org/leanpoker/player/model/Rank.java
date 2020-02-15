package org.leanpoker.player.model;

import lombok.Data;

import java.util.List;

@Data
public class Rank {

    private int rank;

    private int value;

    private int secondValue;

    private int[] kickers;

    private List<Card> cardsUsed;

    private List<Card> cards;
}
