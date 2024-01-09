package fr.cotedazur.univ.polytech.model.bot;

import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import fr.cotedazur.univ.polytech.model.card.Color;
import fr.cotedazur.univ.polytech.model.card.DistrictCard;
import fr.cotedazur.univ.polytech.model.card.DistrictCardComparator;
import fr.cotedazur.univ.polytech.model.deck.DistrictDeck;
import fr.cotedazur.univ.polytech.view.GameView;

import java.util.*;

public class BotWeak extends Player implements GameActions {

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
    public String startChoice(DistrictDeck districtDeck) {
        if(getPlayerRole() == CharacterCard.ARCHITECT) useRoleEffect(Optional.of(districtDeck), Optional.empty());
        discoverValidCard();
        if(getHands().isEmpty() || !validCards.isEmpty()){
            return drawCard(districtDeck);
        }
        return collectTwoGolds();
    }

    @Override
    public DistrictCard choiceToPutADistrict() {
        if(getPlayerRole() != CharacterCard.ARCHITECT)useRoleEffect(Optional.empty(),Optional.empty()); //Simple effects
        return putADistrict();
    }

    @Override
    public void useRoleEffect(Optional<DistrictDeck> districtDeck, Optional<GameView> view) {
        if (districtDeck.isEmpty() && view.isPresent())
            getPlayerRole().useEffect(this, view.get());
        else if (districtDeck.isPresent())
            getPlayerRole().useEffect(this, districtDeck.get());
        else getPlayerRole().useEffect(this);
    }

    @Override
    public int chooseCharacter(List<CharacterCard> characters) {
        discoverValidCard();
        validCards.sort(new DistrictCardComparator());

        if (isArchitectOptimal(characters)) {
            return characters.indexOf(CharacterCard.ARCHITECT);
        } else if (hasColoredCards()) {
            HashMap<Color, Integer> colorMap = createColorMap(characters);
            List<Map.Entry<Color,Integer>> entryList = new ArrayList<>(colorMap.entrySet());
            entryList.sort(Map.Entry.comparingByValue(Collections.reverseOrder()));

            if(!entryList.isEmpty()) {
                return getCharacterIndexByColor(characters, entryList.get(0).getKey());
            }

        }

        Random random = new Random();
        return random.nextInt(characters.size());
    }
    private boolean isArchitectOptimal(List<CharacterCard> characters) {
        return (validCards.size() >= 2 && characters.contains(CharacterCard.ARCHITECT) &&
                validCards.get(0).getDistrictValue() + validCards.get(1).getDistrictValue() <= getGolds()) ||
                (getHands().isEmpty() && characters.contains(CharacterCard.ARCHITECT));
    }
    private boolean hasColoredCards() {
        return countNumberOfSpecifiedColorCard(Color.YELLOW) >= 1 ||
                countNumberOfSpecifiedColorCard(Color.GREEN) >= 1 ||
                countNumberOfSpecifiedColorCard(Color.BLUE) >= 1;
    }


    public int countNumberOfSpecifiedColorCard(Color color){
        int count = 0;
        for(DistrictCard card : getBoard()){
            if(card.getDistrictColor() == color) count++;
        }
        return count;
    }

    private HashMap<Color, Integer> createColorMap(List<CharacterCard> characters) {
        HashMap<Color, Integer> hashMap = new HashMap<>();
        if (characters.contains(CharacterCard.KING)) hashMap.put(Color.YELLOW, countNumberOfSpecifiedColorCard(Color.YELLOW));
        if (characters.contains(CharacterCard.BISHOP)) hashMap.put(Color.BLUE, countNumberOfSpecifiedColorCard(Color.BLUE));
        if (characters.contains(CharacterCard.MERCHANT)) hashMap.put(Color.GREEN, countNumberOfSpecifiedColorCard(Color.GREEN));
        return hashMap;
    }
    private int getCharacterIndexByColor(List<CharacterCard> characters, Color color) {
        return switch (color) {
            case YELLOW -> characters.indexOf(CharacterCard.KING);
            case GREEN -> characters.indexOf(CharacterCard.MERCHANT);
            case BLUE -> characters.indexOf(CharacterCard.BISHOP);
            default -> throw new UnsupportedOperationException("la valeur de color est : " + color);
        };
    }
}
