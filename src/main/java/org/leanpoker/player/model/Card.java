package org.leanpoker.player.model;

import lombok.Data;

@Data
public class Card {

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
}
