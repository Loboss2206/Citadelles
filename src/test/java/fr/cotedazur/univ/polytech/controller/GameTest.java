package fr.cotedazur.univ.polytech.controller;

import fr.cotedazur.univ.polytech.model.bot.BotRandom;
import fr.cotedazur.univ.polytech.model.bot.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

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

    //Difficult to test if we don't know the player action
    /*@Test
    void testRankingWithOnePlayerWithOnlyGoldsAndOnlyOneRound() {
        game.startGame();
        Assertions.assertEquals(2, game.getPlayers().get(0).getGolds());
    }*/
}