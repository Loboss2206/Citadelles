package fr.cotedazur.univ.polytech.controller;

import fr.cotedazur.univ.polytech.logger.LamaLogger;
import fr.cotedazur.univ.polytech.model.bot.DispatchState;
import fr.cotedazur.univ.polytech.model.bot.Player;
import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import fr.cotedazur.univ.polytech.model.card.Color;
import fr.cotedazur.univ.polytech.model.card.DistrictCard;
import fr.cotedazur.univ.polytech.model.card.PurpleEffectState;
import fr.cotedazur.univ.polytech.model.deck.Deck;
import fr.cotedazur.univ.polytech.model.golds.StackOfGolds;
import fr.cotedazur.univ.polytech.view.GameView;

import java.util.*;

public class EffectController {
    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(LamaLogger.class.getName());

    private final Map<DispatchState, Boolean> isEffectUsed = new EnumMap<>(DispatchState.class);

    private Player playerWhoStole;

    private GameView view;

    private StackOfGolds stackOfGolds;

    public EffectController() {
        isEffectUsed.put(DispatchState.EARNDISTRICT_BISHOP, false);
        isEffectUsed.put(DispatchState.EARNDISTRICT_WARLORD, false);
        isEffectUsed.put(DispatchState.EARNDISTRICT_KING, false);
        isEffectUsed.put(DispatchState.EARNDISTRICT_MERCHANT, false);
        isEffectUsed.put(DispatchState.STEAL, false);
        isEffectUsed.put(DispatchState.DESTROY, false);
        isEffectUsed.put(DispatchState.KILL, false);
        isEffectUsed.put(DispatchState.EXCHANGE, false);
    }

    public EffectController(GameView view, StackOfGolds stackOfGolds) {
        this();
        this.view = view;
        this.stackOfGolds = stackOfGolds;
    }

    public Player getPlayerWhoStole() {
        return playerWhoStole;
    }

    public Map<DispatchState, Boolean> getIsEffectUsed() {
        return isEffectUsed;
    }

    /**
     * @param characterCards           the list of the character cards
     * @param characterCardsToGetRidOf the list of the character cards to get rid of
     * @return the list of the character cards without the character cards to get rid of
     */
    public List<CharacterCard> getRidOfASetOfCharacterCard(List<CharacterCard> characterCards, List<CharacterCard> characterCardsToGetRidOf) {
        ArrayList<CharacterCard> newList = new ArrayList<>();
        for (CharacterCard characterCard : characterCards) {
            if (!characterCardsToGetRidOf.contains(characterCard)) newList.add(characterCard);
        }
        LOGGER.info("La liste des personnages sans les personnages à éliminer est: " + newList);
        return newList;
    }

    // Methods to get the information that the players must have

    /**
     * @return the list of role that the player can choose to kill
     */
    public List<CharacterCard> rolesNeededForAssassinEffect() {
        return getRidOfASetOfCharacterCard(Arrays.asList(CharacterCard.values()), List.of(CharacterCard.ASSASSIN));
    }

    /**
     * @return the list of role that the player can choose to steal
     */
    public List<CharacterCard> rolesNeededForThiefEffect() {
        return getRidOfASetOfCharacterCard(Arrays.asList(CharacterCard.values()), List.of(CharacterCard.ASSASSIN, CharacterCard.THIEF));
    }

    /**
     * @param players             the list of the players
     * @param playerThatUseEffect the warlord
     * @return the list of the players that can be destroyed by the warlord
     */
    public List<Player> playersNeededForWarlordEffect(List<Player> players, Player playerThatUseEffect) {
        ArrayList<Player> newList = new ArrayList<>();
        for (Player player : players) {
            if (player.playerHasADestroyableDistrict(playerThatUseEffect)) {
                newList.add(this.playerCopy(player, playerThatUseEffect));
            }
        }
        LOGGER.info("La liste des joueurs qui peuvent être affectés par l'effet du voleur est: " + newList);
        return newList;
    }

