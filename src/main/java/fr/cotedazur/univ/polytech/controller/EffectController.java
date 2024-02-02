package fr.cotedazur.univ.polytech.controller;

import fr.cotedazur.univ.polytech.logger.LamaLogger;
import fr.cotedazur.univ.polytech.model.bot.DispatchState;
import fr.cotedazur.univ.polytech.model.bot.Player;
import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import fr.cotedazur.univ.polytech.model.card.DistrictCard;
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

    public void setView(GameView view) {
        this.view = view;
    }

    public void setStackOfCoins(StackOfGolds stackOfGolds) {
        this.stackOfGolds = stackOfGolds;
    }

    /**
     * @param players the list of the players
     * @param playerThatUseEffect the warlord
     * @return the list of the players that can be destroyed by the warlord
     */
    public List<Player> playerNeededForWarlordEffect(List<Player> players, Player playerThatUseEffect) {
        ArrayList<Player> newList = new ArrayList<>();
        for (Player player : players) {
            if (player.playerHasADestroyableDistrict(playerThatUseEffect)) {
                newList.add(this.playerCopy(player, playerThatUseEffect));
            }
        }
        LOGGER.info("La liste des joueurs qui peuvent être affectés par l'effet du voleur est: " + newList);
        return newList;
    }

    public List<CharacterCard> roleNeededForThiefEffect() {
        return getRidOfASetOfCharacterCard(Arrays.asList(CharacterCard.values()), List.of(CharacterCard.ASSASSIN, CharacterCard.THIEF));
    }

    public List<CharacterCard> roleNeededForAssassinEffect() {
        return getRidOfASetOfCharacterCard(Arrays.asList(CharacterCard.values()), List.of(CharacterCard.ASSASSIN));
    }

    public List<Player> playerNeededForEffectWithoutSensibleInformationForAssassin(List<Player> players, Player playerThatUseEffect) {
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
        }
        LOGGER.info("La liste des quartiers qui peuvent être détruits par le joueur " + playerThatUseEffect.getName() + " est: " + newList);
        return newList;
    }

    private Player playerCopy(Player playerCopy, Player playerThatUseEffect) {
        Player copyPlayer = playerCopy.copy();
        if (playerCopy.getPlayerRole().getCharacterNumber() < playerThatUseEffect.getPlayerRole().getCharacterNumber() && !playerCopy.isDead()) {
            copyPlayer.setPlayerRole(playerCopy.getPlayerRole());
            LOGGER.info("Le role du joueur " + playerCopy.getName() + " (" + playerCopy.getPlayerRole().getCharacterName() + ") a été copié");
        }
        LOGGER.info("Le joueur " + playerCopy.getName() + " (" + playerCopy.getPlayerRole().getCharacterName() + ") a été copié");
        return copyPlayer;
    }

    public Map<DispatchState, Boolean> getIsEffectUsed() {
        return isEffectUsed;
    }

    public List<CharacterCard> getRidOfASetOfCharacterCard(List<CharacterCard> characterCards, List<CharacterCard> characterCardsToGetRidOf) {
        ArrayList<CharacterCard> newList = new ArrayList<>();
        for (CharacterCard characterCard : characterCards) {
            if (!characterCardsToGetRidOf.contains(characterCard)) newList.add(characterCard);
        }
        LOGGER.info("La liste des personnages sans les personnages à éliminer est: " + newList);
        return newList;
    }


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

    public void playerWantToUseEffectAssassin(Player playerThatWantToUseEffect, List<Player> players) {
        if (Boolean.FALSE.equals(this.getIsEffectUsed().get(DispatchState.KILL))) {
            CharacterCard roleKilled = playerThatWantToUseEffect.selectWhoWillBeAffectedByAssassinEffect(this.playerNeededForEffectWithoutSensibleInformationForAssassin(players, playerThatWantToUseEffect), this.roleNeededForAssassinEffect());
            LOGGER.info("Le joueur " + playerThatWantToUseEffect.getName() + " (" + playerThatWantToUseEffect.getPlayerRole().getCharacterName() + ") a choisi le personnage " + roleKilled.getCharacterName() + " pour être éliminé");
            if (roleKilled != CharacterCard.ASSASSIN) {
                for (Player playerAffected : players) {
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

    public void playerWantToUseEffectThief(Player playerThatWantToUseEffect, List<Player> players) {
        if (Boolean.FALSE.equals(this.getIsEffectUsed().get(DispatchState.STEAL))) {
            CharacterCard roleStolen = playerThatWantToUseEffect.selectWhoWillBeAffectedByThiefEffect(players, this.roleNeededForThiefEffect());
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

    private void playerWantToUseEffectMagician(Player playerThatWantToUseEffect, List<Player> players, Deck<DistrictCard> districtDeck) {
        if (Boolean.FALSE.equals(this.getIsEffectUsed().get(DispatchState.EXCHANGE))) {
            DispatchState action = playerThatWantToUseEffect.whichMagicianEffect(players);
            if (action.equals(DispatchState.EXCHANGEDECK)) {
                List<DistrictCard> cardToRemove = playerThatWantToUseEffect.chooseCardsToChange();
                view.exchangeDeckCard(playerThatWantToUseEffect, cardToRemove);
                if (!cardToRemove.isEmpty()) {
                    playerThatWantToUseEffect.getPlayerRole().useEffectMagicianWithDeck(playerThatWantToUseEffect, cardToRemove, districtDeck);
                }
            } else if (action.equals(DispatchState.EXCHANGEPLAYER)) {
                Player playerTargeted = playerThatWantToUseEffect.selectMagicianTarget(players);
                playerThatWantToUseEffect.getPlayerRole().useEffectMagicianWithPlayer(playerThatWantToUseEffect, playerTargeted);
                view.exchangePlayerCard(playerThatWantToUseEffect, playerTargeted);
            } else {
                throw new UnsupportedOperationException("L'action demandé est " + action);
            }
        }
    }

    public void playerWantToUseEffectKing(Player playerThatWantToUseEffect) {
        if (Boolean.FALSE.equals(this.getIsEffectUsed().get(DispatchState.EARNDISTRICT_KING))) {
            int golds = playerThatWantToUseEffect.getGolds();
            playerThatWantToUseEffect.getPlayerRole().useEffect(playerThatWantToUseEffect, stackOfGolds);
            view.printCharacterGetGolds(playerThatWantToUseEffect, playerThatWantToUseEffect.getPlayerRole().getCharacterColor(), playerThatWantToUseEffect.getGolds() - golds);
            this.getIsEffectUsed().put(DispatchState.EARNDISTRICT_KING, true);
        } else {
            LOGGER.info("Le joueur " + playerThatWantToUseEffect.getName() + " ne peut pas utiliser l'effet du roi");
        }
    }

    public void playerWantToUseEffectBishop(Player playerThatWantToUseEffect) {
        if (Boolean.FALSE.equals(this.getIsEffectUsed().get(DispatchState.EARNDISTRICT_BISHOP))) {
            int golds = playerThatWantToUseEffect.getGolds();
            playerThatWantToUseEffect.getPlayerRole().useEffect(playerThatWantToUseEffect, stackOfGolds);
            view.printCharacterGetGolds(playerThatWantToUseEffect, playerThatWantToUseEffect.getPlayerRole().getCharacterColor(), playerThatWantToUseEffect.getGolds() - golds);
            this.getIsEffectUsed().put(DispatchState.EARNDISTRICT_BISHOP, true);
        } else {
            LOGGER.info("Le joueur " + playerThatWantToUseEffect.getName() + " ne peut pas utiliser l'effet de l'évêque");
        }
    }

    public void playerWantToUseEffectMerchant(Player playerThatWantToUseEffect) {
        if (Boolean.FALSE.equals(this.getIsEffectUsed().get(DispatchState.EARNDISTRICT_MERCHANT))) {
            int golds = playerThatWantToUseEffect.getGolds();
            playerThatWantToUseEffect.getPlayerRole().useEffect(playerThatWantToUseEffect, stackOfGolds);
            view.printCharacterGetGolds(playerThatWantToUseEffect, playerThatWantToUseEffect.getPlayerRole().getCharacterColor(), playerThatWantToUseEffect.getGolds() - golds);
            this.getIsEffectUsed().put(DispatchState.EARNDISTRICT_MERCHANT, true);
        } else {
            LOGGER.info("Le joueur " + playerThatWantToUseEffect.getName() + " ne peut pas utiliser l'effet du marchand");
        }
    }

    public void playerWantToUseEffectWarlord(Player player, List<Player> players, Deck<DistrictCard> deck) {
        // Recover the effect that the warlord will use

        DispatchState effect = warlordChooseEffectToUse(player, players);

        LOGGER.info(String.format("Le joueur %s (%s) a choisi l'effet %s pour le warlord", player.getName(), player.getPlayerRole().getCharacterName(), effect));

        if (effect == null) return;

        if (effect.equals(DispatchState.EARNDISTRICT_WARLORD)) {
            // Case where the warlord earn golds for each red district he has
            int golds = player.getGolds();
            player.getPlayerRole().useEffect(player, stackOfGolds);
            view.printCharacterGetGolds(player, player.getPlayerRole().getCharacterColor(), player.getGolds() - golds);
            this.getIsEffectUsed().put(DispatchState.EARNDISTRICT_WARLORD, true);
        } else {
            // Case where the warlord destroy a district

            // Create the list of the players that can be destroyed by the warlord
            List<Player> playersNeeded = playerNeededForWarlordEffect(players, player);

            if (playersNeeded.isEmpty()) return;

            Player playerToDestroy = player.choosePlayerToDestroy(playersNeeded);
            LOGGER.info(String.format("Le joueur %s (%s) a choisi le joueur %s pour être détruit", player.getName(), player.getPlayerRole().getCharacterName(), playerToDestroy != null ? playerToDestroy.getName() : "null"));

            if (playerToDestroy == null) return;

            // If the bot choose a player to destroy, we create the list of the districts that can be destroyed by the warlord for the playerToDestroy
            List<DistrictCard> districtsNeeded = districtNeededForWarlordEffect(player, playerToDestroy);

            if (districtsNeeded.isEmpty()) return;

            DistrictCard districtToDestroy = player.chooseDistrictToDestroy(playerToDestroy, districtsNeeded);
            LOGGER.info(String.format("Le joueur %s (%s) a choisi le quartier %s du joueur %s pour être détruit", player.getName(), player.getPlayerRole().getCharacterName(), districtToDestroy.getDistrictName(), playerToDestroy.getName()));

            // If the bot choose a district to destroy, we destroy it and display the district that was destroyed and the player who had the district
            player.getPlayerRole().useEffectWarlord(player, playerToDestroy, districtToDestroy, deck, this.stackOfGolds);
            this.getIsEffectUsed().put(DispatchState.DESTROY, true);
            if (view != null && playerToDestroy.getPlayerRole() != null) {
                view.printDistrictDestroyed(player, playerToDestroy, districtToDestroy);
            }

            // If a bot has the graveyard, it can retrieve it by paying 1 gold
            useGraveYard(players, districtToDestroy);
        }
    }

    private void useGraveYard(List<Player> players, DistrictCard districtToDestroy) {
        Player playerHasGraveyard = someoneHasGraveyard(players);
        if (playerHasGraveyard != null && playerHasGraveyard.wantToUseGraveyardEffect() && playerHasGraveyard.getGolds() >= 1) {
            playerHasGraveyard.setGolds(playerHasGraveyard.getGolds() - 1);
            stackOfGolds.addGoldsToStack(1);
            playerHasGraveyard.addCardToHand(districtToDestroy);
            view.printGraveyardEffect(playerHasGraveyard, districtToDestroy);
        }
    }

    private DispatchState warlordChooseEffectToUse(Player playerThatWantToUseEffect, List<Player> players) {
        DispatchState warlordEffect = null;

        if (Boolean.FALSE.equals(this.getIsEffectUsed().get(DispatchState.DESTROY))) {
            // Case where the warlord can destroy a district
            if (Boolean.FALSE.equals(this.getIsEffectUsed().get(DispatchState.EARNDISTRICT_WARLORD)))
                // Case where the warlord can earn a district
                warlordEffect = playerThatWantToUseEffect.whichWarlordEffect(players);
            else
                // Case where the warlord can't earn a district
                warlordEffect = DispatchState.KILL;
        } else {
            // Case where the warlord can't destroy a district
            if (Boolean.FALSE.equals(this.getIsEffectUsed().get(DispatchState.EARNDISTRICT_WARLORD)))
                // Case where the warlord can earn a district
                warlordEffect = DispatchState.EARNDISTRICT_WARLORD;
        }

        return warlordEffect;
    }

    public Player getPlayerWhoStole() {
        return playerWhoStole;
    }

    public Player someoneHasGraveyard(List<Player> players) {
        for (Player player : players) {
            if (player.getBoard().contains(DistrictCard.GRAVEYARD)) {
                return player;
            }
        }
        return null;
    }
}

