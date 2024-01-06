package fr.cotedazur.univ.polytech.model.bot;

import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import fr.cotedazur.univ.polytech.model.card.DistrictCard;
import fr.cotedazur.univ.polytech.model.card.DistrictCardComparator;
import fr.cotedazur.univ.polytech.model.deck.DistrictDeck;
import fr.cotedazur.univ.polytech.view.GameView;

import java.util.List;
import java.util.Optional;

public class BotWeak extends Player implements GameActions {

    public BotWeak() {
        super();
    }

    @Override
    public DistrictCard putADistrict() {
        discoverValidCard();
        if (!validCards.isEmpty()) {
            //Sort the hands from the smallest to the biggest
            validCards.sort(new DistrictCardComparator());
            return validCards.get(0);
        }
        return null;
    }

    @Override
    public String startChoice(DistrictDeck districtDeck) {
        //TODO
        return collectTwoGolds();
    }

    @Override
    public DistrictCard choiceToPutADistrict() {
        return putADistrict();
    }

    @Override
    public void useRoleEffect(Optional<DistrictDeck> districtDeck, Optional<GameView> view) {
        //TODO
    }

    @Override
    public int chooseCharacter(List<CharacterCard> characters) {
        //TODO
        return 0;
    }
}
