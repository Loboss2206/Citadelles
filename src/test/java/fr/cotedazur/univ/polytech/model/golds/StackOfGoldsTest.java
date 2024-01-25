package fr.cotedazur.univ.polytech.model.golds;

import fr.cotedazur.univ.polytech.model.bot.BotRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StackOfGoldsTest {

    StackOfGolds stackOfGolds;

    BotRandom botRandom;


    @BeforeEach
    void setUp() {
        stackOfGolds = new StackOfGolds();
        botRandom = new BotRandom();
    }

    @Test
    void takeACoin() {
        //Verify that the player take a gold and don't take a gold if the stack is empty
        for (int i = 0; i < 50; i++) {
            botRandom.setGolds(botRandom.getGolds() + stackOfGolds.takeAGold());
        }
        assertEquals(30, botRandom.getGolds());
    }

    @Test
    void addCoinsToStack() {
        //verify that it's impossible to add when the golds are equal to 30
        stackOfGolds.addGoldsToStack(8);
        assertEquals(30, stackOfGolds.getNbGolds());

        //verify that it's impossible to add when the golds are equal to 30
        for (int i = 0; i < 10; i++) stackOfGolds.takeAGold();
        stackOfGolds.addGoldsToStack(8);
        assertEquals(28, stackOfGolds.getNbGolds());
    }
}