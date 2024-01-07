package fr.cotedazur.univ.polytech.model.bot;

import java.util.Comparator;

public class PlayerComparator implements Comparator<Player> {

    @Override
    public int compare(Player player1, Player player2) {
        int pointsComparison = Integer.compare(player1.getPoints(), player2.getPoints());

        // If points are equals
        if (pointsComparison == 0) {
            //IMPORTANT : Assasin can kill the player don't reveal his character
            return Integer.compare(player1.getPoints() + player1.getPlayerRole().getCharacterNumber(), player2.getPoints() + player2.getPlayerRole().getCharacterNumber());
        }

        return pointsComparison;
    }
}