    /**
     * get players without their cards and the others sensibles information
     *
     * @param players             the list of the players
     * @param playerThatUseEffect the player that use the effect
     * @return the list of the players without their cards and the others sensibles information
     */
    public List<Player> playerNeededWithoutSensibleInformation(List<Player> players, Player playerThatUseEffect) {
        ArrayList<Player> newList = new ArrayList<>();
        for (Player player : players) {
            if (player != playerThatUseEffect) {
                newList.add(this.playerCopy(player, playerThatUseEffect));
            }
        }
        LOGGER.info("La liste des joueurs qui peuvent être affectés par l'effet de l'assassin est: " + newList);
        return newList;
    }

    /**
     * @param playerThatUseEffect the warlord
     * @param playerToDestroy     the player that the warlord want to destroy
     * @return the list of the districts that can be destroyed by the warlord for the playerToDestroy
     */
    public List<DistrictCard> districtNeededForWarlordEffect(Player playerThatUseEffect, Player playerToDestroy) {
        ArrayList<DistrictCard> newList = new ArrayList<>();
        for (DistrictCard districtCard : playerToDestroy.getBoard()) {
            if (districtCard.isDestroyableDistrict(playerThatUseEffect.getGolds())) newList.add(districtCard);
            if (districtCard == DistrictCard.KEEP)
                view.printPurpleEffect(playerToDestroy, PurpleEffectState.KEEP_EFFECT);
        }
        LOGGER.info("La liste des quartiers qui peuvent être détruits par le joueur " + playerThatUseEffect.getName() + " est: " + newList);
        return newList;
    }

    /**
     * Copy the player without the sensible information
     *
     * @param playerCopy          the player to copy
     * @param playerThatUseEffect the player that use the effect
     * @return the copy of the player
     */
    private Player playerCopy(Player playerCopy, Player playerThatUseEffect) {
        Player copyPlayer = playerCopy.copy();
        if (playerCopy.getPlayerRole() != null && playerCopy.getPlayerRole().getCharacterNumber() < playerThatUseEffect.getPlayerRole().getCharacterNumber() && !playerCopy.isDead()) {
            copyPlayer.setPlayerRole(playerCopy.getPlayerRole());
            LOGGER.info("Le role du joueur " + playerCopy.getName() + " (" + playerCopy.getPlayerRole().getCharacterName() + ") a été copié");
        }
        LOGGER.info("Le joueur " + playerCopy.getName() + " (" + (playerCopy.getPlayerRole() != null ? playerCopy.getPlayerRole().getCharacterName() : "non connus") + ") a été copié");
        return copyPlayer;
    }

    // Methods to call the methods needed according to the role of the player

    /**
     * Call the methods needed according to the role of the player
     *
     * @param playerThatWantToUseEffect the player that want to use the effect
     * @param players                   the list of the players
     * @param districtDiscardDeck       the discard deck of the districts
     * @param districtDeck              the deck of the districts
     */
    public void playerWantToUseEffect(Player playerThatWantToUseEffect, List<Player> players, Deck<DistrictCard> districtDiscardDeck, Deck<DistrictCard> districtDeck) {
        LOGGER.info("Le joueur " + playerThatWantToUseEffect.getName() + " (" + playerThatWantToUseEffect.getPlayerRole().getCharacterName() + ") veut utiliser son effet");

        switch (playerThatWantToUseEffect.getPlayerRole()) {
            case ASSASSIN -> playerWantToUseEffectAssassin(playerThatWantToUseEffect, players);
            case THIEF -> playerWantToUseEffectThief(playerThatWantToUseEffect, players);
            case MAGICIAN -> playerWantToUseEffectMagician(playerThatWantToUseEffect, players, districtDeck);
            case KING -> playerWantToUseEffectKing(playerThatWantToUseEffect);
            case BISHOP -> playerWantToUseEffectBishop(playerThatWantToUseEffect);
            case MERCHANT -> playerWantToUseEffectMerchant(playerThatWantToUseEffect);
            case WARLORD -> playerWantToUseEffectWarlord(playerThatWantToUseEffect, players, districtDiscardDeck);
            default ->
                    throw new IllegalStateException("Unexpected value: " + playerThatWantToUseEffect.getPlayerRole());
        }
    }

