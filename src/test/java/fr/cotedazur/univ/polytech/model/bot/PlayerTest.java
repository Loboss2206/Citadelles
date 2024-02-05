package fr.cotedazur.univ.polytech.model.bot;


import fr.cotedazur.univ.polytech.logger.LamaLogger;
import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import fr.cotedazur.univ.polytech.model.card.DistrictCard;
import fr.cotedazur.univ.polytech.model.deck.Deck;
import fr.cotedazur.univ.polytech.model.deck.DeckFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    BotRandom botRandom1;
    BotRandom botRandom0;

    BotRandom botRandom2;
    Map<DispatchState, ArrayList<DistrictCard>> cardsThatThePlayerDontWantAndThatThePlayerWant = new EnumMap<>(DispatchState.class);
    private Deck<DistrictCard> districtDeck;

    @BeforeEach
    void setUp() {
        LamaLogger.mute();
        Player.setCount(0); // because the static variable changes during all the tests (of this class or another) which is fail this class tests
        botRandom0 = new BotRandom();
        botRandom1 = new BotRandom();
        botRandom2 = new BotRandom();
        this.districtDeck = DeckFactory.createDistrictDeck();
        this.districtDeck.shuffle();
        cardsThatThePlayerDontWantAndThatThePlayerWant.put(DispatchState.CARDS_WANTED, new ArrayList<>());
        cardsThatThePlayerDontWantAndThatThePlayerWant.put(DispatchState.CARDS_NOT_WANTED, new ArrayList<>());
    }

    /**
     * Test if the name is automatically and correctly created
     */
    @Test
    void testNameCreation() {
        assertEquals("BOT0", botRandom0.getName());
        assertEquals("BOT1", botRandom1.getName());
        assertEquals("BOT2", botRandom2.getName());

        assertNotEquals(botRandom0.getName(), botRandom1.getName());
    }

    @Test
    void testCollect2Golds() {
        botRandom2.collectTwoGolds();
        botRandom2.collectTwoGolds();
        assertEquals(4, botRandom2.getGolds());
    }

    @Test
    void testDrawCard() {
        Deck<DistrictCard> copyDeck;
        copyDeck = DeckFactory.createDistrictDeck();
        botRandom2.drawCard(cardsThatThePlayerDontWantAndThatThePlayerWant, districtDeck.draw(), districtDeck.draw());
        districtDeck.add(cardsThatThePlayerDontWantAndThatThePlayerWant.get(DispatchState.CARDS_NOT_WANTED).get(0));
        //If the card is correctly removed
        assertEquals(65, copyDeck.size());
        assertEquals(64, districtDeck.size());
    }

    @Test
    void testAddCardOnTheBoard() {
        botRandom2.getHands().add(DistrictCard.TRADING_POST);
        botRandom2.addCardToBoard(DistrictCard.TRADING_POST);
        assertNotNull(botRandom2.getBoard().get(0));
        assertEquals(DistrictCard.TRADING_POST, botRandom2.getBoard().get(0));

        botRandom2.getBoard().clear();
        botRandom2.addCardToBoard(DistrictCard.TRADING_POST);
        assertEquals(1, botRandom2.getBoard().size());
    }

    @Test
    void testAddCardOnTheHand() {
        assertTrue(botRandom2.getHands().isEmpty());
        botRandom2.addCardToHand(DistrictCard.TRADING_POST);
        assertNotNull(botRandom2.getHands().get(0));
        assertEquals(DistrictCard.TRADING_POST, botRandom2.getHands().get(0));

        botRandom2.getHands().clear();
        botRandom2.addCardToHand(DistrictCard.CASTLE);
        assertEquals(1, botRandom2.getHands().size());
    }

    @Test
    void hasCardOnTheBoard() {
        botRandom2.getHands().add(DistrictCard.TRADING_POST);
        botRandom2.getHands().add(DistrictCard.TRADING_POST);
        botRandom2.addCardToBoard(DistrictCard.TRADING_POST);
        assertNotNull(botRandom2.getBoard().get(0));
        assertEquals(DistrictCard.TRADING_POST, botRandom2.getBoard().get(0));
        assertTrue(botRandom2.hasCardOnTheBoard(DistrictCard.TRADING_POST));

        botRandom2.getBoard().clear();
        assertFalse(botRandom2.hasCardOnTheBoard(DistrictCard.TRADING_POST));
    }

    @Test
    void hasPlayableCard() {
        botRandom2.getHands().clear();
        assertFalse(botRandom2.hasPlayableCard());

        botRandom2.getHands().add(DistrictCard.TRADING_POST);
        botRandom2.setGolds(3);
        botRandom2.discoverValidCard();

        assertTrue(botRandom2.hasPlayableCard());

        botRandom2.addCardToBoard(DistrictCard.TRADING_POST);
        botRandom2.getHands().add(DistrictCard.TRADING_POST);
        assertFalse(botRandom2.hasPlayableCard());
    }

    @Test
    void testDiscoverValidCard() {
        botRandom2.getHands().add(DistrictCard.TRADING_POST);
        botRandom2.getHands().add(DistrictCard.MANOR);
        botRandom2.getHands().add(DistrictCard.CASTLE);

        botRandom2.setGolds(5);
        botRandom2.discoverValidCard();
        assertEquals(3, botRandom2.validCards.size());

        botRandom2.setGolds(4);
        botRandom2.discoverValidCard();
        assertEquals(3, botRandom2.validCards.size());

        botRandom2.setGolds(3);
        botRandom2.discoverValidCard();
        assertEquals(2, botRandom2.validCards.size());
    }

    @Test
    void testCopy() {
        botRandom2.getHands().add(DistrictCard.TRADING_POST);
        botRandom2.getHands().add(DistrictCard.MANOR);
        botRandom2.getHands().add(DistrictCard.CASTLE);
        botRandom2.getHands().add(DistrictCard.PALACE);
        botRandom2.setNbCardsInHand(4);
        botRandom2.setGolds(30);
        botRandom2.addCardToBoard(DistrictCard.TRADING_POST);
        botRandom2.addCardToBoard(DistrictCard.MANOR);
        botRandom2.addCardToBoard(DistrictCard.CASTLE);
        botRandom2.setPlayerRole(CharacterCard.ASSASSIN);

        Player copy = botRandom2.copy();

        assertNotEquals(botRandom2.getHands(), copy.getHands());
        assertEquals(botRandom2.getGolds(), copy.getGolds());
        assertEquals(botRandom2.getBoard(), copy.getBoard());
        assertEquals(botRandom2.getName(), copy.getName());
        assertEquals(botRandom2.getHands().size(), copy.getNbCardsInHand());
        assertEquals(botRandom2.isCrowned(), copy.isCrowned());
        assertNotEquals(botRandom2, botRandom1);
        assertNotSame(botRandom2, copy);
    }

    @Test
    void testPlayerHasADestroyableDistrict() {
        botRandom1.setPlayerRole(CharacterCard.WARLORD);
        botRandom2.setPlayerRole(CharacterCard.KING);

        botRandom1.setGolds(30);
        botRandom2.setGolds(30);

        // botRandom2 has no district
        assertFalse(botRandom2.playerHasADestroyableDistrict(botRandom1));

        botRandom2.addCardToBoard(DistrictCard.PALACE);

        // botRandom2 has a district
        assertTrue(botRandom2.playerHasADestroyableDistrict(botRandom1));

        botRandom1.setGolds(3);

        // Because not enough golds to destroy (3 golds to destroy a 5-value district)
        assertFalse(botRandom2.playerHasADestroyableDistrict(botRandom1));

        botRandom1.setGolds(30);
        botRandom2.setPlayerRole(CharacterCard.BISHOP);

        // Enough golds to destroy but botRandom2 is a bishop
        assertFalse(botRandom2.playerHasADestroyableDistrict(botRandom1));
    }

    @Test
    void testHashCode(){
        assertEquals(botRandom0.hashCode(), botRandom0.hashCode());
        assertNotEquals(botRandom0.hashCode(), botRandom1.hashCode());
        assertNotEquals(botRandom0.hashCode(), botRandom2.hashCode());
    }
}