package fr.cotedazur.univ.polytech.controller;

import fr.cotedazur.univ.polytech.model.bot.Player;
import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import fr.cotedazur.univ.polytech.model.card.DistrictCard;
import fr.cotedazur.univ.polytech.model.deck.Deck;
import fr.cotedazur.univ.polytech.view.GameView;

import java.util.*;


public class EffectController {

    private final HashMap<String, Integer> nbTimesEffectIsUsed = new HashMap<>();

    private final GameView view;

    public EffectController(GameView view) {
        nbTimesEffectIsUsed.put("EarnDistrictBishop", 0);
        nbTimesEffectIsUsed.put("EarnDistrictWarlord", 0);
        nbTimesEffectIsUsed.put("EarnDistrictKing", 0);
        nbTimesEffectIsUsed.put("EarnDistrictMerchant", 0);
        nbTimesEffectIsUsed.put("Steal", 0);
        nbTimesEffectIsUsed.put("Destroy", 0);
        this.view = view;
    }

    /**
     * @return the list of the roles that can be stolen by the thief
     */
    public List<CharacterCard> roleNeededForThiefEffect() {
        ArrayList<CharacterCard> newList = new ArrayList<>();
        for (CharacterCard characterCard : CharacterCard.values()) {
            if (characterCard != CharacterCard.ASSASSIN) newList.add(characterCard);
        }
        return newList;
    }

    /**
     * @param players the list of the players
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
        return newList;
    }

    /**
     * @param playerThatUseEffect the warlord
     * @param playerToDestroy the player that the warlord want to destroy
     * @return the list of the districts that can be destroyed by the warlord for the playerToDestroy
     */
    public List<DistrictCard> districtNeededForWarlordEffect(Player playerThatUseEffect, Player playerToDestroy) {
        ArrayList<DistrictCard> newList = new ArrayList<>();
        for (DistrictCard districtCard : playerToDestroy.getBoard()) {
            if (districtCard.isDestroyableDistrict(playerThatUseEffect.getGolds())) newList.add(districtCard);
        }
        return newList;
    }

    private Player playerCopy(Player playerCopy, Player playerThatUseEffect) {
        Player copyPlayer = playerCopy.copy();
        if (playerCopy.getPlayerRole().getCharacterNumber() < playerThatUseEffect.getPlayerRole().getCharacterNumber() && !playerCopy.isDead())
            copyPlayer.setPlayerRole(playerCopy.getPlayerRole());
        return copyPlayer;
    }

    public Map<String, Integer> getNbTimesEffectIsUsed() {
        return nbTimesEffectIsUsed;
    }

    public void playerWantToUseEffect(Player playerThatWantToUseEffect, List<Player> players, Deck<DistrictCard> districtDiscardDeck) {
        switch (playerThatWantToUseEffect.getPlayerRole()) {
            case ASSASSIN -> {
                //TODO TO TEST
            }
            case THIEF -> {
                if (this.getNbTimesEffectIsUsed().get("Steal") == 0) {
                    CharacterCard roleStolen = playerThatWantToUseEffect.selectWhoWillBeAffectedByThiefEffect(players, this.roleNeededForThiefEffect());
                    if (roleStolen != CharacterCard.ASSASSIN) {
                        for (Player player1 : players) {
                            if (player1.getPlayerRole() == roleStolen) {
                                playerThatWantToUseEffect.getPlayerRole().useEffectThief(playerThatWantToUseEffect, player1);
                            }
                        }
                        this.getNbTimesEffectIsUsed().put("Steal", 1);
                    }
                }
            }
            case MAGICIAN -> {
                //TODO TO TEST
            }
            case KING -> {
                if (this.getNbTimesEffectIsUsed().get("EarnDistrictKing") == 0) {
                    playerThatWantToUseEffect.getPlayerRole().useEffect(playerThatWantToUseEffect);
                    this.getNbTimesEffectIsUsed().put("EarnDistrictKing", 1);
                }
            }
            case BISHOP -> {
                if (this.getNbTimesEffectIsUsed().get("EarnDistrictBishop") == 0) {
                    playerThatWantToUseEffect.getPlayerRole().useEffect(playerThatWantToUseEffect);
                    this.getNbTimesEffectIsUsed().put("EarnDistrictBishop", 1);
                }
            }
            case MERCHANT -> {
                if (this.getNbTimesEffectIsUsed().get("EarnDistrictMerchant") == 0) {
                    playerThatWantToUseEffect.getPlayerRole().useEffect(playerThatWantToUseEffect);
                    this.getNbTimesEffectIsUsed().put("EarnDistrictMerchant", 1);
                }
            }
            case WARLORD -> {
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

                if (warlordEffect != null) {
                    if (warlordEffect.equals("EarnDistrictWarlord")) {
                        // Case where the warlord earn a district
                        playerThatWantToUseEffect.getPlayerRole().useEffect(playerThatWantToUseEffect);
                        this.getNbTimesEffectIsUsed().put("EarnDistrictWarlord", 1);
                    } else {
                        // Case where the warlord destroy a district

                        // Create the list of the players that can be destroyed by the warlord
                        List<Player> playersNeeded = playerNeededForWarlordEffect(players, playerThatWantToUseEffect);
                        Player playerToDestroy = null;
                        if (!playersNeeded.isEmpty()) {
                            playerToDestroy = playerThatWantToUseEffect.choosePlayerToDestroy(playersNeeded);
                        }

                        if (playerToDestroy != null) {
                            // If the bot choose a player to destroy, we create the list of the districts that can be destroyed by the warlord for the playerToDestroy
                            List<DistrictCard> districtsNeeded = districtNeededForWarlordEffect(playerThatWantToUseEffect, playerToDestroy);
                            DistrictCard districtToDestroy = null;
                            if (!districtsNeeded.isEmpty()) {
                                districtToDestroy = playerThatWantToUseEffect.chooseDistrictToDestroy(playerToDestroy, districtNeededForWarlordEffect(playerThatWantToUseEffect, playerToDestroy));
                            }

                            if (districtToDestroy != null) {
                                // If the bot choose a district to destroy, we destroy it and display the district that was destroyed and the player who had the district
                                playerThatWantToUseEffect.getPlayerRole().useEffectWarlord(playerThatWantToUseEffect, playerToDestroy, districtToDestroy, districtDiscardDeck);
                                this.getNbTimesEffectIsUsed().put("Destroy", 1);
                                view.printDistrictDestroyed(playerThatWantToUseEffect, playerToDestroy, districtToDestroy);
                            }
                        }
                    }
                }
            }
        }
    }
}