    /**
     * Call the methods needed for the assassin
     *
     * @param playerThatWantToUseEffect the player that want to use the effect
     * @param players                   the list of the players
     */
    public void playerWantToUseEffectAssassin(Player playerThatWantToUseEffect, List<Player> players) {
        if (Boolean.FALSE.equals(this.getIsEffectUsed().get(DispatchState.KILL))) {
            CharacterCard roleKilled = playerThatWantToUseEffect.selectWhoWillBeAffectedByAssassinEffect(this.playerNeededWithoutSensibleInformation(players, playerThatWantToUseEffect), this.rolesNeededForAssassinEffect());
            LOGGER.info("Le joueur " + playerThatWantToUseEffect.getName() + " (" + playerThatWantToUseEffect.getPlayerRole().getCharacterName() + ") a choisi le personnage " + roleKilled.getCharacterName() + " pour être éliminé");
            if (roleKilled != CharacterCard.ASSASSIN) {
                for (Player playerAffected : players) {
                    //Store the role that has been killed by the assassin for all the players
                    playerAffected.setRoleKilledByAssassin(roleKilled);

                    //Kill the player
                    if (playerAffected.getPlayerRole() == roleKilled) {
                        playerThatWantToUseEffect.getPlayerRole().useEffectAssassin(playerThatWantToUseEffect, playerAffected);
                    }
                }
                this.getIsEffectUsed().put(DispatchState.KILL, true);
                if (view != null) view.killPlayer(roleKilled);
            }
        } else {
            LOGGER.info("Le joueur " + playerThatWantToUseEffect.getName() + " ne peut pas utiliser l'effet de l'assassin");
        }
    }

    /**
     * Call the methods needed for the thief
     *
     * @param playerThatWantToUseEffect the player that want to use the effect
     * @param players                   the list of the players
     */
    public void playerWantToUseEffectThief(Player playerThatWantToUseEffect, List<Player> players) {
        if (Boolean.FALSE.equals(this.getIsEffectUsed().get(DispatchState.STEAL))) {
            CharacterCard roleStolen = playerThatWantToUseEffect.selectWhoWillBeAffectedByThiefEffect(this.playerNeededWithoutSensibleInformation(players, playerThatWantToUseEffect), this.rolesNeededForThiefEffect());
            LOGGER.info("Le joueur " + playerThatWantToUseEffect.getName() + " (" + playerThatWantToUseEffect.getPlayerRole().getCharacterName() + ") a choisi le personnage " + roleStolen.getCharacterName() + " pour être volé");
            if (roleStolen != CharacterCard.ASSASSIN) {
                for (Player player1 : players) {
                    if (player1.getPlayerRole() == roleStolen) {
                        playerThatWantToUseEffect.getPlayerRole().useEffectThief(playerThatWantToUseEffect, player1, false);
                        playerWhoStole = playerThatWantToUseEffect;
                    }
                }
                this.getIsEffectUsed().put(DispatchState.STEAL, true);
                if (view != null) view.stolenPlayer(roleStolen);
            }
        } else {
            LOGGER.info("Le joueur " + playerThatWantToUseEffect.getName() + " ne peut pas utiliser l'effet du voleur");
        }
    }

