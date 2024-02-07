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
        //Take the King
        List<CharacterCard> characterCard = new ArrayList<>(List.of(CharacterCard.values()));
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Player player = new BotRandom();
            player.setGolds(4);
            players.add(player);
            player.addCardToHand(DistrictCard.KEEP);
        }

        players.add(botRichard);
        botRichard.addCardToHand(DistrictCard.PALACE);
        assertEquals(CharacterCard.KING, characterCard.get(botRichard.chooseCharacter(characterCard)));

        botRichard.setGolds(4);
        botRichard.getHands().clear();
        botRichard.setListCopyPlayers(players);

        if (botRichard.isFirst(players)) {
            assertEquals(CharacterCard.ASSASSIN, characterCard.get(botRichard.chooseCharacter(characterCard)));
        } else {
            assertEquals(CharacterCard.ARCHITECT, characterCard.get(botRichard.chooseCharacter(characterCard)));
        }
        botRichard.setDiscardedCardDuringTheRound(Arrays.asList(CharacterCard.ARCHITECT, CharacterCard.ASSASSIN));

        //Take the Merchant
        botRichard.setGolds(1);
        botRichard.getHands().clear();
        botRichard.setListCopyPlayers(players);
        assertEquals(CharacterCard.MERCHANT, characterCard.get(botRichard.chooseCharacter(characterCard)));
        botRichard.setDiscardedCardDuringTheRound(Arrays.asList(CharacterCard.ARCHITECT, CharacterCard.ASSASSIN));
        //Take the Magician
        botRichard.setGolds(4);
        botRichard.getHands().clear();
        botRichard.setListCopyPlayers(players);
        assertEquals(CharacterCard.MAGICIAN, characterCard.get(botRichard.chooseCharacter(characterCard)));

        //Don't take the magician when all have empty hands
        for(Player player : players){
            player.getHands().clear();
        }
        botRichard.getHands().clear();
        botRichard.setGolds(2);
        botRichard.setDiscardedCardDuringTheRound(Arrays.asList(CharacterCard.ARCHITECT, CharacterCard.ASSASSIN));
        assertEquals(CharacterCard.THIEF, characterCard.get(botRichard.chooseCharacter(characterCard)));

        //Take Thief
        botRichard.addCardToHand(DistrictCard.KEEP);

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


        //take Assassin to kill magician
        botRichard.getBoard().clear();
        botRichard.getHands().clear();
        botRichard.setGolds(4);
        botRichard.getHands().add(DistrictCard.KEEP);
        botRichard.getHands().add(DistrictCard.DRAGON_GATE);
        botRichard.getHands().add(DistrictCard.LABORATORY);
        botRichard.getHands().add(DistrictCard.LIBRARY);
        botRichard.getHands().add(DistrictCard.SCHOOL_OF_MAGIC);
        assertEquals(CharacterCard.ASSASSIN, characterCard.get(botRichard.chooseCharacter(characterCard)));
        assertEquals(CharacterCard.MAGICIAN, botRichard.getTarget());

        //try to take assassin with not enough cards in hands
        when(random.nextInt(anyInt())).thenReturn(3);
        botRichard.getHands().remove(DistrictCard.KEEP);
        assertEquals(CharacterCard.KING, characterCard.get(botRichard.chooseCharacter(characterCard)));

        //try to take assassin but every player has at least 1 card
        botRichard.getHands().add(DistrictCard.GRAVEYARD);
        for (Player player : players) {
            player.addCardToHand(DistrictCard.KEEP);
        }
        assertEquals(CharacterCard.KING, characterCard.get(botRichard.chooseCharacter(characterCard)));

        //Take the Warlord
        botRichard.getHands().add(DistrictCard.BATTLEFIELD);
        botRichard.setListCopyPlayers(players);
        assertEquals(CharacterCard.WARLORD, characterCard.get(botRichard.chooseCharacter(characterCard)));

    }

    @Test
    void shouldReturnAssassinWhenPlayerRoleIsNotArchitectAndAssassinIsAvailable() {
        List<CharacterCard> characterCard = new ArrayList<>(List.of(CharacterCard.values()));
        List<Player> players = new ArrayList<>();
        Player player = new BotRandom();
        player.setPlayerRole(CharacterCard.KING);
        players.add(player);
        players.add(botRichard);
        botRichard.setListCopyPlayers(players);
        botRichard.setDiscardedCardDuringTheRound(new ArrayList<>());
        assertEquals(CharacterCard.ASSASSIN, characterCard.get(botRichard.chooseCharacter(characterCard)));
    }

    @Test
    void shouldReturnArchitectWhenPlayerHasMoreGoldsAndArchitectIsAvailable() {
        List<CharacterCard> characterCard = new ArrayList<>(List.of(CharacterCard.values()));
        List<Player> players = new ArrayList<>();
        Player player = new BotRandom();
        player.setPlayerRole(CharacterCard.KING);
        players.add(botRichard);
        players.add(player);
        botRichard.setListCopyPlayers(players);
        botRichard.setGolds(5);
        assertEquals(CharacterCard.ARCHITECT, characterCard.get(botRichard.chooseCharacter(characterCard)));
    }

    @Test
    void shouldReturnRandomWhenPlayerRoleIsArchitectAndAssassinIsNotAvailable() {
        List<CharacterCard> characterCard = new ArrayList<>(List.of(CharacterCard.values()));
        characterCard.remove(CharacterCard.ASSASSIN);
        List<Player> players = new ArrayList<>();
        Player player = new BotRandom();
        player.setPlayerRole(CharacterCard.ARCHITECT);
        players.add(player);
        players.add(botRichard);
        botRichard.setListCopyPlayers(players);
        botRichard.setDiscardedCardDuringTheRound(new ArrayList<>());
        botRichard.setGolds(5);
        when(random.nextInt(characterCard.size())).thenReturn(0);
        assertEquals(CharacterCard.THIEF, characterCard.get(botRichard.chooseCharacter(characterCard)));
    }

    @Test
    void testBeforeLastRound(){
        List<CharacterCard> characterCard = new ArrayList<>(List.of(CharacterCard.values()));
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Player player = new BotRandom();
            player.setGolds(4);
            players.add(player);
            player.addCardToHand(DistrictCard.PALACE);
        }

        players.add(botRichard);
        botRichard.setListCopyPlayers(players);
        botRichard.getBoard().add(DistrictCard.FORTRESS);
        botRichard.getBoard().add(DistrictCard.DRAGON_GATE);
        botRichard.getBoard().add(DistrictCard.PRISON);
        botRichard.getBoard().add(DistrictCard.DOCKS);
        botRichard.getBoard().add(DistrictCard.SCHOOL_OF_MAGIC);
        botRichard.getBoard().add(DistrictCard.HAUNTED_CITY);

        //Choose Character before last round (6/8)
        assertEquals(CharacterCard.MERCHANT, characterCard.get(botRichard.chooseCharacter(characterCard)));
        players.get(0).getBoard().add(DistrictCard.FORTRESS);
        players.get(0).getBoard().add(DistrictCard.DRAGON_GATE);
        players.get(0).getBoard().add(DistrictCard.PRISON);
        players.get(0).getBoard().add(DistrictCard.DOCKS);
        players.get(0).getBoard().add(DistrictCard.SCHOOL_OF_MAGIC);
        players.get(0).getBoard().add(DistrictCard.HAUNTED_CITY);


        //When the cards contain the king and is not discarded
        assertEquals(CharacterCard.KING, characterCard.get(botRichard.chooseCharacter(characterCard)));

        //When the king is not contained in the card and the player that is set to win is before Richard
        characterCard.remove(CharacterCard.KING);
        assertEquals(CharacterCard.ASSASSIN, characterCard.get(botRichard.chooseCharacter(characterCard)));
        assertEquals(CharacterCard.KING, botRichard.getTarget());

        //When the player that is set to win is not before Richard
        players.add(players.remove(0));
        assertEquals(CharacterCard.ASSASSIN, characterCard.get(botRichard.chooseCharacter(characterCard)));
        assertNull(botRichard.getTarget());

        //when king is discarded
        botRichard.setDiscardedCardDuringTheRound(Collections.singletonList(CharacterCard.KING));
        assertEquals(CharacterCard.ASSASSIN, characterCard.get(botRichard.chooseCharacter(characterCard)));

        //when assassin is not in the deck
        characterCard.remove(CharacterCard.ASSASSIN);
        assertEquals(CharacterCard.WARLORD, characterCard.get(botRichard.chooseCharacter(characterCard)));
        //We verify that there is not a player targeted by the warlord because the player that is set to win is not the 1st player
        assertNull(botRichard.getTargetedPlayerWhenIsLastBefore());
        //When then player that is set to win is the first player
        Player player = players.remove(players.size() - 1);
        players.add(0, player);
        botRichard.setDiscardedCardDuringTheRound(new ArrayList<>());
        assertEquals(CharacterCard.WARLORD, characterCard.get(botRichard.chooseCharacter(characterCard)));
        //We verify that there is not a player targeted by the warlord because the player that is set to win is not the 1st player
        assertEquals(player,botRichard.getTargetedPlayerWhenIsLastBefore());

        //when the warlord is not in the choice
        characterCard.remove(CharacterCard.WARLORD);
        assertEquals(CharacterCard.BISHOP, characterCard.get(botRichard.chooseCharacter(characterCard)));

        //when the bishop is not in the choice
        characterCard.remove(CharacterCard.BISHOP);
        assertEquals(CharacterCard.MAGICIAN, characterCard.get(botRichard.chooseCharacter(characterCard)));
    }

    @Test
    void testWhenAPlayerThatIsSetToWinTakeTheKing(){
        List<CharacterCard> characterCard = new ArrayList<>(List.of(CharacterCard.values()));
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Player player = new BotRandom();
            player.setGolds(4);
            players.add(player);
            player.addCardToHand(DistrictCard.PALACE);
        }
        players.add(botRichard);
        players.get(0).getBoard().add(DistrictCard.FORTRESS);
        players.get(0).getBoard().add(DistrictCard.DRAGON_GATE);
        players.get(0).getBoard().add(DistrictCard.PRISON);
        players.get(0).getBoard().add(DistrictCard.DOCKS);
        players.get(0).getBoard().add(DistrictCard.SCHOOL_OF_MAGIC);
        players.get(0).getBoard().add(DistrictCard.BATTLEFIELD);
        characterCard.remove(CharacterCard.KING);

        botRichard.setListCopyPlayers(players);
        botRichard.setPlayerRole(characterCard.get(botRichard.chooseCharacter(characterCard)));
        assertEquals(CharacterCard.ASSASSIN, botRichard.getPlayerRole());
        //When Richard is the assassin, and we know that the player that can win take the king
        assertEquals(CharacterCard.KING, botRichard.selectWhoWillBeAffectedByAssassinEffect(players, characterCard));

        //Test when there is not the assassin in the choice
        characterCard.remove(CharacterCard.ASSASSIN);
        botRichard.setPlayerRole(characterCard.get(botRichard.chooseCharacter(characterCard)));
        assertEquals(CharacterCard.WARLORD, botRichard.getPlayerRole());
        assertEquals(players.get(0), botRichard.choosePlayerToDestroy(players));
        assertEquals(DistrictCard.PRISON, botRichard.chooseDistrictToDestroy(players.get(0),players.get(0).getBoard()));

        //Test when the player that is set to win is not the first
        players.add(players.remove(0));
        assertNotEquals(CharacterCard.KING, botRichard.selectWhoWillBeAffectedByAssassinEffect(players, characterCard));
    }

    @Test
    void testWhenAPlayerThatIsSetToWinDontTakeTheKing(){
        List<CharacterCard> characterCard = new ArrayList<>(List.of(CharacterCard.values()));
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Player player = new BotRandom();
            player.setGolds(4);
            players.add(player);
            player.addCardToHand(DistrictCard.PALACE);
        }
        players.add(botRichard);
        players.get(0).getBoard().add(DistrictCard.FORTRESS);
        players.get(0).getBoard().add(DistrictCard.DRAGON_GATE);
        players.get(0).getBoard().add(DistrictCard.PRISON);
        players.get(0).getBoard().add(DistrictCard.DOCKS);
        players.get(0).getBoard().add(DistrictCard.SCHOOL_OF_MAGIC);
        players.get(0).addCardToHand(DistrictCard.MARKET);
        characterCard.remove(CharacterCard.KING);
        characterCard.remove(CharacterCard.ASSASSIN);
        botRichard.setListCopyPlayers(players);
        botRichard.setPlayersThatIsSetToWin(Collections.singletonList(players.get(0)));
        botRichard.setCurrentChoiceOfCharactersCardsDuringTheRound(characterCard);
        botRichard.setPlayerRole(CharacterCard.MAGICIAN);
        botRichard.setBeforeLastRound(true);


        players.get(0).setPlayerRole(CharacterCard.ASSASSIN);
        botRichard.setRoleKilledByAssassin(CharacterCard.WARLORD);
        //When Richard is the magician, and we know that the player that is set to win take the assassin and kill the warlord
        assertEquals(players.get(0), botRichard.selectMagicianTarget(players));

        players.get(0).setPlayerRole(CharacterCard.MERCHANT);
        //When Richard is the magician, and we know that the player that is set to win take the warlord or bishop
        characterCard.remove(CharacterCard.BISHOP);
        assertEquals(players.get(0), botRichard.selectMagicianTarget(players));

        //When Richard is the assassin, and we know that the player that is set to win take the warlord or bishop
        botRichard.setPlayerRole(CharacterCard.ASSASSIN);
        assertEquals(CharacterCard.WARLORD, botRichard.selectWhoWillBeAffectedByAssassinEffect(players, characterCard));

        //When Richard is the Thief, and we know that the player that is set to win take the warlord or bishop
        botRichard.setPlayerRole(CharacterCard.THIEF);
        assertEquals(CharacterCard.WARLORD, botRichard.selectWhoWillBeAffectedByThiefEffect(players, characterCard));

        characterCard.remove(CharacterCard.WARLORD);
        characterCard.add(CharacterCard.BISHOP);
        //When Richard is the assassin, and we know that the player that is set to win take the warlord or bishop
        botRichard.setPlayerRole(CharacterCard.ASSASSIN);
        assertEquals(CharacterCard.BISHOP, botRichard.selectWhoWillBeAffectedByAssassinEffect(players, characterCard));

        //When Richard is the Thief, and we know that the player that is set to win take the warlord or bishop
        botRichard.setPlayerRole(CharacterCard.THIEF);
        assertEquals(CharacterCard.BISHOP, botRichard.selectWhoWillBeAffectedByThiefEffect(players, characterCard));

        //when this is not the before last round
        botRichard.setBeforeLastRound(false);
        botRichard.setPlayerRole(CharacterCard.ASSASSIN);
        assertEquals(CharacterCard.THIEF, botRichard.selectWhoWillBeAffectedByAssassinEffect(players, characterCard));

        botRichard.setPlayerRole(CharacterCard.THIEF);
        assertNotEquals(CharacterCard.WARLORD, botRichard.selectWhoWillBeAffectedByThiefEffect(players, characterCard));
    }

    @Test
    void testOnlyOneWith1GoldDistrict() {
        //Richard is the only one with a 1 gold district
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Player player = new BotRandom();
            player.getBoard().add(DistrictCard.CHURCH);
            players.add(player);
        }
        botRichard.getBoard().add(DistrictCard.TEMPLE);
        players.add(botRichard);
        assertTrue(botRichard.onlyOneWith1GoldDistrict(players));
        //no-one has 1 gold district
        botRichard.getBoard().remove(DistrictCard.TEMPLE);
        assertFalse(botRichard.onlyOneWith1GoldDistrict(players));

        //everyone has 1 gold district
        botRichard.getBoard().add(DistrictCard.TEMPLE);
        for (Player player : players) {
            player.getBoard().add(DistrictCard.TAVERN);
        }
        assertFalse(botRichard.onlyOneWith1GoldDistrict(players));
    }

    @Test
    void shouldReturnAssassinWhenRichardIsFirstOrSecondAndAssassinIsAvailable() {
        List<CharacterCard> characterCard = new ArrayList<>(List.of(CharacterCard.values()));
        List<Player> players = new ArrayList<>();
        players.add(botRichard);
        botRichard.setListCopyPlayers(players);
        assertEquals(CharacterCard.ASSASSIN, characterCard.get(botRichard.chooseCharacter(characterCard)));
    }




    @Test
    void shouldReturnCardFromRichardComboWhenRichardIsThirdPlayer() {
        List<CharacterCard> characterCard = new ArrayList<>(List.of(CharacterCard.values()));
        List<Player> players = new ArrayList<>();
        Player player1 = new BotRandom();
        Player player2 = new BotRandom();
        players.add(player1);
        players.add(player2);
        players.add(botRichard);
        botRichard.setListCopyPlayers(players);
        assertNotNull(characterCard.get(botRichard.chooseCharacter(characterCard)));
    }

    @Test
    void shouldReturnAssassinWhenRichardIsThirdAndBishopNotAvailable() {
        List<CharacterCard> characterCard = new ArrayList<>(List.of(CharacterCard.values()));
        characterCard.remove(CharacterCard.BISHOP);
        List<Player> players = new ArrayList<>();
        Player player1 = new BotRandom();
        Player player2 = new BotRandom();
        players.add(player1);
        players.add(player2);
        players.add(botRichard);
        botRichard.setListCopyPlayers(players);
        assertEquals(CharacterCard.ASSASSIN, characterCard.get(botRichard.chooseCharacter(characterCard)));
    }

    @Test
    void testIsFirst() {
        List<Player> players = new ArrayList<>();
        Player player = new BotRandom();
        player.getBoard().add(DistrictCard.SCHOOL_OF_MAGIC);
        players.add(player);

        //Richard has fewer districts than the bot
        players.add(botRichard);
        assertFalse(botRichard.isFirst(players));
        //Richard has the same amount of district than the bot
        botRichard.getBoard().add(DistrictCard.FORTRESS);
        assertTrue(botRichard.isFirst(players));
        //Richard has more districts than the bot
        botRichard.getBoard().add(DistrictCard.CHURCH);
        assertTrue(botRichard.isFirst(players));
    }
    @Test
    void shouldNotReturnAssassinIndexWhenPositionIsOneAndBishopAndWarlordAreNotAvailableAndAssassinIsNot() {
        List<CharacterCard> cards = new ArrayList<>(List.of(CharacterCard.values()));
        cards.remove(CharacterCard.BISHOP);
        cards.remove(CharacterCard.WARLORD);
        cards.remove(CharacterCard.ASSASSIN);
        int position = 1;
        assertNotEquals(cards.indexOf(CharacterCard.ASSASSIN), botRichard.chooseCharacter(cards));
    }

    @Test
