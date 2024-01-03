package fr.cotedazur.univ.polytech.model.card;

import fr.cotedazur.univ.polytech.model.bot.BotRandom;
import fr.cotedazur.univ.polytech.model.bot.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CharacterCardTest {
    Player player;

    @BeforeEach
    void setUp() {
        player = new BotRandom();
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