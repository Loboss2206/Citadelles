package fr.cotedazur.univ.polytech.model.bot;

import java.util.Random;

import fr.cotedazur.univ.polytech.controller.Round;
import fr.cotedazur.univ.polytech.model.deck.DistrictDeck;

public class BotRandom extends Player implements GameActions {
    Round round;

    public BotRandom() {
        super();
    }

    @Override
    public String startChoice(DistrictDeck districtDeck) {
        Random rand = new Random();
        int randomIndex = rand.nextInt(2);
        switch (randomIndex) {
            case 0 -> {
                collectTwoGolds();
                return "2golds";
            }
            case 1 -> {
                drawCard(districtDeck);
                return "drawCard";
            }
        }
        return null;
    }

    @Override
    public String choiceToPutADistrict() {
        Random rand = new Random();
        int randomIndex = rand.nextInt(2);
        if (randomIndex == 0) {
            putADisctrict();
            return "putDistrict";
        }
        return null;
    }

    @Override
    public boolean putADisctrict() {
        if (!getHands().isEmpty()) {
            Random rand = new Random();
            int randomIndex = rand.nextInt(getHands().size());
            getBoard().add(getHands().get(randomIndex));
            getHands().remove(randomIndex);
            return true;
        }
        return false;
    }

    @Override
    public void useRoleEffect() {

    }

    @Override
    public void chooseCharacter() {

    }
}
