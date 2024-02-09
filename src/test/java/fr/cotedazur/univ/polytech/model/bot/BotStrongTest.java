package fr.cotedazur.univ.polytech.model.bot;

import fr.cotedazur.univ.polytech.logger.LamaLogger;
import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import fr.cotedazur.univ.polytech.model.card.DistrictCard;
import fr.cotedazur.univ.polytech.model.deck.Deck;
import fr.cotedazur.univ.polytech.model.deck.DeckFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BotStrongTest {

    Player botStrong;
    Deck<DistrictCard> districtDeck;

    Map<DispatchState, ArrayList<DistrictCard>> cardsThatThePlayerDontWantAndThatThePlayerWant = new EnumMap<>(DispatchState.class);


    @BeforeEach
    void setUp() {
        LamaLogger.mute();
        botStrong = new BotStrong();
        this.districtDeck = DeckFactory.createDistrictDeck();
        this.districtDeck.shuffle();
        cardsThatThePlayerDontWantAndThatThePlayerWant.put(DispatchState.CARDS_WANTED, new ArrayList<>());
        cardsThatThePlayerDontWantAndThatThePlayerWant.put(DispatchState.CARDS_NOT_WANTED, new ArrayList<>());
    }

    @Test
    void testPutADistrict() {
        botStrong.getHands().add(DistrictCard.PALACE);
        botStrong.getHands().add(DistrictCard.TEMPLE);
        botStrong.getHands().add(DistrictCard.GRAVEYARD);
        botStrong.getHands().add(DistrictCard.MARKET);
        botStrong.getHands().add(DistrictCard.LIBRARY);
        botStrong.getHands().add(DistrictCard.MANOR);
        botStrong.getHands().add(DistrictCard.CHURCH);
        botStrong.setGolds(20);
        botStrong.setPlayerRole(CharacterCard.ASSASSIN);

        //Should be Library because it's the purple card with the highest value
        botStrong.addCardToBoard(botStrong.choiceHowToPlayDuringTheRound());
        assertEquals(DistrictCard.LIBRARY, botStrong.getBoard().get(0));

        //Should be Palace because its value are now the highest of the botStrong hand
        botStrong.addCardToBoard(botStrong.choiceHowToPlayDuringTheRound());
        assertEquals(DistrictCard.GRAVEYARD, botStrong.getBoard().get(1));


        botStrong.addCardToBoard(DistrictCard.CHURCH);
        botStrong.addCardToBoard(DistrictCard.MARKET);
        botStrong.addCardToBoard(DistrictCard.PRISON);

        //Should be Graveyard because its value are now the highest of the botStrong hand and its purple
        botStrong.addCardToBoard(botStrong.choiceHowToPlayDuringTheRound());
        assertEquals(DistrictCard.PALACE, botStrong.getBoard().get(5));

        //Should be Temple because its match with the color of his player's character
        botStrong.setPlayerRole(CharacterCard.BISHOP);
        botStrong.addCardToBoard(botStrong.choiceHowToPlayDuringTheRound());
        assertEquals(DistrictCard.TEMPLE, botStrong.getBoard().get(6));

        //Should be Manor because its value are now the highest of the botStrong hand
        botStrong.addCardToBoard(botStrong.choiceHowToPlayDuringTheRound());
        assertEquals(DistrictCard.MANOR, botStrong.getBoard().get(7));

        botStrong.getHands().clear();
        botStrong.addCardToHand(DistrictCard.PALACE);
        assertNull(botStrong.choiceHowToPlayDuringTheRound());
    }

    @Test
    void testStartChoice() {
        //when there is not enough golds
        botStrong.getHands().add(DistrictCard.SMITHY);
        botStrong.setGolds(0);
        botStrong.setPlayerRole(CharacterCard.ASSASSIN);
        assertEquals(DispatchState.TWO_GOLDS, botStrong.startChoice());

        //Test when we can put a district
        botStrong.setGolds(20);
        assertEquals(DispatchState.TWO_GOLDS, botStrong.startChoice());

        //when there is already the color on the board
        botStrong.setGolds(20);
        botStrong.getBoard().add(DistrictCard.SCHOOL_OF_MAGIC);
        assertEquals(DispatchState.DRAW_CARD, botStrong.startChoice());

        //when hand is empty
        botStrong.setGolds(20);
        botStrong.getHands().clear();
        assertEquals(DispatchState.DRAW_CARD, botStrong.startChoice());
    }

    @Test
    void testChooseCharacter() {
        Deck<CharacterCard> characterDeck = DeckFactory.createCharacterDeck();
        botStrong.getHands().add(DistrictCard.SMITHY);
        botStrong.getBoard().add(DistrictCard.SCHOOL_OF_MAGIC);
        botStrong.setGolds(0);
        assertEquals(CharacterCard.MERCHANT, characterDeck.getCards().get(botStrong.chooseCharacter(characterDeck.getCards())));

        //Test when merchant is not in list of characters
        characterDeck.getCards().remove(CharacterCard.MERCHANT);
        assertEquals(CharacterCard.THIEF, characterDeck.getCards().get(botStrong.chooseCharacter(characterDeck.getCards())));

        //Test with magician
        botStrong.getHands().clear();
        botStrong.setGolds(20);
        assertEquals(CharacterCard.MAGICIAN, characterDeck.getCards().get(botStrong.chooseCharacter(characterDeck.getCards())));

        //Test with architect
        botStrong.getHands().add(DistrictCard.SMITHY);
        botStrong.getHands().add(DistrictCard.PALACE);
        botStrong.getHands().add(DistrictCard.TOWN_HALL);
        botStrong.getHands().add(DistrictCard.MARKET);
        assertEquals(CharacterCard.ARCHITECT, characterDeck.getCards().get(botStrong.chooseCharacter(characterDeck.getCards())));

        //Test colored cards
        botStrong.setGolds(3);
        botStrong.getBoard().add(DistrictCard.CATHEDRAL);
        assertEquals(CharacterCard.BISHOP, characterDeck.getCards().get(botStrong.chooseCharacter(characterDeck.getCards())));

    }

    @Test
    void testSelectWhoWillBeAffectedByThiefEffect(){
        java.util.List<Player> players = new ArrayList<>();
        java.util.List<CharacterCard> characterCard = new ArrayList<>(List.of(CharacterCard.values()));
        botStrong.setPlayerRole(CharacterCard.THIEF);
        assertEquals(CharacterCard.KING, botStrong.selectWhoWillBeAffectedByThiefEffect(players,characterCard ));
        botStrong.setPlayerRole(CharacterCard.ASSASSIN);
        assertNull(botStrong.selectWhoWillBeAffectedByThiefEffect(players, characterCard));
    }

    @Test
    void testSelectMagicianTarget(){
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Player player = new BotRandom();
            player.setGolds(4);
            players.add(player);
        }
        //Magician can't target himself
        botStrong.getBoard().add(DistrictCard.CATHEDRAL);
        botStrong.getBoard().add(DistrictCard.MARKET);
        botStrong.getBoard().add(DistrictCard.TAVERN);
        //player 1 has the most card
        players.get(0).getHands().add(DistrictCard.CASTLE);
        players.get(0).getHands().add(DistrictCard.CASTLE);
        players.get(1).getHands().add(DistrictCard.TAVERN);
        assertEquals(players.get(0), botStrong.selectMagicianTarget(players));
        //player 1 and player 0 has the most card so last player in players is returned
        players.get(1).getHands().add(DistrictCard.OBSERVATORY);
        assertEquals(players.get(1), botStrong.selectMagicianTarget(players));
    }

    @Test
    void testEquals() {
        BotStrong botStrong1 = new BotStrong();
        BotStrong botStrong2 = new BotStrong();
        assertFalse(botStrong1.equals(botStrong2));
        assertFalse(botStrong.equals(null));
        assertTrue(botStrong.equals(botStrong));
        assertFalse(botStrong.equals(botStrong1));
    }
}