    /**
     * Call the methods needed for the magician
     *
     * @param playerThatWantToUseEffect the player that want to use the effect
     * @param players                   the list of the players
     * @param districtDeck              the deck of the districts
     */
    private void playerWantToUseEffectMagician(Player playerThatWantToUseEffect, List<Player> players, Deck<DistrictCard> districtDeck) {
        if (Boolean.FALSE.equals(this.getIsEffectUsed().get(DispatchState.EXCHANGE))) {
            DispatchState action = playerThatWantToUseEffect.whichMagicianEffect(players);
            if (action.equals(DispatchState.EXCHANGE_DECK)) {
                List<DistrictCard> cardToRemove = playerThatWantToUseEffect.chooseCardsToChange();
                view.exchangeDeckCard(playerThatWantToUseEffect, cardToRemove);
                if (!cardToRemove.isEmpty()) {
                    playerThatWantToUseEffect.getPlayerRole().useEffectMagicianWithDeck(playerThatWantToUseEffect, cardToRemove, districtDeck);
                }
            } else if (action.equals(DispatchState.EXCHANGE_PLAYER)) {
                Player playerTargeted = playerThatWantToUseEffect.selectMagicianTarget(players);
                playerThatWantToUseEffect.getPlayerRole().useEffectMagicianWithPlayer(playerThatWantToUseEffect, playerTargeted);
                view.exchangePlayerCard(playerThatWantToUseEffect, playerTargeted);
            } else {
                throw new UnsupportedOperationException("L'action demandé est " + action);
            }
        }
    }

    /**
     * Call the methods needed for the king
     *
     * @param playerThatWantToUseEffect the player that want to use the effect
     */
    public void playerWantToUseEffectKing(Player playerThatWantToUseEffect) {
        if (Boolean.FALSE.equals(this.getIsEffectUsed().get(DispatchState.EARNDISTRICT_KING))) {
            int golds = playerThatWantToUseEffect.getGolds();
            playerThatWantToUseEffect.getPlayerRole().useEffect(playerThatWantToUseEffect, stackOfGolds, verifyPresenceOfSchoolOfMagicCard(playerThatWantToUseEffect));
            view.printCharacterGetGolds(playerThatWantToUseEffect, playerThatWantToUseEffect.getPlayerRole().getCharacterColor(), playerThatWantToUseEffect.getGolds() - golds);
            this.getIsEffectUsed().put(DispatchState.EARNDISTRICT_KING, true);
        } else {
            LOGGER.info("Le joueur " + playerThatWantToUseEffect.getName() + " ne peut pas utiliser l'effet du roi");
        }
    }

    /**
     * Call the methods needed for the bishop
     *
     * @param playerThatWantToUseEffect the player that want to use the effect
     */
    public void playerWantToUseEffectBishop(Player playerThatWantToUseEffect) {
        if (Boolean.FALSE.equals(this.getIsEffectUsed().get(DispatchState.EARNDISTRICT_BISHOP))) {
            int golds = playerThatWantToUseEffect.getGolds();
            playerThatWantToUseEffect.getPlayerRole().useEffect(playerThatWantToUseEffect, stackOfGolds, verifyPresenceOfSchoolOfMagicCard(playerThatWantToUseEffect));
            view.printCharacterGetGolds(playerThatWantToUseEffect, playerThatWantToUseEffect.getPlayerRole().getCharacterColor(), playerThatWantToUseEffect.getGolds() - golds);
            this.getIsEffectUsed().put(DispatchState.EARNDISTRICT_BISHOP, true);
        } else {
            LOGGER.info("Le joueur " + playerThatWantToUseEffect.getName() + " ne peut pas utiliser l'effet de l'évêque");
        }
    }

    /**
     * Call the methods needed for the merchant
     *
     * @param playerThatWantToUseEffect the player that want to use the effect
     */
    public void playerWantToUseEffectMerchant(Player playerThatWantToUseEffect) {
        if (Boolean.FALSE.equals(this.getIsEffectUsed().get(DispatchState.EARNDISTRICT_MERCHANT))) {
            int golds = playerThatWantToUseEffect.getGolds();
            playerThatWantToUseEffect.getPlayerRole().useEffect(playerThatWantToUseEffect, stackOfGolds, verifyPresenceOfSchoolOfMagicCard(playerThatWantToUseEffect));
            view.printCharacterGetGolds(playerThatWantToUseEffect, playerThatWantToUseEffect.getPlayerRole().getCharacterColor(), playerThatWantToUseEffect.getGolds() - golds);
            this.getIsEffectUsed().put(DispatchState.EARNDISTRICT_MERCHANT, true);
        } else {
            LOGGER.info("Le joueur " + playerThatWantToUseEffect.getName() + " ne peut pas utiliser l'effet du marchand");
        }
    }

