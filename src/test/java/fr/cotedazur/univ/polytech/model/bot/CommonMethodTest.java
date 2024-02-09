package fr.cotedazur.univ.polytech.model.bot;

import fr.cotedazur.univ.polytech.controller.EffectController;
import fr.cotedazur.univ.polytech.logger.LamaLogger;
import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import fr.cotedazur.univ.polytech.model.card.Color;
import fr.cotedazur.univ.polytech.model.card.DistrictCard;
import fr.cotedazur.univ.polytech.model.deck.Deck;
import fr.cotedazur.univ.polytech.model.deck.DeckFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class CommonMethodTest extends BotStrong {
    CommonMethod botStrong;
    Deck<DistrictCard> districtDeck;

    Map<DispatchState, ArrayList<DistrictCard>> cardsThatThePlayerDontWantAndThatThePlayerWant = new EnumMap<>(DispatchState.class);

    @BeforeEach
    void setUp() {
        LamaLogger.mute();
        this.districtDeck = DeckFactory.createDistrictDeck();
        this.districtDeck.shuffle();
        cardsThatThePlayerDontWantAndThatThePlayerWant.put(DispatchState.CARDS_WANTED, new ArrayList<>());
        cardsThatThePlayerDontWantAndThatThePlayerWant.put(DispatchState.CARDS_NOT_WANTED, new ArrayList<>());
    }

    @Test
    void testMaxPrice(){
        botStrong = new BotStrong();
        assertNull(botStrong.maxPrice(botStrong.getBoard()));

        botStrong.addCardToBoard(DistrictCard.TEMPLE);
        botStrong.addCardToBoard(DistrictCard.SCHOOL_OF_MAGIC);
        assertEquals(DistrictCard.SCHOOL_OF_MAGIC, botStrong.maxPrice(botStrong.getBoard()));
    }

    @Test
    void testSelectWhoWillBeAffectedByAssassinEffect() {
        BotWeak botWeak = new BotWeak();
        botWeak.setPlayerRole(CharacterCard.ASSASSIN);

        List<Player> players = new ArrayList<>();
        List<CharacterCard> characterCards = Arrays.asList(CharacterCard.ASSASSIN, CharacterCard.THIEF, CharacterCard.MAGICIAN, CharacterCard.KING, CharacterCard.BISHOP, CharacterCard.MERCHANT, CharacterCard.ARCHITECT, CharacterCard.WARLORD);

        // Test when players size is less than 4
        players.add(new BotWeak());
        players.add(new BotWeak());
        players.add(new BotWeak());
        assertEquals(CharacterCard.KING, botWeak.selectWhoWillBeAffectedByAssassinEffect(players, characterCards));

        // Test when players size is less than 6
        players.add(new BotWeak());
        players.add(new BotWeak());
        assertEquals(CharacterCard.MERCHANT, botWeak.selectWhoWillBeAffectedByAssassinEffect(players, characterCards));

        // Test when players size is 6 or more
        players.add(new BotWeak());
        players.add(new BotWeak());
        assertEquals(CharacterCard.ARCHITECT, botWeak.selectWhoWillBeAffectedByAssassinEffect(players, characterCards));

        // Test when player role is not ASSASSIN
        botWeak.setPlayerRole(CharacterCard.KING);
        assertNull(botWeak.selectWhoWillBeAffectedByAssassinEffect(players, characterCards));
    }

    @Test
    void testGetCharacterIndexByColor() {
        BotWeak botWeak = new BotWeak();

        List<CharacterCard> characters = Arrays.asList(CharacterCard.ASSASSIN, CharacterCard.THIEF, CharacterCard.MAGICIAN, CharacterCard.KING, CharacterCard.BISHOP, CharacterCard.MERCHANT, CharacterCard.ARCHITECT, CharacterCard.WARLORD);

        // Test when color is YELLOW
        assertEquals(3, botWeak.getCharacterIndexByColor(characters, Color.YELLOW));

        // Test when color is GREEN
        assertEquals(5, botWeak.getCharacterIndexByColor(characters, Color.GREEN));

        // Test when color is BLUE
        assertEquals(4, botWeak.getCharacterIndexByColor(characters, Color.BLUE));

        // Test when color is not YELLOW, GREEN, or BLUE
        assertThrows(UnsupportedOperationException.class, () -> botWeak.getCharacterIndexByColor(characters, Color.RED));
    }

    @Test
    void testWhichMagicianEffect() {
        Richard botRichard = new Richard();
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
    void testWantToUseEffectForRichard(){
        Richard botRichard = new Richard();
        botRichard.setPlayerRole(CharacterCard.BISHOP);
        assertTrue(botRichard.wantToUseEffect(true));

        botRichard.getHands().add(DistrictCard.TEMPLE);
        botRichard.setGolds(3);
        assertFalse(botRichard.wantToUseEffect(true));

        botRichard.setPlayerRole(CharacterCard.ASSASSIN);
        assertTrue(botRichard.wantToUseEffect(true));

    }

    @Test
    void testChoosePlayerToDestroyInEmptyList() {
        BotWeak botWeak = new BotWeak();
        assertNull(botWeak.choosePlayerToDestroy(Collections.emptyList()));
    }

    @Test
    void testChooseDistrictToDestroy() {
        BotWeak botWeak = new BotWeak();
        BotWeak botWeak2 = new BotWeak();
        botWeak2.addCardToBoard(DistrictCard.CASTLE);
        botWeak2.addCardToBoard(DistrictCard.PALACE);
        botWeak2.addCardToBoard(DistrictCard.MANOR);
        assertNull(botWeak.chooseDistrictToDestroy(botWeak2, botWeak2.getBoard()));
    }

    @Test
    void testStartChoice(){
        botStrong = new BotStrong();
        botStrong.setGolds(5);
        botStrong.addCardToHand(DistrictCard.MARKET);
        botStrong.addCardToBoard(DistrictCard.TAVERN);
        assertEquals(DispatchState.TWO_GOLDS, botStrong.startChoice());
    }

    @Test
    void testChooseHandCardToDiscard(){
        botStrong = new BotStrong();
        botStrong.setGolds(5);
        botStrong.addCardToHand(DistrictCard.MARKET);
        botStrong.addCardToHand(DistrictCard.PALACE);
        assertEquals(DistrictCard.MARKET, botStrong.chooseHandCardToDiscard());

        //When the hand is empty
        botStrong.getHands().clear();
        assertNull(botStrong.chooseHandCardToDiscard());
    }

    @Test
    void testDrawCard(){
        Deck<DistrictCard> districtCardDeck = new Deck<>();
        districtCardDeck.getCards().add(DistrictCard.MARKET);
        districtCardDeck.getCards().add(DistrictCard.TEMPLE);
        districtCardDeck.getCards().add(DistrictCard.PALACE);
        botStrong = new BotStrong();
        botStrong.setGolds(5);
        botStrong.addCardToBoard(DistrictCard.LIBRARY);
        Map<DispatchState, ArrayList<DistrictCard>> cardsThatThePlayerDontWantAndThatThePlayerWant = new EnumMap<>(DispatchState.class);
        cardsThatThePlayerDontWantAndThatThePlayerWant.put(DispatchState.CARDS_WANTED, new ArrayList<>());
        cardsThatThePlayerDontWantAndThatThePlayerWant.put(DispatchState.CARDS_NOT_WANTED, new ArrayList<>());



        botStrong.drawCard(cardsThatThePlayerDontWantAndThatThePlayerWant, districtCardDeck.draw(), districtCardDeck.draw(), districtCardDeck.draw());

        assertEquals(DistrictCard.PALACE, cardsThatThePlayerDontWantAndThatThePlayerWant.get(DispatchState.CARDS_WANTED).get(0));
        assertEquals(DistrictCard.MARKET, cardsThatThePlayerDontWantAndThatThePlayerWant.get(DispatchState.CARDS_WANTED).get(1));
        assertEquals(DistrictCard.TEMPLE, cardsThatThePlayerDontWantAndThatThePlayerWant.get(DispatchState.CARDS_NOT_WANTED).get(0));
    }

    @Test
    void testWhichWarlordEffectAndChooseToDestroy(){
        botStrong = new BotStrong();
        Player player = new BotRandom();
        ArrayList<Player> players = new ArrayList<>();
        players.add(player);
        assertEquals(DispatchState.EARNDISTRICT_WARLORD, botStrong.whichWarlordEffect(players));
        assertNull(botStrong.choosePlayerToDestroy(players));

        player.addCardToBoard(DistrictCard.TAVERN);
        assertEquals(DispatchState.DESTROY, botStrong.whichWarlordEffect(players));
        assertEquals(player, botStrong.choosePlayerToDestroy(players));

        player.getBoard().clear();
        player.addCardToBoard(DistrictCard.SCHOOL_OF_MAGIC);
        assertEquals(DispatchState.EARNDISTRICT_WARLORD, botStrong.whichWarlordEffect(players));
        assertNull(botStrong.choosePlayerToDestroy(players));
        assertNull(botStrong.chooseDistrictToDestroy(player, player.getBoard()));

    }

    @Test
    void testWantToUseLaboratoryEffect(){
        botStrong = new BotStrong();
        botStrong.addCardToHand(DistrictCard.MARKET);
        botStrong.addCardToHand(DistrictCard.PALACE);
        assertTrue(botStrong.wantToUseLaboratoryEffect());

        botStrong.setGolds(5);
        assertFalse(botStrong.wantToUseLaboratoryEffect());
    }

    @Test
    void testChooseCardsToChange(){
        botStrong = new BotStrong();
        botStrong.addCardToHand(DistrictCard.MARKET);
        botStrong.addCardToHand(DistrictCard.PALACE);
        assertEquals(2,botStrong.chooseCardsToChange().size());

        botStrong.setGolds(5);
        assertEquals(0,botStrong.chooseCardsToChange().size());
    }

    @Test
    void testChooseColorForHauntedCity(){
        botStrong = new BotStrong();
        botStrong.addCardToBoard(DistrictCard.MARKET);
        botStrong.addCardToBoard(DistrictCard.PALACE);
        botStrong.addCardToBoard(DistrictCard.TEMPLE);
        botStrong.addCardToBoard(DistrictCard.UNIVERSITY);
        assertEquals(Color.RED, botStrong.chooseColorForHauntedCity());

        botStrong.addCardToBoard(DistrictCard.WATCHTOWER);
        assertEquals(Color.PURPLE, botStrong.chooseColorForHauntedCity());


    }

    @Test
    void testChooseColorForSchoolOfMagic(){
        botStrong = new BotStrong();
        assertEquals(Color.PURPLE, botStrong.chooseColorForSchoolOfMagic());

        //Set A Character
        botStrong.setPlayerRole(CharacterCard.BISHOP);
        assertEquals(Color.BLUE, botStrong.chooseColorForSchoolOfMagic());
    }

}
