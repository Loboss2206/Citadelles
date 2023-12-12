package fr.cotedazur.univ.polytech.controller;

import fr.cotedazur.univ.polytech.model.bot.BotRandom;
import fr.cotedazur.univ.polytech.model.bot.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;

class GameTest {
    ArrayList<Player> players;
    BotRandom botRandom1;
    Game game;

    @BeforeEach
    void setUp() {
        players = new ArrayList<>(Arrays.asList(new BotRandom(), new BotRandom(), new BotRandom(), new BotRandom()));
        game = new Game(players);
    }

    @Test
    void testGameIsFinished() {
        game.startGame();

        boolean onePlayerHas8OrMoreDistricts = false;
        for (Player player : game.getPlayers()) {
            if (player.getBoard().size() >= 8) {
                onePlayerHas8OrMoreDistricts = true;
            }
        }

        assertTrue(onePlayerHas8OrMoreDistricts);
    }

    //Difficult to test if we don't know the player action
    /*@Test
    void testRankingWithOnePlayerWithOnlyGoldsAndOnlyOneRound() {
        game.startGame();
        Assertions.assertEquals(2, game.getPlayers().get(0).getGolds());
    }*/
}