void shouldReturnWarlordIndexWhenRichardIsFirstAndAllCardsAreAvailable() {
    List<CharacterCard> cards = new ArrayList<>(List.of(CharacterCard.values()));
    List<Player> playersOrdered = new ArrayList<>();
    playersOrdered.add(botRichard);
    int position = 2;
    assertEquals(cards.indexOf(CharacterCard.WARLORD), botRichard.RichardCombo(cards, playersOrdered, position));
}

@Test
void shouldReturnAssassinIndexWhenRichardIsSecondAndAllCardsAreAvailable() {
    List<CharacterCard> cards = new ArrayList<>(List.of(CharacterCard.values()));
    List<Player> playersOrdered = new ArrayList<>();
    playersOrdered.add(new BotRandom());
    playersOrdered.add(botRichard);
    int position = 2;
    assertEquals(cards.indexOf(CharacterCard.ASSASSIN), botRichard.RichardCombo(cards, playersOrdered, position));
}

@Test
void shouldReturnAssassinIndexWhenRichardIsFirstAndBishopIsNotAvailable() {
    List<CharacterCard> cards = new ArrayList<>(List.of(CharacterCard.values()));
    cards.remove(CharacterCard.BISHOP);
    List<Player> playersOrdered = new ArrayList<>();
    botRichard.getHands().add(DistrictCard.TEMPLE);
    BotRandom botRandom = new BotRandom();
    botRandom.getHands().add(DistrictCard.DRAGON_GATE);
    botRandom.getHands().add(DistrictCard.MARKET);
    playersOrdered.add(botRichard);
    playersOrdered.add(new BotRandom());
    int position = 2;
    assertEquals(cards.indexOf(CharacterCard.ASSASSIN), botRichard.RichardCombo(cards, playersOrdered, position));
}

