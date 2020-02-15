package org.leanpoker.player.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Card {

    public static final double QUEEN_CHEN_CARD_VALUE = 7.0;
    private String rank;

    private String suit;

    public int getValue() {
        switch (rank) {
            case "2":
            case "3":
            case "4":
            case "5":
            case "6":
            case "7":
            case "8":
            case "9":
            case "10":
                return Integer.parseInt(rank);
            case "J":
                return 11;
            case "Q":
                return 12;
            case "K":
                return 13;
            case "A":
                return 14;
            default:
                throw new RuntimeException();
        }
    }

    public double chenValue() {
        switch (rank) {
            case "2":
                return 1.0;
            case "3":
                return 1.5;
            case "4":
                return 2;
            case "5":
                return 2.5;
            case "6":
                return 3;
            case "7":
                return 3.5;
            case "8":
                return 4;
            case "9":
                return 4.5;
            case "10":
                return 5.0;
            case "J":
                return 6.0;
            case "Q":
                return QUEEN_CHEN_CARD_VALUE;
            case "K":
                return 8.0;
            case "A":
                return 10.0;
            default:
                throw new RuntimeException();
        }
    }
}
