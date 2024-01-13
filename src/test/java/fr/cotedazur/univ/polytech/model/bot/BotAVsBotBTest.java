package fr.cotedazur.univ.polytech.model.bot;

import fr.cotedazur.univ.polytech.controller.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BotAVsBotBTest {
    ArrayList<Player> players;
    Player player = new BotRandom();

    Player player2 = new BotWeak();

    Player player3 = new BotRandom();

    Player player4 = new BotWeak();

    Player player5 = new BotRandom();

    Player player6 = new BotWeak();


    Game game;


    @BeforeEach
    void setUp() {
        players = new ArrayList<>();
        players.add(player);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        players.add(player5);
        players.add(player6);
        game = new Game(players);
    }

    @Test
    void testBattleBetweenBotRandomAndWeak(){
        int numberOfWeakBot = 0;
        int numberOfRandomBot = 0;
        for(int i = 0; i < 100; i++){
            String winnerOfTheGameClass = game.startGameTest();
            if(winnerOfTheGameClass.equals(BotRandom.class.getName()))
                numberOfRandomBot++;
            else if(winnerOfTheGameClass.equals(BotWeak.class.getName()))
                numberOfWeakBot++;
            player = new BotRandom();

            player2 = new BotWeak();

            player3 = new BotRandom();

            player4 = new BotWeak();

            player5 = new BotRandom();

            player6 = new BotWeak();

            players.clear();
            players.add(player);
            players.add(player2);
            players.add(player3);
            players.add(player4);
            players.add(player5);
            players.add(player6);

            game = new Game(players);
        }
        System.out.println(((double) numberOfWeakBot / (numberOfWeakBot + numberOfRandomBot)) * 100);
        assertTrue(70 <= ((double) numberOfWeakBot / (numberOfWeakBot + numberOfRandomBot)) * 100);
    }

}
