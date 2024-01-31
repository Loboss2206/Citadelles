package fr.cotedazur.univ.polytech.model.card;

import fr.cotedazur.univ.polytech.logger.LamaLogger;
import fr.cotedazur.univ.polytech.model.bot.Player;
import fr.cotedazur.univ.polytech.model.deck.Deck;
import fr.cotedazur.univ.polytech.model.golds.StackOfGolds;

import java.util.List;

/**
 * Represents a role card in the game.
 */

public enum CharacterCard {

    ASSASSIN("Assassin", 1, Color.GRAY, "Tuez un personnage"), THIEF("Voleur", 2, Color.GRAY, "Volez l'argent d'un autre joueur"), MAGICIAN("Magicien", 3, Color.GRAY, "Echangez vos cartes avec celles d'un autre joueur"), KING("Roi", 4, Color.YELLOW, "Prenez 1 pièce d'or pour chaque quartier jaune que vous possédez"), BISHOP("Évêque", 5, Color.BLUE, "Prenez 1 pièce d'or pour chaque quartier bleu que vous possédez. Les quartiers de l'Évêque ne peuvent pas être détruits par le Condottiere."), MERCHANT("Marchand", 6, Color.GREEN, "Prenez 1 pièce d'or pour chaque quartier vert dans votre quartier et une pièce d'or supplémentaire après une action"), ARCHITECT("Architecte", 7, Color.GRAY, "Piochez 2 cartes et possibilité de poser jusqu'à 3 bâtiments (si vous avez l'argent nécessaire)"), WARLORD("Condottiere", 8, Color.RED, "Détruisez un quartier en payant 1 pièce d'or de moins que son coût");

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(LamaLogger.class.getName());

    private final String characterName;
    private final int characterNumber;
    private final Color color;
    private final String characterEffect;

    /**
     * Constructs a role card with the specified name, number, color, and description.
     *
     * @param characterName   the name of the character
     * @param characterNumber the number of the character
     * @param color           the color of the character
     * @param characterEffect the description of the character's ability
     */
    private CharacterCard(String characterName, int characterNumber, Color color, String characterEffect) {
        this.characterName = characterName;
        this.characterNumber = characterNumber;
        this.color = color;
        this.characterEffect = characterEffect;
    }

    /**
     * Returns the name of the character.
     *
     * @return the name of the character
     */
    public String getCharacterName() {
        return characterName;
    }

    /**
     * Returns the number of the character.
     *
     * @return the number of the character
     */
    public int getCharacterNumber() {
        return characterNumber;
    }

    /**
     * Returns the color of the character.
     *
     * @return the color of the character
     */
    public Color getCharacterColor() {
        return color;
    }

    /**
     * Returns the description of the character's ability.
     *
     * @return the description of the character's ability
     */
    public String getCharacterEffect() {
        return characterEffect;
    }

    /**
     * Use the effect of the character card.
     *
     * @param player the player who use the effect
     */
    public void useEffect(Player player, StackOfGolds stackOfGolds) {
        LOGGER.info("Le joueur " + player.getName() + " (" + player.getPlayerRole().getCharacterName() + ") utilise son pouvoir");
        Color colorDistrictCard = null;
        if (player.hasCardOnTheBoard(DistrictCard.SCHOOL_OF_MAGIC)) {
            colorDistrictCard = player.chooseColorForSchoolOfMagic();
        }
        switch (this) {
            case KING -> earnGoldsFromDistricts(player, Color.YELLOW, stackOfGolds);
            case BISHOP -> earnGoldsFromDistricts(player, Color.BLUE, stackOfGolds);
            case MERCHANT -> earnGoldsFromDistricts(player, Color.GREEN, stackOfGolds);
            case WARLORD -> earnGoldsFromDistricts(player, Color.RED, stackOfGolds);
            default -> {
                // No default action
            }
        }
        if (colorDistrictCard == player.getPlayerRole().getCharacterColor() && (player.getPlayerRole() == CharacterCard.KING || player.getPlayerRole() == CharacterCard.BISHOP || player.getPlayerRole() == CharacterCard.MERCHANT || player.getPlayerRole() == CharacterCard.WARLORD))
            player.setGolds(player.getGolds() + stackOfGolds.takeAGold());
    }

