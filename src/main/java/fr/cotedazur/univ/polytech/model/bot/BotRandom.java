package fr.cotedazur.univ.polytech.model.bot;

import java.util.List;
import java.util.Random;

import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import fr.cotedazur.univ.polytech.model.card.DistrictCard;
import fr.cotedazur.univ.polytech.model.deck.DistrictDeck;

public class BotRandom extends Player implements GameActions {

    private Random random = new Random();

    public BotRandom() {
        super();
    }

    @Override
    public String startChoice(DistrictDeck districtDeck) {
        int randomIndex = random.nextInt(2);
        switch (randomIndex) {
            case 0 -> {
                return collectTwoGolds();
            }
            case 1 -> {
                return drawCard(districtDeck);
            }
            default -> {
                return null;
            }
        }
    }

    @Override
    public DistrictCard choiceToPutADistrict() {
        int randomIndex = random.nextInt(2);
        if (randomIndex == 0) {
            return putADistrict();
        }
        return null;
    }

    @Override
    public DistrictCard putADistrict() {
        discoverValidCard();
        if (!validCards.isEmpty()) {
            int randomIndex = random.nextInt(validCards.size());
            return validCards.get(randomIndex);
        }
        return null;
    }

    @Override
    public void useRoleEffect() {
        // TODO when the bot will be able to use the effects of its character
    }

    @Override
    public int chooseCharacter(List<CharacterCard> cards) {
        return random.nextInt(cards.size()); //return a random number between 0 and the size of the list
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
