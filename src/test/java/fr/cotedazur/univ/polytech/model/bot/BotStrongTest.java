package fr.cotedazur.univ.polytech.model.bot;

import fr.cotedazur.univ.polytech.logger.LamaLogger;
import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import fr.cotedazur.univ.polytech.model.card.DistrictCard;
import fr.cotedazur.univ.polytech.model.deck.Deck;
import fr.cotedazur.univ.polytech.model.deck.DeckFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.EnumMap;
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
        botStrong.setGolds(20);
        botStrong.setPlayerRole(CharacterCard.ASSASSIN);

        //Should be Library because it's the purple card with the highest value
        botStrong.addCardToBoard(botStrong.choiceHowToPlayDuringTheRound());
        assertEquals(DistrictCard.LIBRARY, botStrong.getBoard().get(0));

        //Should be Palace because its value are now the highest of the botStrong hand
        botStrong.addCardToBoard(botStrong.choiceHowToPlayDuringTheRound());
        assertEquals(DistrictCard.GRAVEYARD, botStrong.getBoard().get(1));


        botStrong.addCardToBoard(DistrictCard.MARKET);
        botStrong.addCardToBoard(DistrictCard.TEMPLE);
        botStrong.addCardToBoard(DistrictCard.PRISON);

        //Should be Graveyard because its value are now the highest of the botStrong hand and its purple
        botStrong.addCardToBoard(botStrong.choiceHowToPlayDuringTheRound());
        assertEquals(DistrictCard.PALACE, botStrong.getBoard().get(5));

        //Should be Manor because its value are now the highest of the botStrong hand
        botStrong.addCardToBoard(botStrong.choiceHowToPlayDuringTheRound());
        assertEquals(DistrictCard.MANOR, botStrong.getBoard().get(6));

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
        characterDeck.getCards().remove(CharacterCard.THIEF);
        assertEquals(CharacterCard.MERCHANT, characterDeck.getCards().get(botStrong.chooseCharacter(characterDeck.getCards())));

        //Test with architect
        botStrong.getHands().add(DistrictCard.PALACE);
        botStrong.getHands().add(DistrictCard.TOWN_HALL);
        botStrong.getHands().add(DistrictCard.MARKET);
        botStrong.setGolds(20);
        assertEquals(CharacterCard.ARCHITECT, characterDeck.getCards().get(botStrong.chooseCharacter(characterDeck.getCards())));
    }
}