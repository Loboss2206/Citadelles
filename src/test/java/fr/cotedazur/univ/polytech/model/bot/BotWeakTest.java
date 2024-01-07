package fr.cotedazur.univ.polytech.model.bot;

import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import fr.cotedazur.univ.polytech.model.card.DistrictCard;
import fr.cotedazur.univ.polytech.model.deck.CharacterDeck;
import fr.cotedazur.univ.polytech.model.deck.DeckFactory;
import fr.cotedazur.univ.polytech.model.deck.DistrictDeck;
import fr.cotedazur.univ.polytech.view.GameView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BotWeakTest {

    Player botWeak;
    DistrictDeck districtDeck;


    @BeforeEach
    void setUp(){
        botWeak = new BotWeak();
        this.districtDeck = DeckFactory.createDistrictDeck();
        this.districtDeck.shuffle();
    }

    @Test
    void putADistrict() {
        botWeak.getHands().add(DistrictCard.PALACE);
        botWeak.getHands().add(DistrictCard.TEMPLE);
        botWeak.getHands().add(DistrictCard.GRAVEYARD);
        botWeak.getHands().add(DistrictCard.MARKET);
        botWeak.setGolds(20);
        botWeak.setPlayerRole(CharacterCard.ASSASSIN);

        //Should be Temple because its value are the smallest of the botWeak hand
        botWeak.addCardToBoard(botWeak.choiceHowToPlayDuringTheRound());
        assertEquals(DistrictCard.TEMPLE,botWeak.getBoard().get(0));

        //Should be Market because its value are now the smallest of the botWeak hand
        botWeak.addCardToBoard(botWeak.choiceHowToPlayDuringTheRound());
        assertEquals(DistrictCard.MARKET,botWeak.getBoard().get(1));
        botWeak.getHands().clear();

        //When District are equals
        botWeak.getHands().add(DistrictCard.MONASTERY);
        botWeak.getHands().add(DistrictCard.MANOR);
        botWeak.getHands().add(DistrictCard.KEEP);
        botWeak.getHands().add(DistrictCard.DOCKS);

        botWeak.addCardToBoard(botWeak.choiceHowToPlayDuringTheRound());

        //Should be Monastery because there are all equals and the order doesn't change
        assertEquals(DistrictCard.MONASTERY,botWeak.getBoard().get(2));
    }

    @Test
    void testUseEffectForBotWeak(){
        //Draw with architect
        botWeak.setPlayerRole(CharacterCard.ARCHITECT);
        botWeak.useRoleEffect(Optional.of(districtDeck), Optional.empty());
        assertEquals(2, botWeak.getHands().size());
        botWeak.getHands().clear();

        //Put 3 district with architect
        botWeak.getHands().add(DistrictCard.PALACE);
        botWeak.getHands().add(DistrictCard.PRISON);
        botWeak.getHands().add(DistrictCard.MANOR);
        botWeak.setGolds(24);
        botWeak.addCardToBoard(botWeak.choiceHowToPlayDuringTheRound());
        botWeak.getPlayerRole().useEffect(botWeak,(GameView) null);
        assertEquals(3, botWeak.getBoard().size());
        botWeak.getHands().clear();
        botWeak.getBoard().clear();

        //Trying to put 3 district with architect but gold are reduced
        botWeak.getHands().add(DistrictCard.PALACE);
        botWeak.getHands().add(DistrictCard.PRISON);
        botWeak.getHands().add(DistrictCard.MANOR);
        botWeak.setGolds(9);
        botWeak.addCardToBoard(botWeak.choiceHowToPlayDuringTheRound());
        botWeak.getPlayerRole().useEffect(botWeak,(GameView) null);
        assertEquals(2, botWeak.getBoard().size());
        botWeak.getHands().clear();
        botWeak.getBoard().clear();

        //Use merchant effect
        botWeak.setPlayerRole(CharacterCard.MERCHANT);
        botWeak.getHands().add(DistrictCard.PALACE);
        botWeak.getHands().add(DistrictCard.TOWN_HALL);
        botWeak.getHands().add(DistrictCard.MARKET);
        botWeak.setGolds(5);
        botWeak.useRoleEffect(Optional.empty(),Optional.empty());
        assertEquals(6,botWeak.getGolds());
        botWeak.addCardToBoard(DistrictCard.TOWN_HALL);
        botWeak.addCardToBoard(DistrictCard.MARKET);
        botWeak.setGolds(5);
        botWeak.useRoleEffect(Optional.empty(),Optional.empty());
        assertEquals(8,botWeak.getGolds());
        botWeak.getHands().clear();
        botWeak.getBoard().clear();
    }

    @Test
    void testChooseCharacter(){
        CharacterDeck characterDeck = DeckFactory.createCharacterDeck();
        botWeak.getHands().add(DistrictCard.SMITHY);
        botWeak.getHands().add(DistrictCard.PALACE);
        botWeak.getHands().add(DistrictCard.TOWN_HALL);
        botWeak.getHands().add(DistrictCard.MARKET);

        botWeak.addCardToBoard(DistrictCard.MARKET);
        botWeak.addCardToBoard(DistrictCard.TOWN_HALL);
        botWeak.addCardToBoard(DistrictCard.PALACE);
        assertEquals(CharacterCard.MERCHANT,characterDeck.getCards().get(botWeak.chooseCharacter(characterDeck.getCards())));

        //Test when merchant is not in list of characters
        characterDeck.getCards().remove(CharacterCard.MERCHANT);
        assertEquals(CharacterCard.KING,characterDeck.getCards().get(botWeak.chooseCharacter(characterDeck.getCards())));

        //Test with architect
        botWeak.getHands().add(DistrictCard.PALACE);
        botWeak.getHands().add(DistrictCard.TOWN_HALL);
        botWeak.getHands().add(DistrictCard.MARKET);
        //Can be architect because there are duplicates cards
        assertNotEquals(CharacterCard.ARCHITECT,characterDeck.getCards().get(botWeak.chooseCharacter(characterDeck.getCards())));
        botWeak.getHands().clear();
        botWeak.getBoard().clear();
        //Now we test if he chose the architect
        botWeak.getHands().add(DistrictCard.SMITHY);
        botWeak.getHands().add(DistrictCard.PALACE);
        botWeak.getHands().add(DistrictCard.TOWN_HALL);
        botWeak.getHands().add(DistrictCard.MARKET);
        botWeak.setGolds(80);
        assertEquals(CharacterCard.ARCHITECT,characterDeck.getCards().get(botWeak.chooseCharacter(characterDeck.getCards())));
    }

    @Test
    void testStartChoice(){
        //Test when we can put a district
        botWeak.getHands().add(DistrictCard.SMITHY);
        botWeak.getHands().add(DistrictCard.PALACE);
        botWeak.getHands().add(DistrictCard.TOWN_HALL);
        botWeak.getHands().add(DistrictCard.MARKET);
        botWeak.setGolds(80);
        botWeak.setPlayerRole(CharacterCard.ASSASSIN);
        assertEquals("drawCard",botWeak.startChoice(districtDeck));

        //when there is not enough golds
        botWeak.setGolds(0);
        assertEquals("2golds",botWeak.startChoice(districtDeck));

        //when hand is empty
        botWeak.setGolds(80);
        botWeak.getHands().clear();
        assertEquals("drawCard",botWeak.startChoice(districtDeck));
    }
}