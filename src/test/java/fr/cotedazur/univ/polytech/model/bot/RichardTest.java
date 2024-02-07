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
        botRichard.getHands().add(DistrictCard.KEEP);
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
}