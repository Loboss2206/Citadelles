package fr.cotedazur.univ.polytech.model;

import fr.cotedazur.univ.polytech.model.bot.Player;
import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import fr.cotedazur.univ.polytech.model.card.DistrictCard;
import fr.cotedazur.univ.polytech.model.deck.Deck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class EffectControl {

    private HashMap<String,Integer> nbTimesEffectIsUsed = new HashMap<>();

    public  EffectControl(){
        nbTimesEffectIsUsed.put("EarnDistrictBishop",0);
        nbTimesEffectIsUsed.put("EarnDistrictWarlord",0);
        nbTimesEffectIsUsed.put("EarnDistrictKing",0);
        nbTimesEffectIsUsed.put("EarnDistrictMerchant",0);
        nbTimesEffectIsUsed.put("Steal",0);
        nbTimesEffectIsUsed.put("Destroy",0);
    }
    public List<Player> playerNeededForEffectWithoutSensibleInformation(List<Player> players, Player playerThatUseEffect){
        ArrayList<Player> newList = new ArrayList<>();
        for(Player player : players){
            if(player != playerThatUseEffect){
                newList.add(this.playerCopy(player,playerThatUseEffect));
            }
        }
        return newList;
    }

    private Player playerCopy(Player playerCopy,Player playerThatUseEffect){
        Player copyPlayer = playerCopy.copyPlayer();
        copyPlayer.setGolds(playerCopy.getGolds());
        if(playerCopy.getPlayerRole().getCharacterNumber() < playerThatUseEffect.getPlayerRole().getCharacterNumber() )copyPlayer.setPlayerRole(playerCopy.getPlayerRole());
        return copyPlayer;
    }

    public HashMap<String, Integer> getNbTimesEffectIsUsed() {
        return nbTimesEffectIsUsed;
    }
}
