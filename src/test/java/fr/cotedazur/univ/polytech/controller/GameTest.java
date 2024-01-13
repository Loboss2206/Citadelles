package fr.cotedazur.univ.polytech.controller;

import fr.cotedazur.univ.polytech.model.bot.BotRandom;
import fr.cotedazur.univ.polytech.model.bot.Player;
import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import fr.cotedazur.univ.polytech.model.card.DistrictCard;
import fr.cotedazur.univ.polytech.view.GameView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameTest {
    ArrayList<Player> players;
    Game game;

    @BeforeEach
    void setUp() {
        players = new ArrayList<>(Arrays.asList(new BotRandom(), new BotRandom(), new BotRandom(), new BotRandom()));
        game = new Game(players, new GameView());
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
        players.clear();
        Player player = new BotRandom();
        Player player2 = new BotRandom();
        Player player3 = new BotRandom();
        Player player4 = new BotRandom();
        players.add(player);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        player.setFirstToAdd8district(true);
        player.setPlayerRole(CharacterCard.ASSASSIN);
        player.addCardToBoard(DistrictCard.MARKET);
        player.addCardToBoard(DistrictCard.MANOR);
        player.addCardToBoard(DistrictCard.SCHOOL_OF_MAGIC);
        player.addCardToBoard(DistrictCard.PRISON);
        player.addCardToBoard(DistrictCard.CHURCH);

        player2.setPlayerRole(CharacterCard.WARLORD);
        player2.addCardToBoard(DistrictCard.MARKET);
        player2.addCardToBoard(DistrictCard.MANOR);
        player2.addCardToBoard(DistrictCard.SCHOOL_OF_MAGIC);
        player2.addCardToBoard(DistrictCard.PRISON);
        player2.addCardToBoard(DistrictCard.CHURCH);

        player3.setPlayerRole(CharacterCard.ARCHITECT);

        player4.setPlayerRole(CharacterCard.BISHOP);
        game.calculatePoints();

        assertEquals(0,player3.getPoints());
        assertEquals(0,player4.getPoints());
        assertEquals(22,player.getPoints());
        assertEquals(18,player2.getPoints());
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
        game = new Game(players,new GameView());

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
        game = new Game(players,new GameView());

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