@Test
void shouldReturnMagicianIndexWhenRichardIsSecondAndFirstPlayerHasLessCards() {
    List<CharacterCard> cards = new ArrayList<>(List.of(CharacterCard.values()));
    cards.remove(CharacterCard.BISHOP);
    List<Player> playersOrdered = new ArrayList<>();
    Player firstPlayer = new BotRandom();
    firstPlayer.getHands().clear();
    playersOrdered.add(firstPlayer);
    playersOrdered.add(botRichard);
    botRichard.getHands().add(DistrictCard.TEMPLE);
    int position = 2;
    assertEquals(cards.indexOf(CharacterCard.MAGICIAN), botRichard.RichardCombo(cards, playersOrdered, position));
}

@Test
void shouldReturnWarlordIndexWhenRichardIsFirstAndAssassinIsNotAvailable() {
    List<CharacterCard> cards = new ArrayList<>(List.of(CharacterCard.values()));
    cards.remove(CharacterCard.ASSASSIN);
    List<Player> playersOrdered = new ArrayList<>();
    playersOrdered.add(botRichard);
    int position = 2;
    assertEquals(cards.indexOf(CharacterCard.WARLORD), botRichard.RichardCombo(cards, playersOrdered, position));
}

@Test
void shouldReturnBishopIndexWhenRichardIsSecondAndAssassinIsNotAvailable() {
    List<CharacterCard> cards = new ArrayList<>(List.of(CharacterCard.values()));
    cards.remove(CharacterCard.ASSASSIN);
    List<Player> playersOrdered = new ArrayList<>();
    playersOrdered.add(new BotRandom());
    playersOrdered.add(botRichard);
    int position = 2;
    assertEquals(cards.indexOf(CharacterCard.BISHOP), botRichard.RichardCombo(cards, playersOrdered, position));
}

