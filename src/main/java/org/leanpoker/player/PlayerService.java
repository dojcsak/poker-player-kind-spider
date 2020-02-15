package org.leanpoker.player;

import com.google.gson.JsonElement;
import org.leanpoker.player.model.Card;
import org.leanpoker.player.model.GameState;

import java.util.List;

public class PlayerService {

    static final String VERSION = "Crazy player";

    public int betRequest(GameState gameState) {
        try {
            List<Card> holeCards = gameState.getPlayers().get(gameState.getInAction()).getHoleCards();

            if (!holeCards.isEmpty()) {
                if (holeCards.get(0).getValue() == holeCards.get(1).getValue()) {
                    return Integer.MAX_VALUE;
                } else {
                    return Integer.MAX_VALUE;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Integer.MAX_VALUE;
    }

    public static void showdown(JsonElement game) {
    }
}
