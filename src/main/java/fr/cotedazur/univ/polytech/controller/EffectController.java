package fr.cotedazur.univ.polytech.controller;

import fr.cotedazur.univ.polytech.model.bot.Player;
import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import fr.cotedazur.univ.polytech.view.GameView;

import java.util.*;


public class EffectController {

    private final HashMap<String,Integer> nbTimesEffectIsUsed = new HashMap<>();

    private GameView view;

    public EffectController(){
        nbTimesEffectIsUsed.put("EarnDistrictBishop",0);
        nbTimesEffectIsUsed.put("EarnDistrictWarlord",0);
        nbTimesEffectIsUsed.put("EarnDistrictKing",0);
        nbTimesEffectIsUsed.put("EarnDistrictMerchant",0);
        nbTimesEffectIsUsed.put("Steal",0);
        nbTimesEffectIsUsed.put("Destroy",0);
        nbTimesEffectIsUsed.put("Kill",0);
    }

    public void setView(GameView view){
        this.view = view;
    }

    public List<Player> playerNeededForEffectWithoutSensibleInformationForThief(List<Player> players, Player playerThatUseEffect){
        ArrayList<Player> newList = new ArrayList<>();
        for(Player player : players){
            if(player != playerThatUseEffect){
                newList.add(this.playerCopy(player,playerThatUseEffect));
            }
        }
        return newList;
    }

    public List<CharacterCard> roleNeededForThiefEffect(){
        ArrayList<CharacterCard> newList = new ArrayList<>();
        for(CharacterCard characterCard :  CharacterCard.values()){
            if(characterCard != CharacterCard.ASSASSIN) newList.add(characterCard);
        }
        return newList;
    }

    public List<Player> playerNeededForEffectWithoutSensibleInformationForAssassin(List<Player> players, Player playerThatUseEffect){
        ArrayList<Player> newList = new ArrayList<>();
        for(Player player : players){
            if(player != playerThatUseEffect){
                newList.add(this.playerCopy(player,playerThatUseEffect));
            }
        }
        return newList;
    }

    public List<Player> playerNeededForEffectWithoutSensibleInformationForWarlord(List<Player> players, Player playerThatUseEffect){
        return null;
    }

    private Player playerCopy(Player playerCopy,Player playerThatUseEffect){
        Player copyPlayer = playerCopy.copyPlayer();
        copyPlayer.setGolds(playerCopy.getGolds());
        copyPlayer.setName(playerCopy.getName());
         copyPlayer.setPlayerRole(playerCopy.getPlayerRole());
        return copyPlayer;
    }

    public Map<String, Integer> getNbTimesEffectIsUsed() {
        return nbTimesEffectIsUsed;
    }

    public  void playerWantToUseEffect(Player playerThatWantToUseEffect,List<Player> players){
        switch (playerThatWantToUseEffect.getPlayerRole()) {
            case ASSASSIN -> {
                if (this.getNbTimesEffectIsUsed().get("Kill") == 0) {
                    CharacterCard roleKilled = playerThatWantToUseEffect.selectWhoWillBeAffectedByAssassinEffect(this.playerNeededForEffectWithoutSensibleInformationForAssassin(players,playerThatWantToUseEffect));
                    if (roleKilled != CharacterCard.ASSASSIN) {
                        for (Player player1 : players) {
                            if (player1.getPlayerRole() == roleKilled) {
                                playerThatWantToUseEffect.getPlayerRole().useEffectAssassin(playerThatWantToUseEffect, player1);
                                if (view != null) view.killPlayer(player1);
                            }
                        }
                        this.getNbTimesEffectIsUsed().put("Kill", 1);
                    }
                }
            }
            case THIEF -> {
                if(this.getNbTimesEffectIsUsed().get("Steal") == 0) {
                    CharacterCard roleStolen = playerThatWantToUseEffect.selectWhoWillBeAffectedByThiefEffect(players,this.roleNeededForThiefEffect());
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
                if(this.getNbTimesEffectIsUsed().get("EarnDistrictKing") == 0){
                    playerThatWantToUseEffect.getPlayerRole().useEffect(playerThatWantToUseEffect);
                    this.getNbTimesEffectIsUsed().put("EarnDistrictKing",1);
                }
            }
            case BISHOP -> {
                if(this.getNbTimesEffectIsUsed().get("EarnDistrictBishop") == 0){
                    playerThatWantToUseEffect.getPlayerRole().useEffect(playerThatWantToUseEffect);
                    this.getNbTimesEffectIsUsed().put("EarnDistrictBishop",1);
                }
            }
            case MERCHANT -> {
                if(this.getNbTimesEffectIsUsed().get("EarnDistrictMerchant") == 0){
                    playerThatWantToUseEffect.getPlayerRole().useEffect(playerThatWantToUseEffect);
                    this.getNbTimesEffectIsUsed().put("EarnDistrictMerchant",1);
                }
            }
            case WARLORD -> {
                //String warlordEffect = player.WhichWarlordEffect();

                //Replace Destroy by the name of the effect
                if(this.getNbTimesEffectIsUsed().get("Destroy") == 0){
                    //WARLORD EFFECT
                    this.getNbTimesEffectIsUsed().put("Destroy",1);
                }
            }
        }
    }
}
