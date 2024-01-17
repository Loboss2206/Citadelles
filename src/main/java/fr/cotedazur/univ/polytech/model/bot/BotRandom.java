package fr.cotedazur.univ.polytech.model.bot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import fr.cotedazur.univ.polytech.model.card.Color;
import fr.cotedazur.univ.polytech.model.card.DistrictCard;


public class BotRandom extends Player implements GameActions {

    private Random random = new Random();

    public BotRandom() {
        super();
    }

    @Override
    public String startChoice() {
        int randomIndex = random.nextInt(2);
        switch (randomIndex) {
            case 0 -> {
                return "2golds";
            }
            case 1 -> {
                return "drawCard";
            }
            default -> {
                return null;
            }
        }
    }



    @Override
    public DistrictCard choiceHowToPlayDuringTheRound() {
        int randomIndex = random.nextInt(2);
        if (randomIndex == 0) {
            return putADistrict();
        }
        return null;
    }

    @Override
    public DistrictCard putADistrict() {
        discoverValidCard();
        if (!validCards.isEmpty()) {
            int randomIndex = random.nextInt(validCards.size());
            return validCards.get(randomIndex);
        }
        return null;
    }

    @Override
    public CharacterCard selectWhoWillBeAffectedByThiefEffect(List<Player> players, List<CharacterCard> characterCards) {
        if (getPlayerRole() == CharacterCard.THIEF) {
            return characterCards.get(random.nextInt(characterCards.size()));
        }
        return null;
    }

    @Override
    public CharacterCard selectWhoWillBeAffectedByAssassinEffect(List<Player> players, List<CharacterCard> characterCards) {
        if (getPlayerRole() == CharacterCard.ASSASSIN) {
            return characterCards.get(random.nextInt(characterCards.size()));
        }
        return null;
    }

    @Override
    public int chooseCharacter(List<CharacterCard> cards) {
        return random.nextInt(cards.size()); //return a random number between 0 and the size of the list
    }

    @Override
    public Player choosePlayerToDestroy(List<Player> players) {
        int rand = random.nextInt(2);
        if (rand == 0) {
            return null;
        }
        else {
            return players.get(random.nextInt(players.size()));
        }
    }

    @Override
    public DistrictCard chooseDistrictToDestroy(Player player, List<DistrictCard> districtCards) {
        return districtCards.get(random.nextInt(districtCards.size()));
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public Color chooseColorForDistrictCard() {
            if (getPlayerRole() == CharacterCard.KING || getPlayerRole() == CharacterCard.BISHOP || getPlayerRole() == CharacterCard.MERCHANT || getPlayerRole() == CharacterCard.WARLORD) {
                return Color.values()[random.nextInt(Color.values().length)];
            }
        return null;
    }

    @Override
    public List<DistrictCard> drawCard(DistrictCard... cards) {
        int randomCard = random.nextInt(cards.length);
        LOGGER.info("Cartes piochées : "+ Arrays.toString(cards));
        ArrayList<DistrictCard> cardThatBotDontWant = new ArrayList<>();
        for(int i = 0;i < cards.length;i++){
            if(i == randomCard) {
                getHands().add(cards[i]);
            }else{
                cardThatBotDontWant.add(cards[i]);
            }
        }
        LOGGER.info("Cartes jetées : "+ cardThatBotDontWant);
        return cardThatBotDontWant;
    }


    @Override
    public String whichWarlordEffect(List<Player> players) {
        int randomIndex = random.nextInt(2);
        switch (randomIndex) {
            case 0 -> {
                return "Destroy";
            }
            case 1 -> {
                return "EarnDistrictWarlord";
            }
            default -> {
                return null;
            }
        }
    }

    @Override
    public String whichMagicianEffect(List<Player> players) {
        int randomIndex = random.nextInt(2);
        switch (randomIndex) {
            case 0 -> {
                return "ExchangePlayer";
            }
            case 1 -> {
                return "ExchangeDeck";
            }
            default -> {
                return null;
            }
        }
    }

    @Override
    public boolean wantToUseEffect(boolean beforePuttingADistrict){
        int randomIndex = random.nextInt(2);
        return randomIndex == 0;
    }

    @Override
    public List<DistrictCard> chooseCardsToChange(){
        List<DistrictCard> cardsToExchange = new ArrayList<>();
        if (getHands().isEmpty())
            return cardsToExchange;
        int nbCards = random.nextInt(this.getHands().size())+1;
        for(int i = 0; i<nbCards; i++){
            cardsToExchange.add(this.getHands().get(i));
        }
        return cardsToExchange;
    }

    @Override
    public Player selectMagicianTarget(List<Player> players){
        return players.get(random.nextInt(players.size()));
    }

}
