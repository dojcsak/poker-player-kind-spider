package org.leanpoker.player;

import com.google.gson.JsonElement;
import org.leanpoker.player.model.Card;
import org.leanpoker.player.model.GameState;
import org.leanpoker.player.model.Player;

import java.util.List;

public class PlayerService {

    static final String VERSION = "Mad player";

    public int betRequest(GameState gameState) {
        try {
            List<Card> holeCards = getPlayer(gameState).getHoleCards();

            if (!holeCards.isEmpty()) {
                if (holeCards.get(0).getValue() == holeCards.get(1).getValue()) {
                    return Integer.MAX_VALUE;
                } else {
                    return call(gameState);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Integer.MAX_VALUE;
    }

    private Player getPlayer(GameState gameState) {
        return gameState.getPlayers().get(gameState.getInAction());
    }

    private int call(GameState gameState) {
        return gameState.getCurrentBuyIn() - getPlayer(gameState).getBet();
    }

    public static void showdown(JsonElement game) {
    }
}