    public void useEffectThief(Player playerThatUseEffect, Player stolenPlayer, boolean hasBeenStolen) {
        if (hasBeenStolen) {
            if (stolenPlayer.getPlayerRole() != CharacterCard.ASSASSIN && (playerThatUseEffect.getPlayerRole() == THIEF)) {
                playerThatUseEffect.setGolds(playerThatUseEffect.getGolds() + stolenPlayer.getGolds());
                stolenPlayer.setGolds(0);
                if (stolenPlayer.getPlayerRole() != null && playerThatUseEffect.getPlayerRole() != null) {
                    LOGGER.info("Le joueur " + playerThatUseEffect.getName() + " (" + playerThatUseEffect.getPlayerRole().getCharacterName() + ") a volé " + stolenPlayer.getName() + " (" + stolenPlayer.getPlayerRole().getCharacterName() + ")");
                    LOGGER.info("Le joueur " + playerThatUseEffect.getName() + " a maintenant " + playerThatUseEffect.getGolds() + " pièces d'or");
                }

            }
        } else {
            stolenPlayer.setHasBeenStolen(true);
        }
    }

    public void useEffectMagicianWithDeck(Player playerThatUseEffect, List<DistrictCard> cardsToRemove, Deck<DistrictCard> districtDeck) {
        int nbCards = cardsToRemove.size();
        while (!cardsToRemove.isEmpty()) {
            DistrictCard card = cardsToRemove.get(0);
            districtDeck.add(card);
            playerThatUseEffect.getHands().remove(card);
            cardsToRemove.remove(0);
        }
        for (int i = 0; i < nbCards; i++) {
            DistrictCard drawn = districtDeck.draw();
            playerThatUseEffect.getHands().add(drawn);
        }
    }

    public void useEffectMagicianWithPlayer(Player playerThatUseEffect, Player playerTargeted) {
        List<DistrictCard> temp = playerTargeted.getHands();
        playerTargeted.setHands(playerThatUseEffect.getHands());
        playerThatUseEffect.setHands(temp);
    }

    public void useEffectWarlord(Player playerThatUseEffect, Player playerToDestroy, DistrictCard districtToDestroy, Deck<DistrictCard> districtDiscardedDeck, StackOfGolds stackOfGolds) {
        if (playerThatUseEffect.getPlayerRole() == WARLORD) {
            playerToDestroy.getBoard().remove(districtToDestroy);
            playerThatUseEffect.setGolds(playerThatUseEffect.getGolds() - (districtToDestroy.getDistrictValue() - 1));
            stackOfGolds.addGoldsToStack((districtToDestroy.getDistrictValue() - 1));
            districtDiscardedDeck.add(districtToDestroy);
            LOGGER.info("Le joueur " + playerThatUseEffect.getName() + " (" + playerThatUseEffect.getPlayerRole().getCharacterName() + ") a détruit le quartier " + districtToDestroy.getDistrictName() + " du joueur " + playerToDestroy.getName());
            LOGGER.info("Le joueur " + playerThatUseEffect.getName() + " a maintenant " + playerThatUseEffect.getGolds() + " pièces d'or");
        }
    }

    /**
     * Use the effect of the character card.
     *
     * @param player the player who use the effect
     */
    public void useEffectArchitect(Player player, Deck<DistrictCard> districtDeck) {
        if (player.getPlayerRole() == CharacterCard.ARCHITECT) {
            for (int i = 0; i < 2; i++) {
                if (!districtDeck.isEmpty()) player.getHands().add(districtDeck.draw());
            }
            LOGGER.info("Le joueur " + player.getName() + " (" + player.getPlayerRole().getCharacterName() + ") a pioché 2 cartes");
        }
        player.setUsedEffect(player.getPlayerRole().getCharacterName().toUpperCase() + "_drawDistrict");
    }


    /**
     * Earn golds from the districts of the specified color.
     *
     * @param player the player who earn golds
     * @param color  the color of the districts
     */
    public void earnGoldsFromDistricts(Player player, Color color, StackOfGolds stackOfGolds) {
        for (DistrictCard district : player.getBoard()) {
            if (district.getDistrictColor() == color) {
                player.setGolds(player.getGolds() + stackOfGolds.takeAGold());
                LOGGER.info("Le joueur " + player.getName() + " a gagné 1 pièce d'or grâce à son quartier " + district.getDistrictName());
            }
        }
        LOGGER.info("Le joueur " + player.getName() + " a maintenant " + player.getGolds() + " pièces d'or");
    }

    public void useEffectAssassin(Player playerThatWantToUseEffect, Player targetPlayer) {
        if (playerThatWantToUseEffect.getPlayerRole() == ASSASSIN && !targetPlayer.isDead()) {
            targetPlayer.setDead(true);
            LOGGER.info("Le joueur " + targetPlayer.getName() + " est mort, il était " + targetPlayer.getPlayerRole().getCharacterName());
        }

    }
}