    /**
     * Verify the presence of the school of magic card
     *
     * @param player the player
     * @return the color of the district card for this turn
     */
    public Color verifyPresenceOfSchoolOfMagicCard(Player player) {
        Color colorDistrictCard = null;
        if (player.hasCardOnTheBoard(DistrictCard.SCHOOL_OF_MAGIC)) {
            colorDistrictCard = player.chooseColorForSchoolOfMagic();
            view.printPurpleEffect(player, colorDistrictCard, PurpleEffectState.SCHOOL_OF_MAGIC_EFFECT);
        }
        return colorDistrictCard;
    }

    /**
     * Call the methods needed for the warlord
     *
     * @param playerThatWantToUseEffect the player that want to use the effect
     * @param players                   the list of the players
     * @param districtDiscardedDeck     the discard deck of the districts
     */
    public void playerWantToUseEffectWarlord(Player playerThatWantToUseEffect, List<Player> players, Deck<DistrictCard> districtDiscardedDeck) {
        // Recover the effect that the warlord will use

        DispatchState effect = warlordChooseEffectToUse(playerThatWantToUseEffect, players);

        LOGGER.info("Le joueur " + playerThatWantToUseEffect.getName() + " (" + playerThatWantToUseEffect.getPlayerRole().getCharacterName() + ") veut utiliser son effet");
        if (effect == null) return;

        if (effect.equals(DispatchState.EARNDISTRICT_WARLORD)) {
            // Case where the warlord earn golds for each red district he has
            int golds = playerThatWantToUseEffect.getGolds();
            playerThatWantToUseEffect.getPlayerRole().useEffect(playerThatWantToUseEffect, stackOfGolds, verifyPresenceOfSchoolOfMagicCard(playerThatWantToUseEffect));
            view.printCharacterGetGolds(playerThatWantToUseEffect, playerThatWantToUseEffect.getPlayerRole().getCharacterColor(), playerThatWantToUseEffect.getGolds() - golds);
            this.getIsEffectUsed().put(DispatchState.EARNDISTRICT_WARLORD, true);
        } else {
            // Case where the warlord destroy a district
            letTheWarlordDestroyADistrict(playerThatWantToUseEffect, players, districtDiscardedDeck);
        }
    }

