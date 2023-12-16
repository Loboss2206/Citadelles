package fr.cotedazur.univ.polytech.controller;

import fr.cotedazur.univ.polytech.model.bot.BotRandom;
import fr.cotedazur.univ.polytech.model.bot.Player;
import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import fr.cotedazur.univ.polytech.model.deck.CharacterDeck;
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
    ArrayList<Player> players;
    BotRandom botRandom1;
    BotRandom botRandom2;
    BotRandom botRandom3;
    BotRandom botRandom4;
    BotRandom botRandom5;
    BotRandom botRandom6;
    BotRandom botRandom7;

    CharacterDeck charactersDiscardDeck;

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
        charactersDiscardDeck = DeckFactory.createEmptyCharacterDeck();
    }

    @Test
    void testOrderForCharacterCard() {
        players.addAll(Arrays.asList(botRandom1, botRandom2, botRandom3));
        players.get(0).setPlayerRole(CharacterCard.ARCHITECT);
        players.get(1).setPlayerRole(CharacterCard.KING);
        players.get(2).setPlayerRole(CharacterCard.THIEF);
        players.sort(Comparator.comparingInt(player -> player.getPlayerRole().getCharacterNumber()));
        assertEquals(players.get(0), botRandom3);
        assertEquals(players.get(1), botRandom2);
        assertEquals(players.get(2), botRandom1);
        assertNotEquals(players.get(0), botRandom1);
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
        players.add(botRandom1);
        players.add(botRandom2);
        players.add(botRandom3);
        players.add(botRandom4);
        Round round = new Round(players, new GameView(new Game(players)), DeckFactory.createDistrictDeck(), DeckFactory.createEmptyDistrictDeck(), DeckFactory.createCharacterDeck(), charactersDiscardDeck,1);
        round.startRound();
        assertSame(charactersDiscardDeck.size(),3);
        assertFalse(charactersDiscardDeck.getCards().get(1) == CharacterCard.KING  || charactersDiscardDeck.getCards().get(2) == CharacterCard.KING);
    }

    @Test
    void testPresenceOfDiscardedKingWith5Player() {
        players.add(botRandom1);
        players.add(botRandom2);
        players.add(botRandom3);
        players.add(botRandom4);
        players.add(botRandom5);
        Round round = new Round(players, new GameView(new Game(players)), DeckFactory.createDistrictDeck(), DeckFactory.createEmptyDistrictDeck(), DeckFactory.createCharacterDeck(), charactersDiscardDeck, 1);
        round.startRound();
        assertSame(charactersDiscardDeck.size(), 2);
        assertNotSame(charactersDiscardDeck.getCards().get(1), CharacterCard.KING);
    }

    @Test
    void testPresenceOfDiscardedKingWith6Player() {
        players.add(botRandom1);
        players.add(botRandom2);
        players.add(botRandom3);
        players.add(botRandom4);
        players.add(botRandom5);
        players.add(botRandom6);
        Round round = new Round(players, new GameView(new Game(players)), DeckFactory.createDistrictDeck(), DeckFactory.createEmptyDistrictDeck(), DeckFactory.createCharacterDeck(), charactersDiscardDeck, 1);
        round.startRound();
        assertSame(charactersDiscardDeck.size(), 1);
    }

    @Test
    void testPresenceOfDiscardedKingWith7Player() {
        players.add(botRandom1);
        players.add(botRandom2);
        players.add(botRandom3);
        players.add(botRandom4);
        players.add(botRandom5);
        players.add(botRandom6);
        players.add(botRandom7);
        Round round = new Round(players, new GameView(new Game(players)), DeckFactory.createDistrictDeck(), DeckFactory.createEmptyDistrictDeck(), DeckFactory.createCharacterDeck(), charactersDiscardDeck, 1);
        round.startRound();
        assertSame(charactersDiscardDeck.size(), 2);
    }
}
