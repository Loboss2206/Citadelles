package fr.cotedazur.univ.polytech.model.bot;

import fr.cotedazur.univ.polytech.logger.LamaLogger;
import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import fr.cotedazur.univ.polytech.model.card.Color;
import fr.cotedazur.univ.polytech.model.card.DistrictCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RichardTest {

    Richard botRichard;
    @Mock
    Random random = mock(Random.class);


    @BeforeEach
    void setUp() {
        LamaLogger.mute();
        botRichard = new Richard();
        botRichard.setRandom(random);
    }

    @Test
    void testSelectWhoWillBeAffectedByThiefEffect() {
        when(random.nextInt(anyInt())).thenReturn(1);
        List<CharacterCard> characterCard = new ArrayList<>(List.of(CharacterCard.values()));
        botRichard.setRoleKilledByAssassin(null);
        assertEquals(CharacterCard.MERCHANT, botRichard.selectWhoWillBeAffectedByThiefEffect(new ArrayList<Player>(), characterCard));

        //Test when discarded Card and role killed contains the constructor role
        when(random.nextInt(anyInt())).thenReturn(0);
        botRichard.setRoleKilledByAssassin(CharacterCard.MERCHANT);
        botRichard.setDiscardedCardDuringTheRound(Collections.singletonList(CharacterCard.ARCHITECT));
        assertEquals(CharacterCard.KING, botRichard.selectWhoWillBeAffectedByThiefEffect(new ArrayList<Player>(), characterCard));
    }

    @Test
    void testChooseCharacter() {
        //Take Thief
        List<CharacterCard> characterCard = new ArrayList<>(List.of(CharacterCard.values()));
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Player player = new BotRandom();
            player.setGolds(4);
            players.add(player);
        }

        players.add(botRichard); //add Richard to the list of players (to avoid the thief effect on him
        botRichard.setListCopyPlayers(players);
        assertEquals(CharacterCard.THIEF, characterCard.get(botRichard.chooseCharacter(characterCard)));

        //try to take the thief when Richard has a lot of golds
        when(random.nextInt(anyInt())).thenReturn(0);
        botRichard.setGolds(4);
        assertEquals(CharacterCard.ASSASSIN, characterCard.get(botRichard.chooseCharacter(characterCard)));

        //try to take the thief when Richard there is not someone with a lot of golds
        when(random.nextInt(anyInt())).thenReturn(3);
        botRichard.setGolds(0);
        for (Player player : players) {
            player.setGolds(2);
        }

        assertEquals(CharacterCard.KING, characterCard.get(botRichard.chooseCharacter(characterCard)));

        //Try to take thief where is not in characters choice
        characterCard.remove(CharacterCard.THIEF);
        when(random.nextInt(anyInt())).thenReturn(1);
        assertEquals(CharacterCard.MAGICIAN, characterCard.get(botRichard.chooseCharacter(characterCard)));

        //Take the Bishop
        characterCard = new ArrayList<>(List.of(CharacterCard.values()));
        players = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Player player = new BotRandom();
            player.setGolds(5);
            players.add(player);
        }
        botRichard.getBoard().add(DistrictCard.TEMPLE);
        botRichard.setListCopyPlayers(players);
        assertEquals(CharacterCard.BISHOP, characterCard.get(botRichard.chooseCharacter(characterCard)));
    }

    @Test
    void testUseEffect() {
        //Test when Richard put the card in the board true
        botRichard.setGolds(5);
        botRichard.setListCopyPlayers(new ArrayList<>());
        botRichard.getBoard().add(DistrictCard.TEMPLE);
        assertTrue(botRichard.wantToUseEffect(true));
        assertEquals(5, botRichard.getGolds());
        assertEquals(1, botRichard.getBoard().size());

        botRichard.getBoard().clear();

        //Test when Richard put the card in the board false
        botRichard.setGolds(5);
        botRichard.getBoard().add(DistrictCard.TEMPLE);
        assertTrue(botRichard.wantToUseEffect(false));
        assertEquals(5, botRichard.getGolds());
        assertEquals(1, botRichard.getBoard().size());
        assertEquals(DistrictCard.TEMPLE, botRichard.getBoard().get(0));
    }
}