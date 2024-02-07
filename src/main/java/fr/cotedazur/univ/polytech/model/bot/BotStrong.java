package fr.cotedazur.univ.polytech.model.bot;

import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import fr.cotedazur.univ.polytech.model.card.Color;
import fr.cotedazur.univ.polytech.model.card.DistrictCard;
import fr.cotedazur.univ.polytech.model.card.DistrictCardComparator;

import java.util.*;

public class BotStrong extends PlayerAssassinStrategy implements GameActions {
    private final Random random = new Random();

    @Override
    public DistrictCard chooseHandCardToDiscard() {
        if (!getHands().isEmpty()) {
            ArrayList<DistrictCard> listOfCardsForSort = (ArrayList<DistrictCard>) getHands();
            DistrictCardComparator districtCardComparator = new DistrictCardComparator();
            listOfCardsForSort.sort(districtCardComparator);
            return listOfCardsForSort.get(0);
        }
        return null;
    }

    public DistrictCard putADistrict() {
        discoverValidCard();

        Set<Color> colorsOnBoard = colorInList(getBoard());
        if (validCards.isEmpty()) return null;

        List<DistrictCard> purpleCard = new ArrayList<>();
        List<DistrictCard> colorNotOnBoard = new ArrayList<>();
        List<DistrictCard> cardsThatMatchWithRoleColor = new ArrayList<>();

        for (DistrictCard districtCard : validCards) {
            if (districtCard.getDistrictColor() == Color.PURPLE) purpleCard.add(districtCard);
            else if (!colorsOnBoard.contains(districtCard.getDistrictColor())) colorNotOnBoard.add(districtCard);
            else if (districtCard.getDistrictColor() == getPlayerRole().getCharacterColor())
                cardsThatMatchWithRoleColor.add(districtCard);
        }

        List<DistrictCard> prioritizedCards;
        if (purpleCard.isEmpty()) {
            if (cardsThatMatchWithRoleColor.isEmpty()) {
                prioritizedCards = colorNotOnBoard;
            } else {
                prioritizedCards = cardsThatMatchWithRoleColor;
            }
        } else {
            prioritizedCards = purpleCard;
        }
        return maxPrice(prioritizedCards.isEmpty() ? validCards : prioritizedCards);
    }

    /**
     * function that returns the list of colors present in the list of district cards
     *
     * @param districtCards list of district cards
     * @return the list of colors present in the list of district cards
     */
    public Set<Color> colorInList(List<DistrictCard> districtCards) {
        Set<Color> listeUnique = new HashSet<>();
        for (DistrictCard districtCard : districtCards) {
            listeUnique.add(districtCard.getDistrictColor());
        }
        return listeUnique;
    }

