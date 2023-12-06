package fr.cotedazur.univ.polytech.startingpoint.controller;

import fr.cotedazur.univ.polytech.startingpoint.model.bot.BotRandom;
import fr.cotedazur.univ.polytech.startingpoint.model.bot.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    ArrayList<Player> players;
    BotRandom botRandom1;
    Game game;

    @BeforeEach
    void setUp() {
        players = new ArrayList<>();
        botRandom1 = new BotRandom();
        players.add(botRandom1);
        game = new Game(players);
    }

    @Test
    void testRankingWithOnePlayerWithOnlyGoldsAndOnlyOneRound() {
        game.startGame();
        assertEquals(2, game.getPlayers().get(0).getGolds());
    }
}