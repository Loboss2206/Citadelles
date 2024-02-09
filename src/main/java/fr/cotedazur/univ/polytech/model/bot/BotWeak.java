package fr.cotedazur.univ.polytech.model.bot;

import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import fr.cotedazur.univ.polytech.model.card.Color;
import fr.cotedazur.univ.polytech.model.card.DistrictCard;
import fr.cotedazur.univ.polytech.model.card.DistrictCardComparator;

import java.util.*;

public class BotWeak extends CommonMethod implements GameActions {

    private final Random random = new Random();

    public BotWeak() {
        super();
    }

    @Override
    public DistrictCard putADistrict() {
        if (hasValidCard()) {
            //Sort the hands from the smallest to the biggest
            validCards.sort(new DistrictCardComparator());
            return validCards.get(0);
        }
        return null;
    }

    @Override
    public DispatchState startChoice() {
        discoverValidCard();
        if (getGolds() == 0) {
            return DispatchState.TWO_GOLDS;
        }

        if (getHands().isEmpty() || validCards.isEmpty() || getGolds() >= 6) {
            return DispatchState.DRAW_CARD;
        }
        return DispatchState.TWO_GOLDS;
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
        // we sort to know if we can put 2 times a district or more by comparing the first two value
        validCards.sort(new DistrictCardComparator());

        if (isArchitectOptimal(characters)) {
            LOGGER.info("Le joueur " + getName() + " prend l'architecte, car il est désigné comme optimal");
            return characters.indexOf(CharacterCard.ARCHITECT);
        } else if (hasColoredCards()) {
            int characterIndex = getCharacterAccordingToColor(characters);
            if (characterIndex != -1) {
                LOGGER.info("Le joueur " + getName() + " prend un personnage en fonction de la couleur de ses cartes");
                return characterIndex;
            }
        }

        return random.nextInt(characters.size());
    }

    /**
     * function that checks whether it's worth taking the architect
     *
     * @param characters the characters available
     * @return true if it's worth taking the architect, else false
     */
    private boolean isArchitectOptimal(List<CharacterCard> characters) {
        return (validCards.size() >= 2 && characters.contains(CharacterCard.ARCHITECT) &&
                validCards.get(0).getDistrictValue() + validCards.get(1).getDistrictValue() <= getGolds()) ||
                (getHands().isEmpty() && characters.contains(CharacterCard.ARCHITECT));
    }

    @Override
    public Color chooseColorForSchoolOfMagic() {
        return getPlayerRole().getCharacterColor();
    }

    @Override
    public Color chooseColorForHauntedCity() {
        for (Color color : Color.values()) {
            if (color.equals(Color.GRAY)) continue;
            if (countNumberOfSpecifiedColorCard(color) == 0) {
                return color;
            }
        }
        return Color.PURPLE;
    }


    @Override
    public boolean wantToUseLaboratoryEffect() {
        for (DistrictCard card : this.getHands()) {
            if (card.getDistrictValue() >= 3) {
                return true;
            }
        }
        return false;
    }

    @Override
    public DistrictCard chooseHandCardToDiscard() {
        if (!getHands().isEmpty()) {
            for (DistrictCard districtCard : getHands()) {
                if (districtCard.getDistrictValue() >= 3) {
                    return districtCard;
                }
            }
        }
        return null;
    }


    @Override
    public void drawCard(Map<DispatchState, ArrayList<DistrictCard>> cardsThatThePlayerDontWantAndThatThePlayerWant, DistrictCard... cards) {
        ArrayList<DistrictCard> listOfCardsForSort = new ArrayList<>(List.of(cards));
        LOGGER.info("Cartes piochées : " + Arrays.toString(cards));
        DistrictCardComparator districtCardComparator = new DistrictCardComparator();
        listOfCardsForSort.sort(districtCardComparator);
        for (int i = 0; i < listOfCardsForSort.size(); i++) {
            if (i == 0 || (this.getBoard().contains(DistrictCard.LIBRARY) && i == 1)) {
                cardsThatThePlayerDontWantAndThatThePlayerWant.get(DispatchState.CARDS_WANTED).add(listOfCardsForSort.get(i));
            } else {
                cardsThatThePlayerDontWantAndThatThePlayerWant.get(DispatchState.CARDS_NOT_WANTED).add(listOfCardsForSort.get(i));
            }
        }
        LOGGER.info("Cartes jetées : " + cardsThatThePlayerDontWantAndThatThePlayerWant.get(DispatchState.CARDS_NOT_WANTED));
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
        for (Player p : players) {
            if (p.getHands().size() > this.getHands().size()) {
                return DispatchState.EXCHANGE_PLAYER;
            }
        }
        return DispatchState.EXCHANGE_DECK;
    }

    @Override
    public List<DistrictCard> chooseCardsToChange() {
        List<DistrictCard> districtCards = new ArrayList<>();
        for (DistrictCard districtCard : this.getHands()) {
            if (districtCard.getDistrictValue() >= 3) {
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
    public boolean wantToUseGraveyardEffect() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BotWeak botWeak = (BotWeak) o;
        return Objects.equals(random, botWeak.random);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), random);
    }
}
