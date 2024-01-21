package fr.cotedazur.univ.polytech.controller;

import fr.cotedazur.univ.polytech.logger.LamaLogger;
import fr.cotedazur.univ.polytech.model.bot.Player;
import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import fr.cotedazur.univ.polytech.model.card.DistrictCard;
import fr.cotedazur.univ.polytech.model.deck.Deck;
import fr.cotedazur.univ.polytech.view.GameView;

import java.util.*;
import java.util.logging.Level;


public class EffectController {
    private final static java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(LamaLogger.class.getName());

    private final HashMap<String, Integer> nbTimesEffectIsUsed = new HashMap<>();

    private Player playerWhoStole;

    private GameView view;

    public EffectController() {
        nbTimesEffectIsUsed.put("EarnDistrictBishop", 0);
        nbTimesEffectIsUsed.put("EarnDistrictWarlord", 0);
        nbTimesEffectIsUsed.put("EarnDistrictKing", 0);
        nbTimesEffectIsUsed.put("EarnDistrictMerchant", 0);
        nbTimesEffectIsUsed.put("Steal", 0);
        nbTimesEffectIsUsed.put("Destroy", 0);
        nbTimesEffectIsUsed.put("Kill", 0);
        nbTimesEffectIsUsed.put("Exchange", 0);
    }

    public void setView(GameView view) {
        this.view = view;
    }

    public EffectController(GameView view) {
        this();
        this.view = view;
    }

    public List<Player> playerNeededForEffectWithoutSensibleInformationForThief(List<Player> players, Player playerThatUseEffect) {
        ArrayList<Player> newList = new ArrayList<>();
        for (Player player : players) {
            if (player != playerThatUseEffect && player.playerHasADestroyableDistrict(playerThatUseEffect)) {
                newList.add(this.playerCopy(player, playerThatUseEffect));
            }
        }
        LOGGER.log(Level.INFO, "La liste des joueurs qui peuvent être affectés par l'effet du voleur est: " + newList);
        return newList;
    }

