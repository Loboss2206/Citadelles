package fr.cotedazur.univ.polytech.controller;

import fr.cotedazur.univ.polytech.model.bot.BotRandom;
import fr.cotedazur.univ.polytech.model.bot.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameTest {
    ArrayList<Player> players;
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

    // Change when we change the method calculatePoints
    @Test
    void testCalculatePoints() {
        game.startGame();
        for (Player player : game.getPlayers()) {
            assertEquals(player.getBoard().size(), player.getPoints());
        }
    }

    @Test
    void testSortPlayersByPoints() {
        Player player1 = new BotRandom();
        Player player2 = new BotRandom();
        Player player3 = new BotRandom();
        Player player4 = new BotRandom();
        player1.setPoints(1);
        player2.setPoints(9);
        player3.setPoints(4);
        player4.setPoints(11);

        players = new ArrayList<>(Arrays.asList(player1, player2, player3, player4));
        game = new Game(players);

        assertEquals(player1, game.getPlayers().get(0));
        assertEquals(player2, game.getPlayers().get(1));
        assertEquals(player3, game.getPlayers().get(2));
        assertEquals(player4, game.getPlayers().get(3));

        game.sortByPoints(players);

        assertEquals(player1, game.getPlayers().get(3));
        assertEquals(player2, game.getPlayers().get(1));
        assertEquals(player3, game.getPlayers().get(2));
        assertEquals(player4, game.getPlayers().get(0));
    }

    @Test
    void testSetCrownedPlayerToFirstPlace() {
        Player player1 = new BotRandom();
        Player player2 = new BotRandom();
        Player player3 = new BotRandom();
        Player player4 = new BotRandom();

        players = new ArrayList<>(Arrays.asList(player1, player2, player3, player4));
        game = new Game(players);

        for (Player player : game.getPlayers()) {
            player.setCrowned(false);
        }

        assertEquals(player1, game.getPlayers().get(0));
        assertEquals(player2, game.getPlayers().get(1));
        assertEquals(player3, game.getPlayers().get(2));
        assertEquals(player4, game.getPlayers().get(3));

        player3.setCrowned(true);
        game.setCrownedPlayerToFirstPlace();

        assertEquals(player3, game.getPlayers().get(0));
        assertEquals(player4, game.getPlayers().get(1));
        assertEquals(player1, game.getPlayers().get(2));
        assertEquals(player2, game.getPlayers().get(3));

        player3.setCrowned(false);
        player4.setCrowned(true);
        game.setCrownedPlayerToFirstPlace();

        assertEquals(player4, game.getPlayers().get(0));
        assertEquals(player1, game.getPlayers().get(1));
        assertEquals(player2, game.getPlayers().get(2));
        assertEquals(player3, game.getPlayers().get(3));
    }

}