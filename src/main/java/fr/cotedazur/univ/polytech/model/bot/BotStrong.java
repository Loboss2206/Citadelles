package fr.cotedazur.univ.polytech.model.bot;

import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import fr.cotedazur.univ.polytech.model.card.Color;
import fr.cotedazur.univ.polytech.model.card.DistrictCard;
import fr.cotedazur.univ.polytech.model.card.DistrictCardComparator;

import java.util.*;

public class BotStrong extends Player implements GameActions {


    private final Random random = new Random();

    public BotStrong() {
        super();
    }

    @Override
    public DistrictCard chooseHandCardToDiscard() {
        if(!getHands().isEmpty()) {
            ArrayList<DistrictCard> listOfCardsForSort = (ArrayList<DistrictCard>) getHands();
            DistrictCardComparator districtCardComparator = new DistrictCardComparator();
            listOfCardsForSort.sort(districtCardComparator);
            return listOfCardsForSort.get(0);
        }
        return null;
    }

    @Override
    public DistrictCard putADistrict() {
        discoverValidCard();
        //List des différentes couleurs sur le Terrain
        Set<Color> colorsOnBoard = colorInList(getBoard());
        Set<Color> colorsInHand = colorInList(getHands());
        if (!validCards.isEmpty()) {
            List<DistrictCard> purpleCard = new ArrayList<>();
            List<DistrictCard> colorNotOnBoard = new ArrayList<>();
            List<DistrictCard> cardsThatMatchWithRoleColor = new ArrayList<>();
            for (DistrictCard districtCard : validCards) {
                if (districtCard.getDistrictColor() == Color.PURPLE) {
                    purpleCard.add(districtCard);
                }
                if (!colorsOnBoard.contains(districtCard.getDistrictColor())) {
                    colorNotOnBoard.add(districtCard);
                }
                if(districtCard.getDistrictColor() == getPlayerRole().getCharacterColor()){
                    cardsThatMatchWithRoleColor.add(districtCard);
                }
            }
            if (!purpleCard.isEmpty()) return maxPrice(purpleCard);

            if (!cardsThatMatchWithRoleColor.isEmpty()) return maxPrice(cardsThatMatchWithRoleColor);

            if (!colorNotOnBoard.isEmpty()) return maxPrice(colorNotOnBoard);

            return maxPrice(validCards);
        }

        return null;
    }


    public Set<Color> colorInList(List<DistrictCard> districtCards) {
        Set<Color> listeUnique = new HashSet<>();
        for (DistrictCard districtCard : districtCards) {
            listeUnique.add(districtCard.getDistrictColor());
        }
        return listeUnique;
    }

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
        for (DistrictCard districtCard : validCards){
            if (!colorsOnBoard.contains(districtCard.getDistrictColor())){
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
            System.out.println("Volllllll:" + characterCards.get(3));
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
     * function that checks whether there is at least one color on the player's board that can be improved by a character
     */
    private boolean hasColoredCards() {
        return countNumberOfSpecifiedColorCard(Color.YELLOW) >= 1 ||
                countNumberOfSpecifiedColorCard(Color.GREEN) >= 1 ||
                countNumberOfSpecifiedColorCard(Color.BLUE) >= 1;
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
    private int getCharacterIndexByColor(List<CharacterCard> characters, Color color) {
        return switch (color) {
            case YELLOW -> characters.indexOf(CharacterCard.KING);
            case GREEN -> characters.indexOf(CharacterCard.MERCHANT);
            case BLUE -> characters.indexOf(CharacterCard.BISHOP);
            default -> throw new UnsupportedOperationException("la valeur de color est : " + color);
        };
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
    public void drawCard(Map<DispatchState, ArrayList<DistrictCard>> cardsThatThePlayerDontWantAndThatThePlayerWant, DistrictCard... cards) {
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
        for(DistrictCard districtCard : validCards){
            if(districtCard.getDistrictColor() == this.getPlayerRole().getCharacterColor() && beforePuttingADistrict){
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
}
