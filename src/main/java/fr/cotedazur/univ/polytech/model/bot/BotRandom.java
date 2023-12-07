package fr.cotedazur.univ.polytech.model.bot;

import java.util.Random;
import fr.cotedazur.univ.polytech.model.card.DistrictCard;

public class BotRandom extends Player {

    public BotRandom() {
        super();
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
