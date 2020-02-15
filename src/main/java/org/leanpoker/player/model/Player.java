package org.leanpoker.player.model;

import lombok.Data;
import org.leanpoker.player.model.Card;

import java.util.List;

@Data
public class Player {

    private String name;

    private int stack;

    private String status;

    private int bet;

    private List<Card> holeCards;

    private int timeUsed;

    private String version;

    private int id;
}
