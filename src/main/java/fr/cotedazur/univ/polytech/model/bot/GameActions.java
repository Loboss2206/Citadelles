package fr.cotedazur.univ.polytech.model.bot;

import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import fr.cotedazur.univ.polytech.model.deck.DistrictDeck;

import java.util.List;

public interface GameActions {
    public void drawCard(DistrictDeck districtDeck);

    public void collectTwoGolds();

    public boolean putADistrict();

    public String startChoice(DistrictDeck districtDeck);

    public String choiceToPutADistrict();

    public void useRoleEffect();

    public int chooseCharacter(List<CharacterCard> characters);
}
