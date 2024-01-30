package fr.cotedazur.univ.polytech.model.bot;

import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import fr.cotedazur.univ.polytech.model.card.Color;
import fr.cotedazur.univ.polytech.model.card.DistrictCard;
import fr.cotedazur.univ.polytech.model.card.DistrictCardComparator;

import java.util.*;

public class BotStrong  extends Player implements GameActions {


    private final Random random = new Random();
    public BotStrong() {
        super();
    }

    @Override
    public DistrictCard chooseHandCardToDiscard() {
        return null;
    }

    @Override
    public DistrictCard putADistrict() {
        discoverValidCard();
        //List des différentes couleurs sur le Terrain
        Set<Color> colorsOnBoard = colorInList(getBoard());
        if (!validCards.isEmpty()) {
            List<DistrictCard> purpleCard = new ArrayList<>();
            List<DistrictCard> colorNotOnBoard = new ArrayList<>();
            for (DistrictCard districtCard : validCards){
                if (districtCard.getDistrictColor() == Color.PURPLE){
                    purpleCard.add(districtCard);
                }
                if (!colorsOnBoard.contains(districtCard.getDistrictColor())){
                    colorNotOnBoard.add(districtCard);
                }
            }
            if (!purpleCard.isEmpty() && !colorsOnBoard.contains(Color.PURPLE)) return maxPrice(purpleCard);

            if (!colorNotOnBoard.isEmpty()) return maxPrice(colorNotOnBoard);

            if (!purpleCard.isEmpty()) return maxPrice(purpleCard);

            return maxPrice(validCards);
        }

        return null;
    }



    public Set<Color> colorInList(List<DistrictCard> districtCards){
        Set<Color> listeUnique = new HashSet<>();
        for (DistrictCard districtCard : districtCards){
            listeUnique.add(districtCard.getDistrictColor());
        }
        return listeUnique;
    }

    public DistrictCard maxPrice(List<DistrictCard> districtCards){
        if (!districtCards.isEmpty()){
            DistrictCard maxValue = districtCards.get(0);
            for (DistrictCard card : districtCards){
                if (maxValue.getDistrictValue() < card.getDistrictValue()){
                    maxValue = card;
                }
            }
            return maxValue;
        }
        return null;
    }


    @Override
    public DispatchState startChoice() {
        discoverValidCard();
        //List des différentes couleurs sur le Terrain
        Set<Color> colorsOnBoard = colorInList(getBoard());
        if (getGolds() == 0) {
            return DispatchState.TWOGOLDS;
        }
        for (DistrictCard districtCard : validCards){
            if (!colorsOnBoard.contains(districtCard.getDistrictColor())){
                return DispatchState.TWOGOLDS;

            }
        }
        return DispatchState.DRAWCARD;
    }

    @Override
    public DistrictCard choiceHowToPlayDuringTheRound() { return putADistrict(); }

    @Override
    public CharacterCard selectWhoWillBeAffectedByThiefEffect(List<Player> players, List<CharacterCard> characterCards) {
        if (getPlayerRole() == CharacterCard.THIEF) {
            return characterCards.get(3);
        }
        return null;
    }

    @Override
    public CharacterCard selectWhoWillBeAffectedByAssassinEffect(List<Player> players, List<CharacterCard> characterCards) {
        if (getPlayerRole() == CharacterCard.ASSASSIN) {
            LOGGER.info("Le joueur " + getName() + " utilise l'effet de l'assassin");
            if (players.size() < 4) return characterCards.get(3);
            if (players.size() < 6) return characterCards.get(5);
            else return characterCards.get(6);
        }
        LOGGER.info("Le joueur " + getName() + " ne peut pas utiliser l'effet de l'assassin");
        return null;
    }

    @Override
    public int chooseCharacter(List<CharacterCard> characters) {
        discoverValidCard();
        if (getGolds() <= 2 && characters.contains(CharacterCard.THIEF)){
            LOGGER.info("Le joueur " + getName() + " prend le voleur");
            return characters.indexOf(CharacterCard.THIEF);
        }
        if (validCards.isEmpty() && characters.contains(CharacterCard.MAGICIAN)){
            LOGGER.info("Le joueur " + getName() + " prend le magicien");
            return characters.indexOf(CharacterCard.MAGICIAN);
        }
        if (getGolds() >= 10 && validCards.size()>= 2 && characters.contains(CharacterCard.ARCHITECT)){
            return characters.indexOf(CharacterCard.ARCHITECT);
        }
        return random.nextInt(characters.size());
    }

