package fr.cotedazur.univ.polytech.model.bot;

import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import fr.cotedazur.univ.polytech.model.card.DistrictCard;
import fr.cotedazur.univ.polytech.model.deck.Deck;

import java.util.ArrayList;
import java.util.List;

public interface GameActions {
    /**
     * function where the player choose the card to put on his board
     * @return the district card chosen
     */
    DistrictCard putADistrict();

    /**
     * function where the player choose between take 2 golds or draw a card
     * @param districtDeck the district deck
     * @return his choice
     */
    String startChoice(Deck<DistrictCard> districtDeck);

    /**
     * function where the player choose to put or not a district on his board
     * @return his choice or null if he doesn't want to put a district
     */
    DistrictCard choiceHowToPlayDuringTheRound();

    /**
     * function where the player choose to use or not the effect of his character
     */
    CharacterCard selectWhoWillBeAffectedByThiefEffect(List<Player> players,List<CharacterCard> characterCards);

    /**
     * function where the player choose to use or not the effect of his character
     */
    CharacterCard selectWhoWillBeAffectedByAssassinEffect(List<Player> players, List<CharacterCard> characterCards);

    /**
     * function where the player choose his character
     * @param characters the characters available
     * @return the index of the character chosen
     */
    int chooseCharacter(List<CharacterCard> characters);
}
