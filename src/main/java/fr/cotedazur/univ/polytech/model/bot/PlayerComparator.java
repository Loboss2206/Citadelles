package fr.cotedazur.univ.polytech.model.bot;

import java.util.Comparator;

public class PlayerComparator implements Comparator<Player> {

    @Override
    public int compare(Player player1, Player player2) {
        return Integer.compare(player1.getPoints(), player2.getPoints());
    }
}
