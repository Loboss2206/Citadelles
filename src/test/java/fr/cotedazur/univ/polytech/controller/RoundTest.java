package fr.cotedazur.univ.polytech.controller;

import fr.cotedazur.univ.polytech.model.bot.BotRandom;
import fr.cotedazur.univ.polytech.model.bot.Player;
import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;


class RoundTest {

    ArrayList<Player> players;
    BotRandom botRandom1;
    BotRandom botRandom2;

    BotRandom botRandom3;


    @BeforeEach
    void setUp() {
        players = new ArrayList<>();
        botRandom1 = new BotRandom();
        botRandom2 = new BotRandom();
        botRandom3 = new BotRandom();
        players.add(botRandom1);
        players.add(botRandom2);
        players.add(botRandom3);
    }

    @Test
    void testOrderForCaracterCard(){
        players.get(0).setPlayerRole(CharacterCard.ARCHITECT);
        players.get(1).setPlayerRole(CharacterCard.KING);
        players.get(2).setPlayerRole(CharacterCard.THIEF);
        players.sort(Comparator.comparingInt(player->player.getPlayerRole().getCharacterNumber()));
        assertSame(players.get(0), botRandom3);
        assertSame(players.get(1), botRandom2);
        assertSame(players.get(2), botRandom1);
        assertNotSame(players.get(0), botRandom1);

    }
}
