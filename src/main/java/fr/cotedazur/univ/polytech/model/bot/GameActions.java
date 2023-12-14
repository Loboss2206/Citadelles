package fr.cotedazur.univ.polytech.model.bot;

import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import fr.cotedazur.univ.polytech.model.deck.DistrictDeck;

import java.util.List;

public interface GameActions {
    public String drawCard(DistrictDeck districtDeck);

    public String collectTwoGolds();

    public boolean putADistrict();

    public String startChoice(DistrictDeck districtDeck);

    public String choiceToPutADistrict();

    public void useRoleEffect();

    public int chooseCharacter(List<CharacterCard> characters);
}
