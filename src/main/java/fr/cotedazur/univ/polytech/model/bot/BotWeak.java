package fr.cotedazur.univ.polytech.model.bot;

import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import fr.cotedazur.univ.polytech.model.card.Color;
import fr.cotedazur.univ.polytech.model.card.DistrictCard;
import fr.cotedazur.univ.polytech.model.card.DistrictCardComparator;

import java.util.*;

public class BotWeak extends Player implements GameActions {

    private final Random random = new Random();

    public BotWeak() {
        super();
    }

    @Override
    public DistrictCard putADistrict() {
        discoverValidCard();
        if (!validCards.isEmpty()) {
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
            return DispatchState.TWOGOLDS;
        }
        if (getHands().isEmpty() || validCards.isEmpty() || getGolds() >= 15) {
            return DispatchState.DRAWCARD;
        }
        return DispatchState.TWOGOLDS;
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
        // we sort to know if we can put 2 times a district or more by comparing the first two value
        validCards.sort(new DistrictCardComparator());

        if (isArchitectOptimal(characters)) {
            LOGGER.info("Le joueur " + getName() + " prend l'architecte, car il est désigné comme optimal");
            return characters.indexOf(CharacterCard.ARCHITECT);
        } else if (hasColoredCards()) {
            Map<Color, Integer> colorMap = createColorMap(characters);
            List<Map.Entry<Color, Integer>> entryList = new ArrayList<>(colorMap.entrySet());
            entryList.sort(Map.Entry.comparingByValue(Collections.reverseOrder()));

            if (!entryList.isEmpty()) {
                LOGGER.info("Le joueur " + getName() + " prend le personnage " + entryList.get(0).getKey() + " car il a le plus de quartiers de cette couleur");
                return getCharacterIndexByColor(characters, entryList.get(0).getKey());
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

    /**
     * function that checks whether there is at least one color on the player's board that can be improved by a character
     */
    private boolean hasColoredCards() {
        return countNumberOfSpecifiedColorCard(Color.YELLOW) >= 1 ||
                countNumberOfSpecifiedColorCard(Color.GREEN) >= 1 ||
                countNumberOfSpecifiedColorCard(Color.BLUE) >= 1;
    }

    @Override
    public Color chooseColorForDistrictCard() {
        if (getPlayerRole() == CharacterCard.KING || getPlayerRole() == CharacterCard.BISHOP || getPlayerRole() == CharacterCard.MERCHANT || getPlayerRole() == CharacterCard.WARLORD) {
            return getPlayerRole().getCharacterColor();
        }
        return null;
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
                cardsThatThePlayerDontWantAndThatThePlayerWant.get(DispatchState.CARDSWANTED).add(listOfCardsForSort.get(i));
            } else {
                cardsThatThePlayerDontWantAndThatThePlayerWant.get(DispatchState.CARDSNOTWANTED).add(listOfCardsForSort.get(i));
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

    /**
     * function that count the number of district card on the board for a specific color
     */
    public int countNumberOfSpecifiedColorCard(Color color) {
        int count = 0;
        for (DistrictCard card : getBoard()) {
            if (card.getDistrictColor() == color) count++;
        }
        return count;
    }

    /**
     * Creates a HashMap that maps each specified character card to its corresponding color count.
     *
     * @param characters the characters available
     * @return A HashMap<Color, Integer> where the keys are colors associated with the specified character cards
     * and the values are the counts of cards of that color in the given list.
     */
    private Map<Color, Integer> createColorMap(List<CharacterCard> characters) {
        Map<Color, Integer> hashMap = new EnumMap<>(Color.class);
        if (characters.contains(CharacterCard.KING))
            hashMap.put(Color.YELLOW, countNumberOfSpecifiedColorCard(Color.YELLOW));
        if (characters.contains(CharacterCard.BISHOP))
            hashMap.put(Color.BLUE, countNumberOfSpecifiedColorCard(Color.BLUE));
        if (characters.contains(CharacterCard.MERCHANT))
            hashMap.put(Color.GREEN, countNumberOfSpecifiedColorCard(Color.GREEN));
        return hashMap;
    }

    /**
     * Retrieves the index of a specific character card in the given list based on its associated color.
     *
     * @param characters the characters available
     * @param color      The color associated with the character card to find.
     * @return The index of the character card associated with the specified color, or an exception if not found.
     */
    int getCharacterIndexByColor(List<CharacterCard> characters, Color color) {
        return switch (color) {
            case YELLOW -> characters.indexOf(CharacterCard.KING);
            case GREEN -> characters.indexOf(CharacterCard.MERCHANT);
            case BLUE -> characters.indexOf(CharacterCard.BISHOP);
            default -> throw new UnsupportedOperationException("la valeur de color est : " + color);
        };
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
    public List<DistrictCard> chooseCardsToChange() {
        List<DistrictCard> districtCards = new ArrayList<>();
        for (DistrictCard d : this.getHands()) {
            if (d.getDistrictValue() >= 3) {
                districtCards.add(d);
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
    public boolean chooseUseGraveyardEffect() {
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
