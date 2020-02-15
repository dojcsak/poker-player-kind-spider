package org.leanpoker.player;

import com.google.gson.JsonElement;
import org.leanpoker.player.model.Card;
import org.leanpoker.player.model.GameState;
import org.leanpoker.player.model.Player;
import org.leanpoker.player.model.Rank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerService {

    static final String VERSION = "Lean Rainman player";

    private RankService rankService = new RankService();

    private Map<String, Rank> rankCache = new HashMap<>();

    public int betRequest(GameState gameState) {
        try {
            List<Card> holeCards = getPlayer(gameState).getHoleCards();

            if (!holeCards.isEmpty()) {
                if (gameState.getCommunityCards().isEmpty()) {
                    if (holeCards.get(0).getValue() == holeCards.get(1).getValue()) {
                        return allIn();
                    } else {
                        return fold();
                    }
                } else {
                    List<Card> cards = new ArrayList<>(gameState.getCommunityCards());
                    cards.addAll(getPlayer(gameState).getHoleCards());
                    Rank rank = rankCache.computeIfAbsent(gameState.getGameId(), key -> rankService.getRank(cards));

                    if (rank.getCards().size() != cards.size()) {
                        rank = rankCache.put(gameState.getGameId(), rankService.getRank(cards));
                    }

                    switch (rank.getRank()) {
                        case 0:
                            fold();
                        case 1:
                        case 2:
                            call(gameState);
                        case 3:
                        case 4:
                        case 5:
                            raise(gameState);
                        case 6:
                        case 7:
                        case 8:
                            allIn();
                        default:
                            fold();

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Integer.MAX_VALUE;
    }

    public static int chenPreflopScore(Card card1, Card card2) {
        var c1v = card1.chenValue();
        var c2v = card2.chenValue();
        var score = Math.max(c1v, c2v);
        if (c1v == c2v) score *= 2;
        if (card1.getSuit() == card2.getSuit()) score += 2;
        var gap = Math.abs(card1.getValue() - card2.getValue());
        switch (gap) {
            case 0:
                break;
            case 1:
                score -= 1;
                break;
            case 2:
                score -= 2;
                break;
            case 3:
                score -= 4;
                break;
            default:
                score -= 5;
                break;
        }
        return (int) Math.ceil(score);
        /*
        highest card /2
        pair? *2
        same suit? +2
        gap
        Add 1 point if there is a 0 or 1 card gap and both cards are lower than a Q.
        */
    }

    private Player getPlayer(GameState gameState) {
        return gameState.getPlayers().get(gameState.getInAction());
    }

    private int allIn() {
        return Integer.MAX_VALUE;
    }

    private int fold() {
        return 0;
    }

    private int raise(GameState gameState) {
        return gameState.getCurrentBuyIn() - getPlayer(gameState).getBet() + gameState.getMinimumRaise();
    }

    private int call(GameState gameState) {
        return gameState.getCurrentBuyIn() - getPlayer(gameState).getBet();
    }

    public void showdown(GameState gameState) {
        if (rankCache.containsKey(gameState.getGameId())) {
            rankCache.remove(gameState.getGameId());
        }
    }
}
