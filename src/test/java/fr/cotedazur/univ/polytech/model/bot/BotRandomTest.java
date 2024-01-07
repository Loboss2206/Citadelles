package fr.cotedazur.univ.polytech.model.bot;

import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import fr.cotedazur.univ.polytech.model.card.DistrictCard;
import fr.cotedazur.univ.polytech.model.deck.CharacterDeck;
import fr.cotedazur.univ.polytech.model.deck.DeckFactory;
import fr.cotedazur.univ.polytech.model.deck.DistrictDeck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Collections;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;


class BotRandomTest {
    @Mock Random random = mock(Random.class);

    BotRandom botRandom2;
    BotRandom botRandom1;

    private DistrictDeck districtDeck;

    @BeforeEach
    void setUp() {
        botRandom1 = new BotRandom();
        botRandom2 = new BotRandom();
        botRandom1.setRandom(random);
        this.districtDeck = DeckFactory.createDistrictDeck();
        this.districtDeck.shuffle();
    }

    @Test
    void testPutADistrict() {
        botRandom2.getHands().add(DistrictCard.TRADING_POST);
        botRandom2.setGolds(DistrictCard.TRADING_POST.getDistrictValue());
        assertNotNull(botRandom2.putADistrict());
        botRandom2.setGolds(DistrictCard.TRADING_POST.getDistrictValue());
        assertEquals(botRandom2.putADistrict(), botRandom2.getHands().get(0));
        botRandom2.getBoard().clear();

        botRandom2.drawCard(districtDeck);
        botRandom2.setGolds(botRandom2.getHands().get(0).getDistrictValue());
        assertNotNull(botRandom2.putADistrict());

        botRandom2.getHands().clear();
        assertNull(botRandom2.putADistrict());
    }

    @Test
    void testChoiceToPutADistrictIfNoCardsInHand() {
        botRandom2.setPlayerRole(CharacterCard.ASSASSIN);
        assertNull(botRandom2.choiceToPutADistrict());
    }

    @Test
    void testBotRandomActionIfDistrictDeckIsEmpty() {
        districtDeck.clear();
        int currentGolds1 = botRandom1.getGolds();
        int currentGolds2 = botRandom2.getGolds();
        botRandom2.startChoice(districtDeck);
        botRandom1.startChoice(districtDeck);

        //If the deck is empty the bot should collect 2 golds
        assertEquals(currentGolds1 + 2, botRandom1.getGolds());
        assertEquals(currentGolds2 + 2, botRandom2.getGolds());
    }

    @Test
    void testBotRandomPutADistrict(){
        //Taking the third card from the hand of the random bot
        when(random.nextInt(anyInt())).thenReturn(2);
        botRandom1.setGolds(20); //add golds to be able to put a district
        botRandom1.drawCard(districtDeck);
        botRandom1.drawCard(districtDeck);
        botRandom1.drawCard(districtDeck);
        botRandom1.drawCard(districtDeck);

        //Store the card that will be drawn from the hand
        DistrictCard districtCard = botRandom1.getHands().get(2);
        botRandom1.addCardToBoard(botRandom1.putADistrict());
        assertEquals(districtCard,botRandom1.getBoard().get(0));
    }

    @Test
    void testBotRandomCollect2golds(){
        //0 is when the random bot should take the golds
        when(random.nextInt(anyInt())).thenReturn(0);

        //We store the golds of the bot to see the evolution after startChoice function
        int oldGoldsValue = botRandom1.getGolds();
        botRandom1.startChoice(districtDeck);

        //the bot should have 2 golds added
        assertEquals(oldGoldsValue + 2,botRandom1.getGolds());
    }

    @Test
    void testBotRandomDrawCard(){
        //With 1 the bot Random will choose to draw a card
        when(random.nextInt(anyInt())).thenReturn(1);
        int oldHandSize = botRandom1.getHands().size();
        botRandom1.startChoice(districtDeck);

        //Verify that the hand size is correct
        assertEquals(oldHandSize + 1,botRandom1.getHands().size());
        assertEquals(65, districtDeck.size());

        //Verify that the card drawn is not the deck by checking if there is not all the number of this type of card in the deck
        DistrictCard cardDrawn = botRandom1.getHands().get(0);
        assertEquals(cardDrawn.getQuantityInDeck() - 1, Collections.frequency(districtDeck.getCards(),cardDrawn));

        //Check if the assertEquals is working
        districtDeck.add(cardDrawn);
        assertEquals(cardDrawn.getQuantityInDeck(), Collections.frequency(districtDeck.getCards(),cardDrawn));
    }

