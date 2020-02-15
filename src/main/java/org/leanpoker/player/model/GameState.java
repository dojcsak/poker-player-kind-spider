package org.leanpoker.player.model;

import lombok.Data;

import java.util.List;

@Data
public class GameState {

    private String tournamentId;

    private String gameId;

    private int round;

    List<Player> players;

    private int smallBlind;

    private int bigBlind;

    private int orbits;

    private int dealer;

    List<Card> communityCards;

    private int currentBuyIn;

    private int pot;
}
