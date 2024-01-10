package fr.cotedazur.univ.polytech.controller;

import fr.cotedazur.univ.polytech.model.bot.BotRandom;
import fr.cotedazur.univ.polytech.model.bot.Player;
import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import fr.cotedazur.univ.polytech.model.deck.DeckFactory;
import fr.cotedazur.univ.polytech.view.GameView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class RoundTest {
    Game game;
    Round round;
    ArrayList<Player> players;
    BotRandom botRandom1;
    BotRandom botRandom2;
    BotRandom botRandom3;
    BotRandom botRandom4;
    BotRandom botRandom5;
    BotRandom botRandom6;
    BotRandom botRandom7;

    @BeforeEach
    void setUp() {
        players = new ArrayList<>();
        botRandom1 = new BotRandom();
        botRandom2 = new BotRandom();
        botRandom3 = new BotRandom();
        botRandom4 = new BotRandom();
        botRandom5 = new BotRandom();
        botRandom6 = new BotRandom();
        botRandom7 = new BotRandom();
    }

    @Test
    void testOrderForCharacterCard() {
        players.addAll(Arrays.asList(botRandom1, botRandom2, botRandom3));
        players.get(0).setPlayerRole(CharacterCard.ARCHITECT);
        players.get(1).setPlayerRole(CharacterCard.KING);
        players.get(2).setPlayerRole(CharacterCard.THIEF);
        players.sort(Comparator.comparingInt(player -> player.getPlayerRole().getCharacterNumber()));
        assertSame(players.get(0), botRandom3);
        assertSame(players.get(1), botRandom2);
        assertSame(players.get(2), botRandom1);
        assertNotSame(players.get(0), botRandom1);
    }

    @Test
    void testNoDoubleOnBoardForOnePlayer() {
        players.addAll(Arrays.asList(botRandom1, botRandom2, botRandom3));
        game = new Game(players);
        game.startGame();
        for (Player p : players) {
            for (int i = 0; i < p.getBoard().size() - 1; i++) {
                assertNotEquals(p.getBoard().get(i), p.getBoard().get(i + 1));
            }
        }
    }

    @Test
    void testPresenceOfDiscardedKingWith4Player(){
        players.addAll(Arrays.asList(botRandom1, botRandom2, botRandom3, botRandom4));
        Round round = new Round(players, new GameView(), DeckFactory.createDistrictDeck(), DeckFactory.createEmptyDistrictDeck(),1);
        round.startRound();
        assertSame(2, round.getCharacterDiscardDeck().size());
        assertFalse(round.getCharacterDiscardDeck().contains(CharacterCard.KING));
    }

    @Test
    void testPresenceOfDiscardedKingWith5Player() {
        players.addAll(Arrays.asList(botRandom1, botRandom2, botRandom3, botRandom4, botRandom5));
        Round round = new Round(players, new GameView(), DeckFactory.createDistrictDeck(), DeckFactory.createEmptyDistrictDeck(), 1);
        round.startRound();
        assertSame(1, round.getCharacterDiscardDeck().size());
        assertNotSame(CharacterCard.KING, round.getCharacterDiscardDeck().getCards().get(0));
    }

    @Test
    void testNumberDiscardFaceUpWith6Player() {
        players.addAll(Arrays.asList(botRandom1, botRandom2, botRandom3, botRandom4, botRandom5, botRandom6));
        Round round = new Round(players, new GameView(), DeckFactory.createDistrictDeck(), DeckFactory.createEmptyDistrictDeck(), 1);
        round.startRound();
        assertSame(0, round.getCharacterDiscardDeck().size());
    }

    @Test
    void testNumberDiscardFaceUpWith7Player() {
        players.addAll(Arrays.asList(botRandom1, botRandom2, botRandom3, botRandom4, botRandom5, botRandom6, botRandom7));
        Round round = new Round(players, new GameView(), DeckFactory.createDistrictDeck(), DeckFactory.createEmptyDistrictDeck(), 1);
        round.startRound();
        assertSame(0, round.getCharacterDiscardDeck().size());
    }

    @Test
    void testSetNewCrownedPlayer() {
        botRandom1.setCrowned(true);
        botRandom1.setPlayerRole(CharacterCard.ARCHITECT);
        botRandom2.setPlayerRole(CharacterCard.KING);
        botRandom3.setPlayerRole(CharacterCard.THIEF);
        botRandom4.setPlayerRole(CharacterCard.BISHOP);
        players.addAll(Arrays.asList(botRandom1, botRandom2, botRandom3, botRandom4));

        assertTrue(botRandom1.isCrowned());
        assertFalse(botRandom2.isCrowned());
        assertFalse(botRandom3.isCrowned());
        assertFalse(botRandom4.isCrowned());

        Round round = new Round(players, new GameView(), DeckFactory.createDistrictDeck(), DeckFactory.createEmptyDistrictDeck(), 1);
        round.setNewCrownedPlayer();

        assertFalse(botRandom1.isCrowned());
        assertTrue(botRandom2.isCrowned());
        assertFalse(botRandom3.isCrowned());
        assertFalse(botRandom4.isCrowned());
    }
}
