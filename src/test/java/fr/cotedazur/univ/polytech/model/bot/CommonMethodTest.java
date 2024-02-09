package fr.cotedazur.univ.polytech.model.bot;

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

public class CommonMethodTest extends BotStrong {
    Player botStrong;
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
}
