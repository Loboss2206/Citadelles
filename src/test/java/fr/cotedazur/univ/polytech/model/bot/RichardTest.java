package fr.cotedazur.univ.polytech.model.bot;

import fr.cotedazur.univ.polytech.logger.LamaLogger;
import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import fr.cotedazur.univ.polytech.model.card.DistrictCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
        botRichard.setListCopyPlayers(players);
        assertEquals(CharacterCard.THIEF, characterCard.get(botRichard.chooseCharacter(characterCard)));

        //try to take the thief when Richard has a lot of golds
        when(random.nextInt(anyInt())).thenReturn(2);
        botRichard.setGolds(4);
        assertEquals(CharacterCard.MAGICIAN, characterCard.get(botRichard.chooseCharacter(characterCard)));

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

        //take Assassin to kill magician
        botRichard.setGolds(4);
        botRichard.getHands().add(DistrictCard.FORTRESS);
        botRichard.getHands().add(DistrictCard.DRAGON_GATE);
        botRichard.getHands().add(DistrictCard.CASTLE);
        botRichard.getHands().add(DistrictCard.DOCKS);
        botRichard.getHands().add(DistrictCard.SCHOOL_OF_MAGIC);
        assertEquals(CharacterCard.ASSASSIN, characterCard.get(botRichard.chooseCharacter(characterCard)));
        assertEquals(CharacterCard.MAGICIAN, botRichard.getTarget());

        //try to take assassin with not enough cards in hands
        when(random.nextInt(anyInt())).thenReturn(2);
        botRichard.getHands().remove(DistrictCard.DOCKS);
        assertEquals(CharacterCard.KING, characterCard.get(botRichard.chooseCharacter(characterCard)));

        //try to take assassin but every player has at least 1 card
        botRichard.getHands().add(DistrictCard.DOCKS);
        for (Player player : players) {
            player.addCardToHand(DistrictCard.CHURCH);
        }
        assertEquals(CharacterCard.KING, characterCard.get(botRichard.chooseCharacter(characterCard)));
    }

    @Test
    void testOnlyOneWith1GoldDistrict() {
        //Richard is the only one with a 1 gold district
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Player player = new BotRandom();
            player.addCardToBoard(DistrictCard.CHURCH);
            players.add(player);
        }
        botRichard.addCardToBoard(DistrictCard.TEMPLE);
        players.add(botRichard);
        assertTrue(botRichard.onlyOneWith1GoldDistrict(players));
        //no-one has 1 gold district
        botRichard.getBoard().remove(DistrictCard.TEMPLE);
        assertFalse(botRichard.onlyOneWith1GoldDistrict(players));

        //everyone has 1 gold district
        botRichard.addCardToBoard(DistrictCard.TEMPLE);
        for (Player player : players) {
            player.addCardToBoard(DistrictCard.TAVERN);
        }
        assertFalse(botRichard.onlyOneWith1GoldDistrict(players));
    }

    @Test
    void testIsFirst() {
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Player player = new BotRandom();
            player.addCardToBoard(DistrictCard.SCHOOL_OF_MAGIC);
            players.add(player);
        }
        //Richard has the most points
        botRichard.addCardToBoard(DistrictCard.DRAGON_GATE);
        players.add(botRichard);
        //Richard hasn't the most points
        assertTrue(botRichard.isFirst(players));
        players.get(0).addCardToBoard(DistrictCard.CASTLE);
        assertFalse(botRichard.isFirst(players));
        //Richard is tied on top
        botRichard.addCardToBoard(DistrictCard.CHURCH);
        assertTrue(botRichard.isFirst(players));
    }
    @Test
    void testSomeHasNoCards(){
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Player player = new BotRandom();
            players.add(player);
        }
        //no-one has cards in hand
        players.add(botRichard);
        assertTrue(botRichard.someoneHasNoCards(players));
        //Richard is the only one who has card
        botRichard.addCardToHand(DistrictCard.SMITHY);
        assertTrue(botRichard.someoneHasNoCards(players));
        for (Player player : players) {
            player.addCardToHand(DistrictCard.SCHOOL_OF_MAGIC);
        }
        assertFalse(botRichard.someoneHasNoCards(players));
    }
    @Test
    void testSomeoneIsGoingToGetRich(){
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Player player = new BotRandom();
            player.setGolds(4);
            players.add(player);
        }
        //everyone is rich
        botRichard.setGolds(4);
        players.add(botRichard);
        assertFalse(botRichard.someoneIsGoingToGetRich(players));

        //Richard is the only one to be poor
        botRichard.setGolds(0);
        assertFalse(botRichard.someoneIsGoingToGetRich(players));

        //1 bot is poor and 3 are rich
        players.get(0).setGolds(1);
        assertTrue(botRichard.someoneIsGoingToGetRich(players));
    }
    @Test
    void testWhatCharacterGotTookByGoodPlayer(){
        List<CharacterCard> characterCard = new ArrayList<>(List.of(CharacterCard.values()));
        List<CharacterCard> discardedCard = new ArrayList<>();
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Player player = new BotRandom();
            player.setGolds(4);
            players.add(player);
        }
        players.add(botRichard);
        players.get(0).addCardToBoard(DistrictCard.SCHOOL_OF_MAGIC);
        players.get(0).addCardToBoard(DistrictCard.SMITHY);
        players.get(0).addCardToBoard(DistrictCard.CASTLE);
        players.get(0).addCardToBoard(DistrictCard.BATTLEFIELD);
        players.get(0).addCardToBoard(DistrictCard.DRAGON_GATE);
        players.get(0).addCardToBoard(DistrictCard.TAVERN);
        botRichard.setListCopyPlayers(players);
        discardedCard.add(CharacterCard.ASSASSIN);
        discardedCard.add(CharacterCard.MERCHANT);
        botRichard.setDiscardedCardDuringTheRound(discardedCard);
        botRichard.setCurrentChoiceOfCharactersCardsDuringTheRound(characterCard);
        assertFalse(botRichard.whatCharacterGotTookByGoodPlayer(players, CharacterCard.MERCHANT));
        discardedCard.remove(CharacterCard.MERCHANT);
        assertFalse(botRichard.whatCharacterGotTookByGoodPlayer(players, CharacterCard.MERCHANT));
        characterCard.remove(CharacterCard.MERCHANT);
        botRichard.setCurrentChoiceOfCharactersCardsDuringTheRound(characterCard);
        assertTrue(botRichard.whatCharacterGotTookByGoodPlayer(players, CharacterCard.MERCHANT));
        players.remove(0);
        Player player = new BotRandom();
        players.add(player);
        players.get(4).addCardToBoard(DistrictCard.SCHOOL_OF_MAGIC);
        players.get(4).addCardToBoard(DistrictCard.SMITHY);
        players.get(4).addCardToBoard(DistrictCard.CASTLE);
        players.get(4).addCardToBoard(DistrictCard.BATTLEFIELD);
        players.get(4).addCardToBoard(DistrictCard.DRAGON_GATE);
        players.get(4).addCardToBoard(DistrictCard.TAVERN);
        botRichard.setListCopyPlayers(players);
        botRichard.setListCopyPlayers(players);
        discardedCard.add(CharacterCard.MERCHANT);
        botRichard.setDiscardedCardDuringTheRound(discardedCard);
        botRichard.setCurrentChoiceOfCharactersCardsDuringTheRound(characterCard);
        assertFalse(botRichard.whatCharacterGotTookByGoodPlayer(players, CharacterCard.MERCHANT));
        discardedCard.remove(CharacterCard.MERCHANT);
        assertFalse(botRichard.whatCharacterGotTookByGoodPlayer(players, CharacterCard.MERCHANT));
        characterCard.remove(CharacterCard.MERCHANT);
        botRichard.setCurrentChoiceOfCharactersCardsDuringTheRound(characterCard);
        assertTrue(botRichard.whatCharacterGotTookByGoodPlayer(players, CharacterCard.MERCHANT));
    }
}