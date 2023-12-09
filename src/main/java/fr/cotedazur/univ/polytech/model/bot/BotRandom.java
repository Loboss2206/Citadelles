package fr.cotedazur.univ.polytech.model.bot;

import java.util.Random;

import fr.cotedazur.univ.polytech.model.deck.DistrictDeck;

public class BotRandom extends Player implements GameActions {

    private final Random random = new Random();

    public BotRandom() {
        super();
    }

    @Override
    public String startChoice(DistrictDeck districtDeck) {
        int randomIndex = random.nextInt(2);
        switch (randomIndex) {
            case 0 -> {
                collectTwoGolds();
                return "2golds";
            }
            case 1 -> {
                drawCard(districtDeck);
                return "drawCard";
            }
            default -> {
                return null;
            }
        }
    }

    @Override
    public String choiceToPutADistrict() {
        int randomIndex = random.nextInt(2);
        if (randomIndex == 0) {
            putADistrict();
            return "putDistrict";
        }
        return null;
    }

    @Override
    public void putADistrict() {
        if (!getHands().isEmpty()) {
            int randomIndex = random.nextInt(getHands().size());
            getBoard().add(getHands().get(randomIndex));
            getHands().remove(randomIndex);
        }
    }

    @Override
    public void useRoleEffect() {

    }

    @Override
    public void chooseCharacter() {

    }
}
