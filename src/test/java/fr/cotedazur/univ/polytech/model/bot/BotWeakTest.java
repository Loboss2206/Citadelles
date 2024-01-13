package fr.cotedazur.univ.polytech.model.bot;

import fr.cotedazur.univ.polytech.controller.EffectController;
import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import fr.cotedazur.univ.polytech.model.card.DistrictCard;
import fr.cotedazur.univ.polytech.model.deck.Deck;
import fr.cotedazur.univ.polytech.model.deck.DeckFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BotWeakTest {

    Player botWeak;
    Deck<DistrictCard> districtDeck;


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
        botWeak.getPlayerRole().useEffectArchitect(botWeak,districtDeck);
        assertEquals(2, botWeak.getHands().size());
        botWeak.getHands().clear();

        /*//Put 3 district with architect
        botWeak.getHands().add(DistrictCard.PALACE);
        botWeak.getHands().add(DistrictCard.PRISON);
        botWeak.getHands().add(DistrictCard.MANOR);
        botWeak.setGolds(24);
        botWeak.addCardToBoard(botWeak.choiceHowToPlayDuringTheRound());
        botWeak.getPlayerRole().useEffect(botWeak,(Player) null);
        assertEquals(3, botWeak.getBoard().size());
        botWeak.getHands().clear();
        botWeak.getBoard().clear();*/

        /*//Trying to put 3 district with architect but gold are reduced
        botWeak.getHands().add(DistrictCard.PALACE);
        botWeak.getHands().add(DistrictCard.PRISON);
        botWeak.getHands().add(DistrictCard.MANOR);
        botWeak.setGolds(9);
        botWeak.addCardToBoard(botWeak.choiceHowToPlayDuringTheRound());
        botWeak.getPlayerRole().useEffect(botWeak,(Player) null);
        assertEquals(2, botWeak.getBoard().size());
        botWeak.getHands().clear();
        botWeak.getBoard().clear();*/

        //Use merchant effect
        botWeak.setPlayerRole(CharacterCard.MERCHANT);
        botWeak.getHands().add(DistrictCard.PALACE);
        botWeak.getHands().add(DistrictCard.TOWN_HALL);
        botWeak.getHands().add(DistrictCard.MARKET);
        botWeak.setGolds(5);
        botWeak.getPlayerRole().useEffect(botWeak);
        assertEquals(6,botWeak.getGolds());
        botWeak.addCardToBoard(DistrictCard.TOWN_HALL);
        botWeak.addCardToBoard(DistrictCard.MARKET);
        botWeak.setGolds(5);
        botWeak.getPlayerRole().useEffect(botWeak);
        assertEquals(8,botWeak.getGolds());
        botWeak.getHands().clear();
        botWeak.getBoard().clear();

        //Use Thief effect
        botWeak.setGolds(5);
        botWeak.setPlayerRole(CharacterCard.THIEF);
        ArrayList<Player> players = new ArrayList<>();
        Player player = new BotWeak();
        player.setGolds(31);
        player.setPlayerRole(CharacterCard.BISHOP);
        players.add(player);
        Player player2 = new BotWeak();
        player2.setGolds(32);
        player2.setPlayerRole(CharacterCard.ASSASSIN);
        players.add(player2);
        Player player3 = new BotWeak();
        player3.setGolds(33);
        player3.setPlayerRole(CharacterCard.MERCHANT);
        players.add(player3);

        EffectController effectController = new EffectController();
        effectController.playerWantToUseEffect(botWeak,players);
        assertEquals(38,botWeak.getGolds());

        players.clear();

        for (CharacterCard characterCard : CharacterCard.values()) {
            if (characterCard != CharacterCard.ASSASSIN) {
                Player currentPlayer = new BotWeak();
                currentPlayer.setGolds(10);
                currentPlayer.setPlayerRole(characterCard);
                players.add(currentPlayer);
            }
        }

        botWeak.setPlayerRole(CharacterCard.ASSASSIN);
        EffectController effectController2 = new EffectController();
        effectController2.playerWantToUseEffect(botWeak, players);
        assertTrue(players.get(6).isDead());
        for (Player player1 : players) {
            if (player1.getPlayerRole() != CharacterCard.ASSASSIN && player1.getPlayerRole() != players.get(6).getPlayerRole()) {
                assertFalse(player1.isDead());
            }
        }


    }

    @Test
    void testChooseCharacter(){
        Deck<CharacterCard> characterDeck = DeckFactory.createCharacterDeck();
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