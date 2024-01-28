package fr.cotedazur.univ.polytech.model.bot;

import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import fr.cotedazur.univ.polytech.model.card.Color;
import fr.cotedazur.univ.polytech.model.card.DistrictCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BotStrong  extends Player implements GameActions {

    public BotStrong() {
        super();
    }

    @Override
    public DistrictCard chooseHandCardToDiscard() {
        return null;
    }

    @Override
    public DistrictCard putADistrict() {
        return null;
    }

    @Override
    public DispatchState startChoice() {
        return null;
    }

    @Override
    public DistrictCard choiceHowToPlayDuringTheRound() {
        return null;
    }

    @Override
    public CharacterCard selectWhoWillBeAffectedByThiefEffect(List<Player> players, List<CharacterCard> characterCards) {
        return null;
    }

    @Override
    public CharacterCard selectWhoWillBeAffectedByAssassinEffect(List<Player> players, List<CharacterCard> characterCards) {
        return null;
    }

    @Override
    public int chooseCharacter(List<CharacterCard> characters) {
        return 0;
    }

    @Override
    public Player choosePlayerToDestroy(List<Player> players) {
        return null;
    }

    @Override
    public DistrictCard chooseDistrictToDestroy(Player player, List<DistrictCard> districtCards) {
        return null;
    }

    @Override
    public DispatchState whichWarlordEffect(List<Player> players) {
        return null;
    }

    @Override
    public DispatchState whichMagicianEffect(List<Player> players) {
        return null;
    }

    @Override
    public List<DistrictCard> chooseCardsToChange() {
        return null;
    }

    @Override
    public Player selectMagicianTarget(List<Player> players) {
        return null;
    }

    @Override
    public Color chooseColorForDistrictCard() {
        return null;
    }

    @Override
    public void drawCard(Map<DispatchState, ArrayList<DistrictCard>> cardsThatThePlayerDontWantAndThatThePlayerWant, DistrictCard... cards) {

    }

    @Override
    public boolean wantToUseEffect(boolean beforePuttingADistrict) {
        return false;
    }

    @Override
    public boolean wantsToUseSmithyEffect() {
        return false;
    }

    @Override
    public boolean wantToUseGraveyardEffect() {
        return false;
    }

    @Override
    public boolean wantToUseLaboratoryEffect() {
        return false;
    }
}