@Test
void shouldReturnNullWhenRichardIsThirdAndAllCardsAreNotAvailable() {
    List<CharacterCard> cards = new ArrayList<>(List.of(CharacterCard.values()));
    cards.remove(CharacterCard.ASSASSIN);
    cards.remove(CharacterCard.WARLORD);
    cards.remove(CharacterCard.BISHOP);
    List<Player> playersOrdered = new ArrayList<>();
    playersOrdered.add(new BotRandom());
    playersOrdered.add(new BotRandom());
    playersOrdered.add(botRichard);
    int position = 2;
    assertNull(botRichard.RichardCombo(cards, playersOrdered, position));
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
        players.get(0).getBoard().add(DistrictCard.SCHOOL_OF_MAGIC);
        players.get(0).getBoard().add(DistrictCard.SMITHY);
        players.get(0).getBoard().add(DistrictCard.CASTLE);
        players.get(0).getBoard().add(DistrictCard.BATTLEFIELD);
        players.get(0).getBoard().add(DistrictCard.DRAGON_GATE);
        players.get(0).getBoard().add(DistrictCard.TAVERN);
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
        players.get(4).getBoard().add(DistrictCard.SCHOOL_OF_MAGIC);
        players.get(4).getBoard().add(DistrictCard.SMITHY);
        players.get(4).getBoard().add(DistrictCard.CASTLE);
        players.get(4).getBoard().add(DistrictCard.BATTLEFIELD);
        players.get(4).getBoard().add(DistrictCard.DRAGON_GATE);
        players.get(4).getBoard().add(DistrictCard.TAVERN);
        characterCard.add(CharacterCard.MERCHANT);
        botRichard.setListCopyPlayers(players);
        botRichard.setListCopyPlayers(players);
        discardedCard.add(CharacterCard.MERCHANT);
        botRichard.setDiscardedCardDuringTheRound(discardedCard);
        botRichard.setCurrentChoiceOfCharactersCardsDuringTheRound(characterCard);
        assertFalse(botRichard.whatCharacterGotTookByGoodPlayer(players, CharacterCard.MERCHANT));
        discardedCard.remove(CharacterCard.MERCHANT);
        botRichard.setCurrentChoiceOfCharactersCardsDuringTheRound(characterCard);
        assertTrue(botRichard.whatCharacterGotTookByGoodPlayer(players, CharacterCard.MERCHANT));
        characterCard.remove(CharacterCard.MERCHANT);
        botRichard.setCurrentChoiceOfCharactersCardsDuringTheRound(characterCard);
        assertFalse(botRichard.whatCharacterGotTookByGoodPlayer(players, CharacterCard.MERCHANT));
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

    @Test
    void testWhichMagicianEffect() {
        List<Player> players = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            Player player = new BotRandom();
            List<DistrictCard> cards = new ArrayList<>();
            cards.add(DistrictCard.DRAGON_GATE);
            cards.add(DistrictCard.TEMPLE);
            cards.add(DistrictCard.TAVERN);
            player.setHands(cards);
            players.add(player);
        }
        assertEquals(DispatchState.EXCHANGE_PLAYER, botRichard.whichMagicianEffect(players));


        //Test when the bot richard has the same number of cards as the other
        for(Player player : players){
            player.getHands().clear();
        }
        assertEquals(DispatchState.EXCHANGE_DECK, botRichard.whichMagicianEffect(players));
    }

    @Test
    void testSelectMagicianTarget() {
        List<Player> players = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            Player player = new BotRandom();
            List<DistrictCard> cards = new ArrayList<>();
            cards.add(DistrictCard.DRAGON_GATE);
            cards.add(DistrictCard.TEMPLE);
            if(i==2){
                player.setGolds(20);
                player.getBoard().add(DistrictCard.MARKET);
            }
            if(i == 3){
                cards.add(DistrictCard.TAVERN);
            }
            player.setHands(cards);
            players.add(player);
        }
        Player playerThatShouldBeTargeted = players.get(3);

        assertEquals(playerThatShouldBeTargeted, botRichard.selectMagicianTarget(players));

        //Test when 2 players as the same number of cards but one as one card on the board
        for(Player player : players){
            if(player.getBoard().isEmpty()){
                player.getHands().add(DistrictCard.TRADING_POST);
            }
        }
        playerThatShouldBeTargeted = players.get(3);
        assertEquals(playerThatShouldBeTargeted, botRichard.selectMagicianTarget(players));

    }

    @Test
     public void testNumberOfDistrictInOrder(){
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Player player = new BotRandom();
            player.getBoard().add(DistrictCard.CHURCH);
            players.add(player);
        }
        //Richard is first
        botRichard.getBoard().add(DistrictCard.FORTRESS);
        botRichard.getBoard().add(DistrictCard.SMITHY);
        players.add(botRichard);
        assertEquals(botRichard.numberOfDistrictInOrder(players).get(0), botRichard);
        //A bot is first
        players.get(0).getBoard().add(DistrictCard.FORTRESS);
        players.get(0).getBoard().add(DistrictCard.SMITHY);
        assertEquals(botRichard.numberOfDistrictInOrder(players).get(0), players.get(0));
    }

    @Test
    void testChoosePlayerToDestroyInEmptyList() {
        assertNull(botRichard.choosePlayerToDestroy(Collections.emptyList()));
    }

    @Test
    void testChoosePlayerToDestroy() {
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Player player = new BotRandom();
            player.getBoard().add(DistrictCard.CHURCH);
            players.add(player);
        }
        //no has a card to destroy
        players.add(botRichard);
        assertNull(botRichard.choosePlayerToDestroy(players));
        //only Richard has a card to destroy
        botRichard.getBoard().add(DistrictCard.CHURCH);
        botRichard.getBoard().add(DistrictCard.TAVERN);
        assertNull(botRichard.choosePlayerToDestroy(players));
        //someone has a card to destroy
        players.get(0).getBoard().add(DistrictCard.TAVERN);
        assertEquals(players.get(0), botRichard.choosePlayerToDestroy(players));
    }
    @Test
    void testChooseDistrictToDestroy() {
        Richard botRichard = new Richard();
        botRichard.addCardToBoard(DistrictCard.CASTLE);
        botRichard.addCardToBoard(DistrictCard.PALACE);
        botRichard.addCardToBoard(DistrictCard.MANOR);
        assertNull(botRichard.chooseDistrictToDestroy(botRichard, botRichard.getBoard()));
    }

    @Test
    void testFirstHas1GoldDistrict() {
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Player player = new BotRandom();
            player.getBoard().add(DistrictCard.CHURCH);
            players.add(player);
        }
        players.get(0).getBoard().add(DistrictCard.TAVERN);
        players.add(botRichard);
        assertTrue(botRichard.firstHas1GoldDistrict(players));
        players.get(0).getBoard().remove(DistrictCard.TAVERN);
        botRichard.getBoard().add(DistrictCard.TAVERN);
        assertFalse(botRichard.firstHas1GoldDistrict(players));
    }

    @Test
    void testWantToUseEffectForRichard(){
        botRichard.setPlayerRole(CharacterCard.BISHOP);
        assertTrue(botRichard.wantToUseEffect(true));

        botRichard.getHands().add(DistrictCard.TEMPLE);
        botRichard.setGolds(3);
        assertFalse(botRichard.wantToUseEffect(true));

        botRichard.setPlayerRole(CharacterCard.ASSASSIN);
        assertTrue(botRichard.wantToUseEffect(true));

    }

    @Test
    void testIsBeforeLastRound() {
        List<Player> players = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            Player player = new BotRandom();
            List<DistrictCard> cards = new ArrayList<>();
            cards.add(DistrictCard.DRAGON_GATE);
            cards.add(DistrictCard.TEMPLE);
            cards.add(DistrictCard.TAVERN);
            player.setHands(cards);
            players.add(player);
        }
        List<DistrictCard> cards = new ArrayList<>();
        cards.add(DistrictCard.DRAGON_GATE);
        cards.add(DistrictCard.TEMPLE);
        cards.add(DistrictCard.TAVERN);
        cards.add(DistrictCard.MARKET);
        cards.add(DistrictCard.SCHOOL_OF_MAGIC);
        cards.add(DistrictCard.CASTLE);
        botRichard.setBoard(cards);
        players.add(botRichard);
        botRichard.setListCopyPlayers(players);
        assertFalse(botRichard.ifIsBeforeLastRound());

        //When someone has 6 cards
        Player player2 = players.get(0);
        player2.getBoard().addAll(cards);
        assertTrue(botRichard.ifIsBeforeLastRound());
    }
}