    /**
     * function that returns the district card with the highest value
     *
     * @param districtCards list of district cards
     * @return the district card with the highest value
     */
    public DistrictCard maxPrice(List<DistrictCard> districtCards) {
        if (!districtCards.isEmpty()) {
            DistrictCard maxValue = districtCards.get(0);
            for (DistrictCard card : districtCards) {
                if (maxValue.getDistrictValue() < card.getDistrictValue()) {
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
        Set<Color> colorsOnBoard = colorInList(getBoard());
        for (DistrictCard districtCard : validCards) {
            if (!colorsOnBoard.contains(districtCard.getDistrictColor())) {
                return DispatchState.TWO_GOLDS;

            }
        }
        if (getGolds() <= 4) {
            return DispatchState.TWO_GOLDS;
        }
        if (getHands().isEmpty() || validCards.isEmpty() || getGolds() >= 6) {
            return DispatchState.DRAW_CARD;
        }
        return DispatchState.TWO_GOLDS;
    }

    @Override
    public DistrictCard choiceHowToPlayDuringTheRound() {
        return putADistrict();
    }

    @Override
    public CharacterCard selectWhoWillBeAffectedByThiefEffect(List<Player> players, List<CharacterCard> characterCards) {
        if (getPlayerRole() == CharacterCard.THIEF) {
            return characterCards.get(3);
        }
        return null;
    }

    @Override
    public int chooseCharacter(List<CharacterCard> characters) {
        discoverValidCard();
        if (getGolds() == 0 && characters.contains(CharacterCard.MERCHANT)) {
            LOGGER.info("Le joueur " + getName() + " prend le marchant");
            return characters.indexOf(CharacterCard.MERCHANT);
        }
        if (getGolds() == 0 && !characters.contains(CharacterCard.MERCHANT) && characters.contains(CharacterCard.THIEF)) {
            LOGGER.info("Le joueur " + getName() + " prend le marchant");
            return characters.indexOf(CharacterCard.THIEF);
        }
        if (validCards.isEmpty() && characters.contains(CharacterCard.MAGICIAN)) {
            LOGGER.info("Le joueur " + getName() + " prend le magicien");
            return characters.indexOf(CharacterCard.MAGICIAN);
        }
        if (getGolds() >= 4 && validCards.size() >= 2 && characters.contains(CharacterCard.ARCHITECT)) {
            return characters.indexOf(CharacterCard.ARCHITECT);
        }
        if (hasColoredCards()) {
            int characterIndex = getCharacterAccordingToColor(characters);
            if (characterIndex != -1) {
                LOGGER.info("Le joueur " + getName() + " prend un personnage en fonction de la couleur de ses cartes");
                return characterIndex;
            }
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
                if (districtCard.getDistrictValue() <= 1) return DispatchState.DESTROY;
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
                return DispatchState.EXCHANGE_PLAYER;
            }
        }
        return DispatchState.EXCHANGE_DECK;
    }

    @Override
    public List<DistrictCard> chooseCardsToChange() {
        List<DistrictCard> districtCards = new ArrayList<>();
        for (DistrictCard districtCard : this.getHands()) {
            if (!validCards.contains(districtCard)) {
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
    public Color chooseColorForSchoolOfMagic() {
        if (getPlayerRole() == CharacterCard.KING || getPlayerRole() == CharacterCard.BISHOP || getPlayerRole() == CharacterCard.MERCHANT || getPlayerRole() == CharacterCard.WARLORD) {
            return getPlayerRole().getCharacterColor();
        }
        return Color.PURPLE;
    }

    @Override
    public Color chooseColorForHauntedCity() {
        Set<Color> colorsOnBoard = colorInList(getBoard());
        for (Color color : Color.values()) {
            if (!colorsOnBoard.contains(color)) {
                return color;
            }
        }
        return Color.PURPLE;
    }

    @Override
    public void drawCard
            (Map<DispatchState, ArrayList<DistrictCard>> cardsThatThePlayerDontWantAndThatThePlayerWant, DistrictCard...
                    cards) {
        ArrayList<DistrictCard> listOfCardsForSort = new ArrayList<>(List.of(cards));
        LOGGER.info("Cartes piochées : " + Arrays.toString(cards));
        DistrictCardComparator districtCardComparator = new DistrictCardComparator();
        listOfCardsForSort.sort(districtCardComparator);
        for (int i = 0; i < listOfCardsForSort.size(); i++) {
            if (i == 0 || (this.getBoard().contains(DistrictCard.LIBRARY) && i == 1)) {
                cardsThatThePlayerDontWantAndThatThePlayerWant.get(DispatchState.CARDS_WANTED).add(listOfCardsForSort.get(listOfCardsForSort.size() - 1 - i));
            } else {
                cardsThatThePlayerDontWantAndThatThePlayerWant.get(DispatchState.CARDS_NOT_WANTED).add(listOfCardsForSort.get(listOfCardsForSort.size() - 1 - i));
            }
        }
        LOGGER.info("Cartes jetées : " + cardsThatThePlayerDontWantAndThatThePlayerWant.get(DispatchState.CARDS_NOT_WANTED));
    }

    @Override
    public boolean wantToUseEffect(boolean beforePuttingADistrict) {
        discoverValidCard();
        for (DistrictCard districtCard : validCards) {
            if (districtCard.getDistrictColor() == this.getPlayerRole().getCharacterColor() && beforePuttingADistrict) {
                return getPlayerRole() == CharacterCard.WARLORD;
            }
        }
        return true;
    }

    @Override
    public boolean wantsToUseSmithyEffect() {
        return getGolds() >= 3 && validCards.isEmpty();
    }

    @Override
    public boolean wantToUseGraveyardEffect() {
        return true;
    }


    @Override
    public boolean wantToUseLaboratoryEffect() {
        discoverValidCard();
        for (DistrictCard card : this.getHands()) {
            if (!validCards.contains(card)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BotStrong botStrong)) return false;
        if (!super.equals(o)) return false;
        return random.equals(botStrong.random);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
