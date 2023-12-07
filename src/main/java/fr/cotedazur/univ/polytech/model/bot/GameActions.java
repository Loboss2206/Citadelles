package fr.cotedazur.univ.polytech.model.bot;

import fr.cotedazur.univ.polytech.model.deck.DistrictDeck;

public interface GameActions {
    public void drawCard(DistrictDeck districtDeck);

    public void collectTwoGolds();

    public boolean putADisctrict();

    public void useRoleEffect();

    public void chooseCharacter();
}