    /**
     * Call the methods needed for the warlord to destroy a district
     *
     * @param playerThatWantToUseEffect the player that want to use the effect
     * @param players                   the list of the players
     * @param districtDiscardedDeck     the discard deck of the districts
     */
    private void letTheWarlordDestroyADistrict(Player playerThatWantToUseEffect, List<Player> players, Deck<DistrictCard> districtDiscardedDeck) {
        // Create the list of the players that can be destroyed by the warlord
        List<Player> playersNeeded = playersNeededForWarlordEffect(players, playerThatWantToUseEffect);

        if (playersNeeded.isEmpty()) return;

        Player playerToDestroy = playerThatWantToUseEffect.choosePlayerToDestroy(playersNeeded);
        LOGGER.info(String.format("Le joueur %s (%s) a choisi le joueur %s pour être détruit", playerThatWantToUseEffect.getName(), playerThatWantToUseEffect.getPlayerRole().getCharacterName(), playerToDestroy != null ? playerToDestroy.getName() : "null"));
        if (playerToDestroy == null) return;

        // If the bot choose a player to destroy, we create the list of the districts that can be destroyed by the warlord for the playerToDestroy
        List<DistrictCard> districtsNeeded = districtNeededForWarlordEffect(playerThatWantToUseEffect, playerToDestroy);

        if (districtsNeeded.isEmpty()) return;

        DistrictCard districtToDestroy = playerThatWantToUseEffect.chooseDistrictToDestroy(playerToDestroy, districtsNeeded);
        LOGGER.info(String.format("Le joueur %s (%s) a choisi le quartier %s du joueur %s pour être détruit", playerThatWantToUseEffect.getName(), playerThatWantToUseEffect.getPlayerRole().getCharacterName(), districtToDestroy.getDistrictName(), playerToDestroy.getName()));

        // If the bot choose a district to destroy, we destroy it and display the district that was destroyed and the player who had the district
        playerThatWantToUseEffect.getPlayerRole().useEffectWarlord(playerThatWantToUseEffect, playerToDestroy, districtToDestroy, districtDiscardedDeck, this.stackOfGolds);
        this.getIsEffectUsed().put(DispatchState.DESTROY, true);
        if (view != null && playerToDestroy.getPlayerRole() != null) {
            assert districtToDestroy != null;
            view.printDistrictDestroyed(playerThatWantToUseEffect, playerToDestroy, districtToDestroy);
        }

        // If a bot has the graveyard, it can retrieve it by paying 1 gold
        useGraveyard(players, districtToDestroy);
    }

    /**
     * player choose to use the effect of the graveyard for the player who has it
     *
     * @param players           the list of the players
     * @param districtToDestroy district which has been destroyed
     */
    private void useGraveyard(List<Player> players, DistrictCard districtToDestroy) {
        Player playerHasGraveyard = someoneHasGraveyard(players);
        if (playerHasGraveyard != null && playerHasGraveyard.wantToUseGraveyardEffect() && playerHasGraveyard.getGolds() >= 1) {
            view.printPurpleEffect(playerHasGraveyard, districtToDestroy, PurpleEffectState.GRAVEYARD_EFFECT);
            playerHasGraveyard.setGolds(playerHasGraveyard.getGolds() - 1);
            stackOfGolds.addGoldsToStack(1);
            playerHasGraveyard.addCardToHand(districtToDestroy);
            view.printGraveyardEffect(playerHasGraveyard, districtToDestroy);
        }
    }

    /**
     * Checks if any player in the given list has the Graveyard district in their board.
     *
     * @param players A list of players to check for the Graveyard district.
     * @return The first player found with the Graveyard district, or null if none have it.
     */
    public Player someoneHasGraveyard(List<Player> players) {
        for (Player player : players) {
            if (player.getBoard().contains(DistrictCard.GRAVEYARD)) {
                return player;
            }
        }
        return null;
    }

    /**
     * Determines the warlord effect to be used based on the current state and available options.
     *
     * @param playerThatWantToUseEffect The player who wants to use the warlord effect.
     * @param players                   A list of players in the game.
     * @return The chosen warlord effect represented by a DispatchState enum.
     */
    private DispatchState warlordChooseEffectToUse(Player playerThatWantToUseEffect, List<Player> players) {
        DispatchState warlordEffect = null;

        if (Boolean.FALSE.equals(this.getIsEffectUsed().get(DispatchState.DESTROY))) {
            // Case where the warlord can destroy a district
            if (Boolean.FALSE.equals(this.getIsEffectUsed().get(DispatchState.EARNDISTRICT_WARLORD)))
                // Case where the warlord can earn a district
                warlordEffect = playerThatWantToUseEffect.whichWarlordEffect(players);
            else
                // Case where the warlord can't earn a district
                warlordEffect = DispatchState.DESTROY;
        } else {
            // Case where the warlord can't destroy a district
            if (Boolean.FALSE.equals(this.getIsEffectUsed().get(DispatchState.EARNDISTRICT_WARLORD)))
                // Case where the warlord can earn a district
                warlordEffect = DispatchState.EARNDISTRICT_WARLORD;
        }

        return warlordEffect;
    }
}

