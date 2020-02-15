package org.leanpoker.player;

import org.leanpoker.player.model.Card;
import org.leanpoker.player.model.GameState;
import org.leanpoker.player.model.Player;
import org.leanpoker.player.model.Rank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerService {

    static final String VERSION = "Safer Rainman player";

    private RankService rankService = new RankService();

    private Map<String, Rank> rankCache = new HashMap<>();

    public int betRequest(GameState gameState) {
        try {
            List<Card> holeCards = getPlayer(gameState).getHoleCards();

            if (!holeCards.isEmpty()) {
                if (isPreflop(gameState)) {
                    var chenScore = chenPreflopScore(holeCards);
                    if (chenScore <= 8.5) {
                        if (gameState.getBigBlind() < call(gameState)) {
                            return fold();
                        } else {
                            return raise(gameState);
                        }
                    } else {
                        return allIn();
                    }
                } else {
                    List<Card> cards = new ArrayList<>(gameState.getCommunityCards());
                    cards.addAll(getPlayer(gameState).getHoleCards());

                    Rank rank = null;
                    if (!rankCache.containsKey(gameState.getGameId())) {
                        rank = rankService.getRank(cards);
                        rankCache.put(gameState.getGameId(), rank);
                    } else {
                        rank = rankCache.get(gameState.getGameId());
                        if (rank.getCards().size() != cards.size()) {
                            rank = rankService.getRank(cards);
                            rankCache.put(gameState.getGameId(), rank);
                        }
                    }


                    boolean hasHighCard = false;
                    if (holeCards.get(0).getValue() == rank.getValue() || holeCards.get(1).getValue() == rank.getValue()) {
                        hasHighCard = true;
                    }

                    boolean hasSecondHighCard = false;
                    if (holeCards.get(0).getValue() == rank.getSecondValue() || holeCards.get(1).getValue() == rank.getSecondValue()) {
                        hasSecondHighCard = true;
                    }

                    switch (rank.getRank()) {
                        case 0:
                            return fold();
                        case 1:
                            if (hasHighCard) {
                                return call(gameState);
                            } else {
                                return fold();
                            }
                        case 2:
                            if (hasHighCard) {
                                if (hasSecondHighCard) {
                                    return allIn();
                                } else {
                                    return call(gameState);
                                }
                            } else {
                                return call(gameState);
                            }
                        case 3:
                        case 4:
                        case 5:
                            if (hasHighCard) {
                                if (hasSecondHighCard) {
                                    return allIn();
                                } else {
                                    return raise(gameState);
                                }
                            } else {
                                return call(gameState);
                            }
                        case 6:
                        case 7:
                        case 8:
                            return allIn();
                        default:
                            return fold();

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Integer.MAX_VALUE;
    }

    private boolean hasHighChenValue(List<Card> holeCards) {
        if (chenPreflopScore(holeCards) > 7.5) {
            return true;
        }
        return false;
    }

    private boolean isPair(List<Card> holeCards) {
        return holeCards.get(0).getValue() == holeCards.get(1).getValue();
    }

    private boolean isPreflop(GameState gameState) {
        return gameState.getCommunityCards().isEmpty();
    }

    // http://www.thepokerbank.com/strategy/basic/starting-hand-selection/chen-formula/
    public static int chenPreflopScore(Card card1, Card card2) {
        var c1v = card1.chenValue();
        var c2v = card2.chenValue();
        var score = Math.max(c1v, c2v);
        var isPair = c1v == c2v;
        if (isPair) score *= 2;
        if (card1.getSuit() == card2.getSuit()) score += 2;
        var gap = Math.max(Math.abs(card1.getValue() - card2.getValue()) - 1, 0);
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
        if (!isPair && c1v < Card.QUEEN_CHEN_CARD_VALUE && c2v < Card.QUEEN_CHEN_CARD_VALUE && gap <= 1)
            score += 1.0;
        return (int) Math.ceil(score);
        /*
        highest card /2
        pair? *2
        same suit? +2
        gap
        Add 1 point if there is a 0 or 1 card gap and both cards are lower than a Q.
        */
    }

    public static int chenPreflopScore(List<Card> holeCards) {
        return chenPreflopScore(holeCards.get(0), holeCards.get(1));
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
