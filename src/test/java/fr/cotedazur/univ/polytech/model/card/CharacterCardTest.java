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