package fr.cotedazur.univ.polytech.model.card;


import fr.cotedazur.univ.polytech.logger.LamaLogger;
import fr.cotedazur.univ.polytech.model.bot.BotRandom;
import fr.cotedazur.univ.polytech.model.bot.BotWeak;
import fr.cotedazur.univ.polytech.model.bot.Player;
import fr.cotedazur.univ.polytech.model.deck.Deck;
import fr.cotedazur.univ.polytech.model.golds.StackOfGolds;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class CharacterCardTest {
    Player player;

    @Mock
    Random random = mock(Random.class);
    BotRandom botRandom1;

    @BeforeEach
    void setUp() {
        LamaLogger.mute();
        player = new BotRandom();
        botRandom1 = new BotRandom();
        botRandom1.setRandom(random);
    }

    @Test
    void testUseEffectForKing() {
        player.setPlayerRole(CharacterCard.KING);

        player.addCardToBoard(DistrictCard.CASTLE);
        player.addCardToBoard(DistrictCard.MARKET);
        player.addCardToBoard(DistrictCard.MANOR);
        player.addCardToBoard(DistrictCard.PALACE);
        player.setGolds(0);

        player.getPlayerRole().useEffect(player, new StackOfGolds(), null);
        assertEquals(3, player.getGolds());
    }

    /*@Test
    void testEffectForArchitect() {
        districtDeck = DeckFactory.createEmptyDistrictDeck();
        districtDeck.add(DistrictCard.MANOR);
        districtDeck.add(DistrictCard.HAUNTED_CITY);
        botRandom1.setPlayerRole(CharacterCard.ARCHITECT);
        botRandom1.setGolds(50);

        when(random.nextInt(anyInt())).thenReturn(0);

        botRandom1.getPlayerRole().useEffect(player, new StackOfCoins())(botRandom1, districtDeck);
        botRandom1.getPlayerRole().useEffect(player, new StackOfCoins())(botRandom1, (Player) null);

        assertEquals(0, botRandom1.getHands().size());
        assertEquals(45, botRandom1.getGolds());

        botRandom1.setGolds(50);
        botRandom1.getHands().clear();
        botRandom1.getBoard().clear();
        districtDeck = DeckFactory.createEmptyDistrictDeck();
        districtDeck.add(DistrictCard.MANOR);
        districtDeck.add(DistrictCard.HAUNTED_CITY);
        botRandom1.setPlayerRole(CharacterCard.ARCHITECT);
        when(random.nextInt(anyInt())).thenReturn(1);

        botRandom1.getPlayerRole().useEffect(player, new StackOfCoins())(botRandom1, districtDeck);
        botRandom1.getPlayerRole().useEffect(player, new StackOfCoins())(botRandom1, (Player) null);

        assertEquals(2, botRandom1.getHands().size());
        assertEquals(50, botRandom1.getGolds());
    }*/

    @Test
    void testUseEffectForMerchant() {
        player.setGolds(50);

        player.setPlayerRole(CharacterCard.MERCHANT);

        player.addCardToBoard(DistrictCard.CASTLE);
        player.addCardToBoard(DistrictCard.MARKET);
        player.addCardToBoard(DistrictCard.TAVERN);
        player.addCardToBoard(DistrictCard.PALACE);
        player.getPlayerRole().useEffect(player, new StackOfGolds(), null);
        //The golds don't change because it's the game that manages the golds
        assertEquals(52, player.getGolds());

        player.addCardToBoard(DistrictCard.DOCKS);
        player.getPlayerRole().useEffect(player, new StackOfGolds(), null);
        // Adding a green district (1 gold) and the passive effect of the merchant (1 gold) minus the cost of the district (3 golds)
        // 41 + 1 + 1 + 1 + 1 - 3 = 42 golds
        assertEquals(55, player.getGolds());

        player.addCardToBoard(DistrictCard.CATHEDRAL);
        player.getPlayerRole().useEffect(player, new StackOfGolds(), null);
        // Adding a blue district (0 golds) and the passive effect of the merchant (1 gold) minus the cost of the district (5 golds)
        // 42 + 1 + 1 + 1 + 1 + 0 - 5 = 41 golds
        assertEquals(58, player.getGolds());
    }

    @Test
    void testUseEffectForBishop() {
        player.setGolds(28);
        player.setPlayerRole(CharacterCard.BISHOP);
        player.addCardToBoard(DistrictCard.CASTLE);
        player.addCardToBoard(DistrictCard.MARKET);
        player.addCardToBoard(DistrictCard.MONASTERY);
        player.addCardToBoard(DistrictCard.TEMPLE);
        player.getPlayerRole().useEffect(player, new StackOfGolds(), null);
        //Should be 20 because when we add a district on the board we withdraw from his golds
        assertEquals(30, player.getGolds());

        player.addCardToBoard(DistrictCard.CATHEDRAL);
        player.getPlayerRole().useEffect(player, new StackOfGolds(), null);

        //Should be 18 because when we add a district on the board we withdraw from his golds, and now we have 3 blue district, so we add 3 in the number of golds instead of 2
        assertEquals(33, player.getGolds());
    }

    @Test
    void testUseEffectForThief() {
        player = new BotWeak();
        player.setGolds(20);
        player.setPlayerRole(CharacterCard.THIEF);

        Player player2 = new BotRandom();
        player2.setGolds(31);

        player.getPlayerRole().useEffectThief(player, player2, true);
        assertEquals(51, player.getGolds());
        assertEquals(0, player2.getGolds());

        //Test when assassin (should not take the golds)
        player2.setGolds(31);
        player2.setPlayerRole(CharacterCard.ASSASSIN);
        player.getPlayerRole().useEffectThief(player, player2, true);
        assertEquals(51, player.getGolds());
        assertEquals(31, player2.getGolds());
    }

    @Test
    void testUseEffectForMagicianWithPlayer() {
        player = new BotWeak();
        List<DistrictCard> districts = new ArrayList<>();
        districts.add(DistrictCard.SMITHY);
        districts.add(DistrictCard.DRAGON_GATE);
        player.setHands(districts);
        player.setPlayerRole(CharacterCard.MAGICIAN);

        List<DistrictCard> districtsp2 = new ArrayList<>();
        districtsp2.add(DistrictCard.FORTRESS);
        districtsp2.add(DistrictCard.CATHEDRAL);
        Player player2 = new BotRandom();
        player2.setHands(districtsp2);

        player.getPlayerRole().useEffectMagicianWithPlayer(player, player2);
        assertEquals(districts, player2.getHands());
        assertEquals(districtsp2, player.getHands());
    }

    @Test
    void testUseEffectForMagicianWithDeck() {
        Deck<DistrictCard> Deck = new Deck<>();
        Deck.add(DistrictCard.TAVERN);
        Deck.add(DistrictCard.LIBRARY);
        Deck.add(DistrictCard.KEEP);
        player = new BotWeak();
        List<DistrictCard> districts = new ArrayList<>();
        List<DistrictCard> districtsDiscard = new ArrayList<>();
        districts.add(DistrictCard.SMITHY);
        districts.add(DistrictCard.MARKET);
        districts.add(DistrictCard.UNIVERSITY);
        districtsDiscard.add(DistrictCard.SMITHY);
        districtsDiscard.add(DistrictCard.UNIVERSITY);
        player.setHands(districts);

        player.setPlayerRole(CharacterCard.MAGICIAN);
        player.getPlayerRole().useEffectMagicianWithDeck(player, districtsDiscard, Deck);
        assertEquals(DistrictCard.MARKET, player.getHands().get(0));
        assertEquals(DistrictCard.TAVERN, player.getHands().get(1));
        assertEquals(DistrictCard.LIBRARY, player.getHands().get(2));

    }

    @Test
    void earnGoldsFromDistricts() {
        CharacterCard characterCard;

        // Tests for the King
        characterCard = CharacterCard.KING;

        assertEquals(0, player.getGolds());

        player.addCardToBoard(DistrictCard.CASTLE);
        player.setGolds(0);

        characterCard.earnGoldsFromDistricts(player, Color.YELLOW, new StackOfGolds());
        assertEquals(1, player.getGolds());

        player.addCardToBoard(DistrictCard.MARKET);
        player.setGolds(0);

        characterCard.earnGoldsFromDistricts(player, Color.YELLOW, new StackOfGolds());
        assertEquals(1, player.getGolds());

        player.addCardToBoard(DistrictCard.MANOR);
        player.addCardToBoard(DistrictCard.PALACE);
        player.setGolds(0);

        characterCard.earnGoldsFromDistricts(player, Color.YELLOW, new StackOfGolds());
        assertEquals(3, player.getGolds());
    }
}