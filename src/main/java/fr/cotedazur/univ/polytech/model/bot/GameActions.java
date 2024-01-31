package fr.cotedazur.univ.polytech.model.bot;

import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import fr.cotedazur.univ.polytech.model.card.Color;
import fr.cotedazur.univ.polytech.model.card.DistrictCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface GameActions {
    /**
     * function where the player choose the card to put on his board
     *
     * @return the district card chosen
     */
    DistrictCard putADistrict();

    /**
     * function where the player choose between take 2 golds or draw a card
     *
     * @return his choice
     */
    DispatchState startChoice();

    /**
     * function where the player choose to put or not a district on his board
     *
     * @return his choice or null if he doesn't want to put a district
     */
    DistrictCard choiceHowToPlayDuringTheRound();

    /**
     * function where the player choose to use or not the effect of his character
     */
    CharacterCard selectWhoWillBeAffectedByThiefEffect(List<Player> players, List<CharacterCard> characterCards);

    /**
     * function where the player choose to use or not the effect of his character
     */
    CharacterCard selectWhoWillBeAffectedByAssassinEffect(List<Player> players, List<CharacterCard> characterCards);

    /**
     * function where the player choose his character
     *
     * @param characters the characters available
     * @return the index of the character chosen
     */
    int chooseCharacter(List<CharacterCard> characters);

    /**
     * function where the player choose a player to destroy one of his districts
     *
     * @param players the list of players
     * @return the player chosen
     */
    Player choosePlayerToDestroy(List<Player> players);

    /**
     * function where the player choose a district to destroy
     *
     * @param player the player to destroy
     * @return the district chosen
     */
    DistrictCard chooseDistrictToDestroy(Player player, List<DistrictCard> districtCards);

    /**
     * function where the player choose the effect he wants use as warlord
     *
     * @param players the players of the game
     * @return the effect chosen
     */
    DispatchState whichWarlordEffect(List<Player> players);

    /**
     * function where the player choose the effect he wants use as magician
     *
     * @param players the players of the game
     * @return the effect chosen
     */
    DispatchState whichMagicianEffect(List<Player> players);

    /**
     * function where the player choose the cards he wants to exchange with the deck
     *
     * @return the cards chosen
     */
    List<DistrictCard> chooseCardsToChange();

    /**
     * function where the player choose the player he wants to exchange his hand with
     *
     * @param players the players of the game
     * @return the player chosen
     */
    Player selectMagicianTarget(List<Player> players);

    /**
     * function where the player choose the color of the district he wants to replace the purple district ("School of Magic")
     *
     * @return the color chosen
     */
    Color chooseColorForSchoolOfMagic();

    /**
     * function where the player choose the color of the district he wants to replace the purple district ("Haunted City")
     *
     * @return the color chosen
     */
    Color chooseColorForHauntedCity();

    /**
     * function where the player choose the card he wants from the cards he draws
     *
     * @param cards the cards he draws
     */
    void drawCard(Map<DispatchState, ArrayList<DistrictCard>> cardsThatThePlayerDontWantAndThatThePlayerWant, DistrictCard... cards);//When we will implement th purple card that let us draw 3 cards

    /**
     * function where the player choose to use the effect of his character or not
     *
     * @param beforePuttingADistrict true if it is before putting a district, false otherwise
     * @return true if he wants to use the effect, false otherwise
     */
    boolean wantToUseEffect(boolean beforePuttingADistrict);

    /**
     * function where the player choose to use the effect of the smithy or not
     *
     * @return true if he wants to use the effect, false otherwise
     */
    boolean wantsToUseSmithyEffect();

    /**
     * function where the player choose to use the effect of the graveyard or not
     *
     * @return true if he wants to use the effect, false otherwise
     */
    boolean chooseUseGraveyardEffect();
}
