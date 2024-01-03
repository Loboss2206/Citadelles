package fr.cotedazur.univ.polytech.model.card;

import fr.cotedazur.univ.polytech.model.bot.BotRandom;
import fr.cotedazur.univ.polytech.model.bot.Player;
import fr.cotedazur.univ.polytech.model.deck.DeckFactory;
import fr.cotedazur.univ.polytech.model.deck.DistrictDeck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CharacterCardTest {
    Player player;
    DistrictDeck districtDeck;

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

    @Test
    void testEffectForArchitect() {
        districtDeck = new DistrictDeck();
        districtDeck.add(DistrictCard.MANOR);
        districtDeck.add(DistrictCard.HAUNTED_CITY);
        botRandom1.setPlayerRole(CharacterCard.ARCHITECT);
        botRandom1.setGolds(50);

        when(random.nextInt(anyInt())).thenReturn(0);

        botRandom1.getPlayerRole().useEffect(botRandom1, districtDeck, null);

        assertEquals(0, botRandom1.getHands().size());
        assertEquals(45, botRandom1.getGolds());

        botRandom1.setGolds(50);
        botRandom1.getHands().clear();
        botRandom1.getBoard().clear();
        districtDeck = new DistrictDeck();
        districtDeck.add(DistrictCard.MANOR);
        districtDeck.add(DistrictCard.HAUNTED_CITY);
        botRandom1.setPlayerRole(CharacterCard.ARCHITECT);
        when(random.nextInt(anyInt())).thenReturn(1);

        botRandom1.getPlayerRole().useEffect(botRandom1, districtDeck, null);

        assertEquals(2, botRandom1.getHands().size());
        assertEquals(50, botRandom1.getGolds());
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