    @Test
    void testChoiceToPutADistrict(){
        //Put a district for the first call of the fonction and choose the
        when(random.nextInt(anyInt())).thenReturn(1).thenReturn(0).thenReturn(1);

        botRandom1.setGolds(20); //add golds to be able to put a district

        botRandom1.drawCard(districtDeck);
        botRandom1.drawCard(districtDeck);
        botRandom1.setPlayerRole(CharacterCard.ASSASSIN);

        //Take a card to test if the bot has chosen to put a district
        DistrictCard card = botRandom1.getHands().get(1);
        assertEquals(card,botRandom1.choiceToPutADistrict());
        botRandom1.addCardToBoard(card);
        botRandom1.addCardToBoard(botRandom1.getHands().get(0));

        //Test when there is no card in hand
        when(random.nextInt(anyInt())).thenReturn(0).thenReturn(1);
        assertNull(botRandom1.choiceToPutADistrict());

        //Test when bot choose to not put a district
        botRandom1.drawCard(districtDeck);
        botRandom1.drawCard(districtDeck);
        when(random.nextInt(anyInt())).thenReturn(1);
        assertNull(botRandom1.choiceToPutADistrict());
    }

    @Test
    void testUseRoleEffect() {
        CharacterCard characterCard = spy(CharacterCard.KING);
        botRandom1.setPlayerRole(characterCard);
        botRandom1.useRoleEffect(Optional.of(districtDeck), Optional.empty());
        verify(characterCard, times(1)).useEffect(botRandom1, districtDeck);
    }

    @Test
    void testChooseCharacter(){
        //Test with king
        when(random.nextInt(anyInt())).thenReturn(3);
        CharacterDeck characterDeck = DeckFactory.createCharacterDeck();
        int characterNumber = botRandom1.chooseCharacter(characterDeck.getCards());
        botRandom1.setPlayerRole( characterDeck.draw(characterNumber));
        assertEquals(CharacterCard.KING,botRandom1.getPlayerRole());

        //Test with warlord
        when(random.nextInt(anyInt())).thenReturn(6);
        characterNumber = botRandom1.chooseCharacter(characterDeck.getCards());
        botRandom1.setPlayerRole( characterDeck.draw(characterNumber));
        assertEquals(CharacterCard.WARLORD,botRandom1.getPlayerRole());

        //Test with assassin
        when(random.nextInt(anyInt())).thenReturn(0);
        characterNumber = botRandom1.chooseCharacter(characterDeck.getCards());
        botRandom1.setPlayerRole( characterDeck.draw(characterNumber));
        assertEquals(CharacterCard.ASSASSIN,botRandom1.getPlayerRole());

        //Test with bishop
        when(random.nextInt(anyInt())).thenReturn(2);
        characterNumber = botRandom1.chooseCharacter(characterDeck.getCards());
        botRandom1.setPlayerRole( characterDeck.draw(characterNumber));
        assertEquals(CharacterCard.BISHOP,botRandom1.getPlayerRole());

        //Test with architect
        when(random.nextInt(anyInt())).thenReturn(3);
        characterNumber = botRandom1.chooseCharacter(characterDeck.getCards());
        botRandom1.setPlayerRole( characterDeck.draw(characterNumber));
        assertEquals(CharacterCard.ARCHITECT,botRandom1.getPlayerRole());

        //Test with merchant
        when(random.nextInt(anyInt())).thenReturn(2);
        characterNumber = botRandom1.chooseCharacter(characterDeck.getCards());
        botRandom1.setPlayerRole( characterDeck.draw(characterNumber));
        assertEquals(CharacterCard.MERCHANT,botRandom1.getPlayerRole());

        //Test with magician
        when(random.nextInt(anyInt())).thenReturn(1);
        characterNumber = botRandom1.chooseCharacter(characterDeck.getCards());
        botRandom1.setPlayerRole( characterDeck.draw(characterNumber));
        assertEquals(CharacterCard.MAGICIAN,botRandom1.getPlayerRole());

        //Test with thief
        when(random.nextInt(anyInt())).thenReturn(0);
        characterNumber = botRandom1.chooseCharacter(characterDeck.getCards());
        botRandom1.setPlayerRole( characterDeck.draw(characterNumber));
        assertEquals(CharacterCard.THIEF,botRandom1.getPlayerRole());
    }
}