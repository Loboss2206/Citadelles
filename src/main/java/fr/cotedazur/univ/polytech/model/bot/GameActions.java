package fr.cotedazur.univ.polytech.model.bot;

import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import fr.cotedazur.univ.polytech.model.card.Color;
import fr.cotedazur.univ.polytech.model.card.DistrictCard;

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
     * @return his choice
     */
    String startChoice();

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

    /**
     * function where the player choose the effect he wants use as magician
     * @param players the players of the game
     * @return the effect chosen
     */
    String whichMagicianEffect(List<Player> players);

    List<DistrictCard> chooseCardsToChange();

    Player selectMagicianTarget(List<Player> players);

     /**
     * function where the player choose the color of the district he wants to replace the purple district ("School of Magic")
     * @return the color chosen
     */
    Color chooseColorForDistrictCard();

    public List<DistrictCard> drawCard(DistrictCard... cards);//When we will implement th purple card that let us draw 3 cards
}