    @Override
    public Player choosePlayerToDestroy(List<Player> players) {
        for (Player player : players) {
            for (DistrictCard districtCard : player.getBoard()) {
                if (districtCard.getDistrictValue() <= 1) return player;
            }
        }
        return null;
    }

    @Override
    public DistrictCard chooseDistrictToDestroy(Player player, List<DistrictCard> districtCards) {
        for (DistrictCard districtCard : player.getBoard()) {
            if (districtCard.getDistrictValue() <= 1) return districtCard;
        }
        return null;
    }

    @Override
    public DispatchState whichWarlordEffect(List<Player> players) {
        for (Player player : players) {
            for (DistrictCard districtCard : player.getBoard()) {
                if (districtCard.getDistrictValue() <= 1) return DispatchState.KILL;
            }
        }
        return DispatchState.EARNDISTRICT_WARLORD;
    }

    @Override
    public DispatchState whichMagicianEffect(List<Player> players) {
        int nbCardPlayer = this.getHands().size();
        for (Player p : players) {
            int nbCardOther = p.getHands().size();
            if (nbCardOther > nbCardPlayer) {
                return DispatchState.EXCHANGEPLAYER;
            }
        }
        return DispatchState.EXCHANGEDECK;
    }

    @Override
    public List<DistrictCard> chooseCardsToChange() {
        List<DistrictCard> districtCards = new ArrayList<>();
        for (DistrictCard districtCard : this.getHands()) {
            if (!validCards.contains(districtCard)){
                districtCards.add(districtCard);
            }
        }
        return districtCards;
    }

    @Override
    public Player selectMagicianTarget(List<Player> players) {
        Player highNbCards = players.get(0);
        for (Player p : players) {
            if (p.getHands().size() >= highNbCards.getHands().size()) {
                highNbCards = p;
            }
        }
        return highNbCards;
    }

    @Override
    public Color chooseColorForDistrictCard() {
        //List des différentes couleurs sur le Terrain
        Set<Color> colorsOnBoard = colorInList(getBoard());
        if (getPlayerRole() == CharacterCard.KING || getPlayerRole() == CharacterCard.BISHOP || getPlayerRole() == CharacterCard.MERCHANT || getPlayerRole() == CharacterCard.WARLORD) {
            return getPlayerRole().getCharacterColor();
        }
        for (Color color : Color.values()){
            if (!colorsOnBoard.contains(color)){
                return color;
            }
        }
        return Color.RED;
    }

    @Override
    public void drawCard(Map<DispatchState, ArrayList<DistrictCard>> cardsThatThePlayerDontWantAndThatThePlayerWant, DistrictCard... cards) {
        ArrayList<DistrictCard> listOfCardsForSort = new ArrayList<>(List.of(cards));
        LOGGER.info("Cartes piochées : " + Arrays.toString(cards));
        DistrictCardComparator districtCardComparator = new DistrictCardComparator();
        listOfCardsForSort.sort(districtCardComparator);
        for (int i = 0; i < listOfCardsForSort.size(); i++) {
            if (i == 0 || (this.getBoard().contains(DistrictCard.LIBRARY) && i == 1)) {
                cardsThatThePlayerDontWantAndThatThePlayerWant.get(DispatchState.CARDSWANTED).add(listOfCardsForSort.get(listOfCardsForSort.size()-1-i));
            } else {
                cardsThatThePlayerDontWantAndThatThePlayerWant.get(DispatchState.CARDSNOTWANTED).add(listOfCardsForSort.get(listOfCardsForSort.size()-1-i));
            }
        }
        LOGGER.info("Cartes jetées : " + cardsThatThePlayerDontWantAndThatThePlayerWant.get(DispatchState.CARDSNOTWANTED));
    }

    @Override
    public boolean wantToUseEffect(boolean beforePuttingADistrict) {
        return beforePuttingADistrict;
    }

    @Override
    public boolean wantsToUseSmithyEffect() {
        return getGolds() >= 7;
    }

    @Override
    public boolean wantToUseGraveyardEffect() {
        return true;
    }

    @Override
    public boolean wantToUseLaboratoryEffect() {
        discoverValidCard();
        for (DistrictCard card : this.getHands()) {
            if (!validCards.contains(card)){
                return true;
            }
        }
        return false;
    }
}
