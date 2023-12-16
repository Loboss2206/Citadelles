package fr.cotedazur.univ.polytech.model.bot;

import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import fr.cotedazur.univ.polytech.model.deck.DistrictDeck;

import java.util.List;

public interface GameActions {

    String putADistrict();

    String startChoice(DistrictDeck districtDeck);

    String choiceToPutADistrict();

    void useRoleEffect();

    int chooseCharacter(List<CharacterCard> characters);
}
