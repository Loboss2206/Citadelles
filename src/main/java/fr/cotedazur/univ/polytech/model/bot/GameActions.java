package fr.cotedazur.univ.polytech.model.bot;

import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import fr.cotedazur.univ.polytech.model.card.DistrictCard;
import fr.cotedazur.univ.polytech.model.deck.Deck;

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
     * function where the player choose his character
     * @param characters the characters available
     * @return the index of the character chosen
     */
    int chooseCharacter(List<CharacterCard> characters);

    /**
     * function where the player choose a player to destroy one of his districts
     * @param players the list of players
     * @return the player chosen
     */
    Player choosePlayerToDestroy(List<Player> players);

    /**
     * function where the player choose a district to destroy
     * @param player the player to destroy
     * @return the district chosen
     */
    DistrictCard chooseDistrictToDestroy(Player player, List<DistrictCard> districtCards);

    /**
     * function where the player choose the effect he wants use as warlord
     * @param players the players of the game
     * @return the effect chosen
     */
    String whichWarlordEffect(List<Player> players);
}
