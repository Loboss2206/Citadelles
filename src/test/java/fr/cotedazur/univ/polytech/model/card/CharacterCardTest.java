package fr.cotedazur.univ.polytech.model.card;

import fr.cotedazur.univ.polytech.controller.Game;
import fr.cotedazur.univ.polytech.controller.Round;
import fr.cotedazur.univ.polytech.model.bot.BotRandom;
import fr.cotedazur.univ.polytech.model.bot.BotWeak;
import fr.cotedazur.univ.polytech.model.bot.Player;
import fr.cotedazur.univ.polytech.model.deck.Deck;
import fr.cotedazur.univ.polytech.model.deck.DeckFactory;
import fr.cotedazur.univ.polytech.view.GameView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CharacterCardTest {
    Player player;
    Deck<DistrictCard> districtDeck;

    @Mock
    Random random = mock(Random.class);
    BotRandom botRandom1;

    @BeforeEach
    void setUp() {
        player = new BotRandom();
        botRandom1 = new BotRandom();
        botRandom1.setRandom(random);
    }

    @Test
    void useEffectForKing() {
        player.setPlayerRole(CharacterCard.KING);

        player.addCardToBoard(DistrictCard.CASTLE);
        player.addCardToBoard(DistrictCard.MARKET);
        player.addCardToBoard(DistrictCard.MANOR);
        player.addCardToBoard(DistrictCard.PALACE);
        player.setGolds(0);

        player.getPlayerRole().useEffect(player);
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

        botRandom1.getPlayerRole().useEffect(botRandom1, districtDeck);
        botRandom1.getPlayerRole().useEffect(botRandom1, (Player) null);

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

        botRandom1.getPlayerRole().useEffect(botRandom1, districtDeck);
        botRandom1.getPlayerRole().useEffect(botRandom1, (Player) null);

        assertEquals(2, botRandom1.getHands().size());
        assertEquals(50, botRandom1.getGolds());
    }*/
  
  @Test
    void useEffectForMerchant() {
        player.setGolds(50);

        player.setPlayerRole(CharacterCard.MERCHANT);

        player.addCardToBoard(DistrictCard.CASTLE);
        player.addCardToBoard(DistrictCard.MARKET);
        player.addCardToBoard(DistrictCard.TAVERN);
        player.addCardToBoard(DistrictCard.PALACE);
        player.getPlayerRole().useEffect(player);
        // We count the decrease of the putted cards (50-4-2-1-5 = 38 golds left)
        // Then we add the 1 gold for the merchant and 1 gold per green district (38+1+1+1 = 41 golds)
        assertEquals(41, player.getGolds());

        player.addCardToBoard(DistrictCard.DOCKS);
        player.getPlayerRole().useEffect(player);
        // Adding a green district (1 gold) and the passive effect of the merchant (1 gold) minus the cost of the district (3 golds)
        // 41 + 1 + 1 + 1 + 1 - 3 = 42 golds
        assertEquals(42, player.getGolds());

        player.addCardToBoard(DistrictCard.CATHEDRAL);
        player.getPlayerRole().useEffect(player);
        // Adding a blue district (0 golds) and the passive effect of the merchant (1 gold) minus the cost of the district (5 golds)
        // 42 + 1 + 1 + 1 + 1 + 0 - 5 = 41 golds
        assertEquals(41, player.getGolds());
    }
  
  @Test
    void useEffectForBishop() {
        player.setGolds(28);
        player.setPlayerRole(CharacterCard.BISHOP);
        player.addCardToBoard(DistrictCard.CASTLE);
        player.addCardToBoard(DistrictCard.MARKET);
        player.addCardToBoard(DistrictCard.MONASTERY);
        player.addCardToBoard(DistrictCard.TEMPLE);
        player.getPlayerRole().useEffect(player);
        //Should be 20 because when we add a district on the board we withdraw from his golds
        assertEquals(20, player.getGolds());

        player.addCardToBoard(DistrictCard.CATHEDRAL);
        player.getPlayerRole().useEffect(player);

        //Should be 18 because when we add a district on the board we withdraw from his golds, and now we have 3 blue district, so we add 3 in the number of golds instead of 2
        assertEquals(18, player.getGolds());
    }
    @Test
    void useEffectForThief() {
        player = new BotWeak();
        ArrayList<Player> listPlayer= new ArrayList<>();
        player.setGolds(20);
        player.setPlayerRole(CharacterCard.THIEF);

        Player player2 = new BotRandom();
        player2.setGolds(31);

        player.getPlayerRole().useEffectThief(player,player2);
        assertEquals(51,player.getGolds());
        assertEquals(0,player2.getGolds());

        //Test when assassin (should not take the golds)
        player2.setGolds(31);
        player2.setPlayerRole(CharacterCard.ASSASSIN);
        player.getPlayerRole().useEffectThief(player,player2);
        assertEquals(51,player.getGolds());
        assertEquals(31,player2.getGolds());
    }
    @Test
    void useEffectForMagicianWithPlayer(){
        player = new BotWeak();
        ArrayList<Player> listPlayer= new ArrayList<>();
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

        player.getPlayerRole().useEffectMagicianWithPlayer(player,player2);
        assertEquals(districts,player2.getHands());
        assertEquals(districtsp2,player.getHands());
    }

    @Test
    void useEffectForMagicianWithDeck(){
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
        System.out.println(player.getHands());


        player.getPlayerRole().useEffectMagicianWithDeck(player,districtsDiscard,Deck);
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

        characterCard.earnGoldsFromDistricts(player, Color.YELLOW);
        assertEquals(1, player.getGolds());

        player.addCardToBoard(DistrictCard.MARKET);
        player.setGolds(0);

        characterCard.earnGoldsFromDistricts(player, Color.YELLOW);
        assertEquals(1, player.getGolds());

        player.addCardToBoard(DistrictCard.MANOR);
        player.addCardToBoard(DistrictCard.PALACE);
        player.setGolds(0);

        characterCard.earnGoldsFromDistricts(player, Color.YELLOW);
        assertEquals(3, player.getGolds());
    }
}