    /**
     * @param players             the list of the players
     * @param playerThatUseEffect the warlord
     * @return the list of the players that can be destroyed by the warlord
     */
    public List<Player> playerNeededForWarlordEffect(List<Player> players, Player playerThatUseEffect) {
        ArrayList<Player> newList = new ArrayList<>();
        for (Player player : players) {
            if (player != playerThatUseEffect && player.playerHasADestroyableDistrict(playerThatUseEffect)) {
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

    public Map<String, Integer> getNbTimesEffectIsUsed() {
        return nbTimesEffectIsUsed;
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
        if (this.getNbTimesEffectIsUsed().get("Kill") == 0) {
            CharacterCard roleKilled = playerThatWantToUseEffect.selectWhoWillBeAffectedByAssassinEffect(this.playerNeededForEffectWithoutSensibleInformationForAssassin(players, playerThatWantToUseEffect), this.roleNeededForAssassinEffect());
            LOGGER.info("Le joueur " + playerThatWantToUseEffect.getName() + " (" + playerThatWantToUseEffect.getPlayerRole().getCharacterName() + ") a choisi le personnage " + roleKilled.getCharacterName() + " pour être éliminé");
            if (roleKilled != CharacterCard.ASSASSIN) {
                for (Player playerAffected : players) {
                    if (playerAffected.getPlayerRole() == roleKilled) {
                        playerThatWantToUseEffect.getPlayerRole().useEffectAssassin(playerThatWantToUseEffect, playerAffected);
                    }
                }
                this.getNbTimesEffectIsUsed().put("Kill", 1);
                if (view != null) view.killPlayer(roleKilled);
            }
        } else {
            LOGGER.info("Le joueur " + playerThatWantToUseEffect.getName() + " ne peut pas utiliser l'effet de l'assassin");
        }
    }

    public void playerWantToUseEffectThief(Player playerThatWantToUseEffect, List<Player> players) {
        if (this.getNbTimesEffectIsUsed().get("Steal") == 0) {
            CharacterCard roleStolen = playerThatWantToUseEffect.selectWhoWillBeAffectedByThiefEffect(players, this.roleNeededForThiefEffect());
            LOGGER.info("Le joueur " + playerThatWantToUseEffect.getName() + " (" + playerThatWantToUseEffect.getPlayerRole().getCharacterName() + ") a choisi le personnage " + roleStolen.getCharacterName() + " pour être volé");
            if (roleStolen != CharacterCard.ASSASSIN) {
                for (Player player1 : players) {
                    if (player1.getPlayerRole() == roleStolen) {
                        playerThatWantToUseEffect.getPlayerRole().useEffectThief(playerThatWantToUseEffect, player1, false);
                        playerWhoStole = playerThatWantToUseEffect;
                    }
                }
                this.getNbTimesEffectIsUsed().put("Steal", 1);
                if (view != null) view.stolenPlayer(roleStolen);
            }
        } else {
            LOGGER.info("Le joueur " + playerThatWantToUseEffect.getName() + " ne peut pas utiliser l'effet du voleur");
        }
    }

    private void playerWantToUseEffectMagician(Player playerThatWantToUseEffect, List<Player> players, Deck<DistrictCard> districtDeck) {
        if (this.getNbTimesEffectIsUsed().get("Exchange") == 0) {
            String action = playerThatWantToUseEffect.whichMagicianEffect(players);
            if (action.equals("ExchangeDeck")) {
                List<DistrictCard> cardToRemove = playerThatWantToUseEffect.chooseCardsToChange();
                view.exchangeDeckCard(playerThatWantToUseEffect, cardToRemove);
                if (!cardToRemove.isEmpty()) {
                    playerThatWantToUseEffect.getPlayerRole().useEffectMagicianWithDeck(playerThatWantToUseEffect, cardToRemove, districtDeck);
                }
            } else if (action.equals("ExchangePlayer")) {
                Player playerTargeted = playerThatWantToUseEffect.selectMagicianTarget(players);
                playerThatWantToUseEffect.getPlayerRole().useEffectMagicianWithPlayer(playerThatWantToUseEffect, playerTargeted);
                view.exchangePlayerCard(playerThatWantToUseEffect, playerTargeted);
            } else {
                throw new UnsupportedOperationException("L'action demandé est " + action);
            }
        }
    }

    public void playerWantToUseEffectKing(Player playerThatWantToUseEffect) {
        if (this.getNbTimesEffectIsUsed().get("EarnDistrictKing") == 0) {
            int golds = playerThatWantToUseEffect.getGolds();
            playerThatWantToUseEffect.getPlayerRole().useEffect(playerThatWantToUseEffect);
            view.printCharacterGetGolds(playerThatWantToUseEffect, playerThatWantToUseEffect.getPlayerRole().getCharacterColor(), playerThatWantToUseEffect.getGolds() - golds);
            this.getNbTimesEffectIsUsed().put("EarnDistrictKing", 1);
        } else {
            LOGGER.info("Le joueur " + playerThatWantToUseEffect.getName() + " ne peut pas utiliser l'effet du roi");
        }
    }

    public void playerWantToUseEffectBishop(Player playerThatWantToUseEffect) {
        if (this.getNbTimesEffectIsUsed().get("EarnDistrictBishop") == 0) {
            int golds = playerThatWantToUseEffect.getGolds();
            playerThatWantToUseEffect.getPlayerRole().useEffect(playerThatWantToUseEffect);
            view.printCharacterGetGolds(playerThatWantToUseEffect, playerThatWantToUseEffect.getPlayerRole().getCharacterColor(), playerThatWantToUseEffect.getGolds() - golds);
            this.getNbTimesEffectIsUsed().put("EarnDistrictBishop", 1);
        } else {
            LOGGER.info("Le joueur " + playerThatWantToUseEffect.getName() + " ne peut pas utiliser l'effet de l'évêque");
        }
    }

    public void playerWantToUseEffectMerchant(Player playerThatWantToUseEffect) {
        if (this.getNbTimesEffectIsUsed().get("EarnDistrictMerchant") == 0) {
            int golds = playerThatWantToUseEffect.getGolds();
            playerThatWantToUseEffect.getPlayerRole().useEffect(playerThatWantToUseEffect);
            view.printCharacterGetGolds(playerThatWantToUseEffect, playerThatWantToUseEffect.getPlayerRole().getCharacterColor(), playerThatWantToUseEffect.getGolds() - golds);
            this.getNbTimesEffectIsUsed().put("EarnDistrictMerchant", 1);
        } else {
            LOGGER.info("Le joueur " + playerThatWantToUseEffect.getName() + " ne peut pas utiliser l'effet du marchand");
        }
    }

    public void playerWantToUseEffectWarlord(Player playerThatWantToUseEffect, List<Player> players, Deck<DistrictCard> districtDiscardDeck) {
        // Recover the effect that the warlord will use
        String warlordEffect = warlordChooseEffectToUse(playerThatWantToUseEffect, players);

        LOGGER.info("Le joueur " + playerThatWantToUseEffect.getName() + " (" + playerThatWantToUseEffect.getPlayerRole().getCharacterName() + ") a choisi l'effet " + warlordEffect + " pour le warlord");

        if (warlordEffect != null) {
            if (warlordEffect.equals("EarnDistrictWarlord")) {
                // Case where the warlord earn golds for each red district he has
                int golds = playerThatWantToUseEffect.getGolds();
                playerThatWantToUseEffect.getPlayerRole().useEffect(playerThatWantToUseEffect);
                view.printCharacterGetGolds(playerThatWantToUseEffect, playerThatWantToUseEffect.getPlayerRole().getCharacterColor(), playerThatWantToUseEffect.getGolds() - golds);
                this.getNbTimesEffectIsUsed().put("EarnDistrictWarlord", 1);
            } else {
                // Case where the warlord destroy a district

                // Create the list of the players that can be destroyed by the warlord
                List<Player> playersNeeded = playerNeededForWarlordEffect(players, playerThatWantToUseEffect);

                Player playerToDestroy = null;
                DistrictCard districtToDestroy = null;

                if (!playersNeeded.isEmpty()) {
                    playerToDestroy = playerThatWantToUseEffect.choosePlayerToDestroy(playersNeeded);
                    LOGGER.info("Le joueur " + playerThatWantToUseEffect.getName() + " (" + playerThatWantToUseEffect.getPlayerRole().getCharacterName() + ") a choisi le joueur " + (playerToDestroy != null ? playerToDestroy.getName() : "null") + " pour être détruit");
                }

                if (playerToDestroy != null) {
                    // If the bot choose a player to destroy, we create the list of the districts that can be destroyed by the warlord for the playerToDestroy
                    List<DistrictCard> districtsNeeded = districtNeededForWarlordEffect(playerThatWantToUseEffect, playerToDestroy);
                    if (!districtsNeeded.isEmpty()) {
                        districtToDestroy = playerThatWantToUseEffect.chooseDistrictToDestroy(playerToDestroy, districtNeededForWarlordEffect(playerThatWantToUseEffect, playerToDestroy));
                        LOGGER.info("Le joueur " + playerThatWantToUseEffect.getName() + " (" + playerThatWantToUseEffect.getPlayerRole().getCharacterName() + ") a choisi le quartier " + districtToDestroy.getDistrictName() + " du joueur " + playerToDestroy.getName() + " pour être détruit");
                    }
                }

                if (districtToDestroy != null) {
                    // If the bot choose a district to destroy, we destroy it and display the district that was destroyed and the player who had the district
                    playerThatWantToUseEffect.getPlayerRole().useEffectWarlord(playerThatWantToUseEffect, playerToDestroy, districtToDestroy, districtDiscardDeck);
                    this.getNbTimesEffectIsUsed().put("Destroy", 1);
                    if (view != null && playerToDestroy.getPlayerRole() != null)
                        view.printDistrictDestroyed(playerThatWantToUseEffect, playerToDestroy, districtToDestroy);
                }
            }
        }
    }

    private String warlordChooseEffectToUse(Player playerThatWantToUseEffect, List<Player> players) {
        String warlordEffect = null;

        if (this.getNbTimesEffectIsUsed().get("Destroy") == 0) {
            // Case where the warlord can destroy a district
            if (this.getNbTimesEffectIsUsed().get("EarnDistrictWarlord") == 0)
                // Case where the warlord can earn a district
                warlordEffect = playerThatWantToUseEffect.whichWarlordEffect(players);
            else
                // Case where the warlord can't earn a district
                warlordEffect = "Destroy";
        } else {
            // Case where the warlord can't destroy a district
            if (this.getNbTimesEffectIsUsed().get("EarnDistrictWarlord") == 0)
                // Case where the warlord can earn a district
                warlordEffect = "EarnDistrictWarlord";
        }

        return warlordEffect;
    }

    public Player getPlayerWhoStole() {
        return playerWhoStole;
    }
}
