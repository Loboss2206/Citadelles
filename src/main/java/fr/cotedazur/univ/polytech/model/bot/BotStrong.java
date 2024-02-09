package fr.cotedazur.univ.polytech.model.bot;

import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import fr.cotedazur.univ.polytech.model.card.Color;
import fr.cotedazur.univ.polytech.model.card.DistrictCard;
import fr.cotedazur.univ.polytech.model.card.DistrictCardComparator;

import java.util.*;

public class BotStrong extends CommonMethod implements GameActions {
    private final Random random = new Random();


    public DistrictCard putADistrict() {
        discoverValidCard();
        //List des différentes couleurs sur le Terrain
        Set<Color> colorsOnBoard = colorInList(getBoard());
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
                if (districtCard.getDistrictColor() == getPlayerRole().getCharacterColor()) {
                    cardsThatMatchWithRoleColor.add(districtCard);
                }
            }
            if (!purpleCard.isEmpty()) return maxPrice(purpleCard);
        }

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
    public Player selectMagicianTarget(List<Player> players) {
        Player highNbCards = players.get(0);
        for (Player p : players) {
            if (p.equals(this)) continue;
            if (p.getHands().size() >= highNbCards.getHands().size()) {
                highNbCards = p;
            }
        }
        return highNbCards;
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
