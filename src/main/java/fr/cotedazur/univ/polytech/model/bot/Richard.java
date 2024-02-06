package fr.cotedazur.univ.polytech.model.bot;

import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import fr.cotedazur.univ.polytech.model.card.Color;
import fr.cotedazur.univ.polytech.model.card.DistrictCard;
import fr.cotedazur.univ.polytech.model.card.PurpleEffectState;

import java.util.*;


public class Richard extends Player implements GameActions {

    private Random random = new Random();

    public Richard() {
        super();
    }

    private CharacterCard target;

    @Override
    public DispatchState startChoice() {
        int randomIndex = random.nextInt(2);
        switch (randomIndex) {
            case 0 -> {
                return DispatchState.TWO_GOLDS;
            }
            case 1 -> {
                return DispatchState.DRAW_CARD;
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
        //Avoid aggressive characters and opportunist characaters (warlord, thief, assassin, magician, bishop) and remove the visible discarded cards and the character that has been killed
        List<CharacterCard> characterCardsCopy = new ArrayList<>(characterCards);
        characterCardsCopy.removeIf(element -> (element != CharacterCard.ARCHITECT && element != CharacterCard.KING && element != CharacterCard.MERCHANT) || (getDiscardedCardDuringTheRound().contains(element)) || element == getRoleKilledByAssassin());
        if(!characterCardsCopy.isEmpty()){
            return characterCardsCopy.get(random.nextInt(characterCardsCopy.size()));
        }
        return characterCards.get(random.nextInt(characterCards.size()));
    }

    @Override
    public CharacterCard selectWhoWillBeAffectedByAssassinEffect(List<Player> players, List<CharacterCard> characterCards) {
        if (target == CharacterCard.MAGICIAN)
            return target;
        if (isFirst(players) || whatCharacterGotTookByGoodPlayer(players, CharacterCard.WARLORD) || onlyOneWith1GoldDistrict(players)) {
            return CharacterCard.WARLORD;
        }
        if (someoneIsGoingToGetRich(players) || whatCharacterGotTookByGoodPlayer(players, CharacterCard.THIEF)){
            return CharacterCard.THIEF;
        }
        return characterCards.get(random.nextInt(characterCards.size()));
    }

    public boolean whatCharacterGotTookByGoodPlayer(List<Player> players, CharacterCard card) {
        if (getDiscardedCardDuringTheRound().contains(card)){
            return false;
        }
        List<Player> playersInOrder = getListCopyPlayers();
        for (Player player : players){
            if (player.equals(this)) continue;
            if (player.getBoard().size() >= 6){
                if (playersInOrder.indexOf(player) < playersInOrder.indexOf(this)){
                    return !getCurrentChoiceOfCharactersCardsDuringTheRound().contains(card);
                }
                else {
                    return getCurrentChoiceOfCharactersCardsDuringTheRound().contains(card);
                }
            }
        }
        return false;
    }

    public boolean someoneIsGoingToGetRich(List<Player> players) {
        int count = 0;
        boolean someonePoor = false;
        for (Player player : players){
            if (player.getGolds() >= 4){
                count++;
            }
            if (player.getGolds() <= 1){
                if (player.equals(this)) continue;
                someonePoor = true;
            }
        }
        return someonePoor && count>=2;
    }

    public boolean onlyOneWith1GoldDistrict(List<Player> players) {
        for (Player player : players){
            if (player.equals(this)) continue;
            for (DistrictCard districtCard : player.getBoard()){
                if (districtCard.getDistrictValue() == 1){
                    return false;
                }
            }
        }
        for (DistrictCard districtCard : getBoard()){
            if (districtCard.getDistrictValue() == 1){
                return true;
            }
        }
        return false;
    }


    public boolean isFirst(List<Player> players){
        Map<String, Integer> countPoints = new HashMap<>();
        for (Player player : players){
            countPoints.put(player.getName(), 0);
        }
        for (Player player : players) {
            for (DistrictCard card : player.getBoard()) {
                int i = (card == DistrictCard.DRAGON_GATE || card == DistrictCard.UNIVERSITY) ? 2 : 0;
                countPoints.put(player.getName() ,countPoints.get(player.getName())+ card.getDistrictValue() + i);
            }
        }
        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(countPoints.entrySet());
        entryList.sort(Map.Entry.comparingByValue());
        Collections.reverse(entryList);
        return entryList.get(0).getKey().equals(this.getName());
    }
    @Override
    public int chooseCharacter(List<CharacterCard> cards) {

        //Thief is interesting at first but when the game progresses he is not interesting (according to tt-22a5e3f98e5243b9f1135d1caadc4cc7)
        if(cards.contains(CharacterCard.THIEF) && getCurrentNbRound() <= 3 && getGolds() <= 2 && thereIsSomeoneWithALotOfGolds()) {
            return cards.indexOf(CharacterCard.THIEF);
        }
        if (cards.contains(CharacterCard.ASSASSIN)){
            if ((this.getHands().size() >= 5 && someoneHasNoCards(getListCopyPlayers()))){
                target = CharacterCard.MAGICIAN;
                return cards.indexOf(CharacterCard.ASSASSIN);

            }
            //TODO

        }
        return random.nextInt(cards.size()); //return a random number between 0 and the size of the list
    }

    public boolean someoneHasNoCards(List<Player> players) {
        for (Player player : players){
            if (player.equals(this)) continue;
            if (player.getHands().isEmpty()){
                return true;
            }
        }
        return false;
    }

    public boolean thereIsSomeoneWithALotOfGolds(){
        for(Player player : getListCopyPlayers()){
            if(player.getGolds() >= 3){
                return true;
            }
        }
        return false;
    }

    @Override
    public Player choosePlayerToDestroy(List<Player> players) {
        int rand = random.nextInt(2);
        if (rand == 0) {
            return null;
        } else {
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
    public Color chooseColorForSchoolOfMagic() {
        return Color.values()[random.nextInt(Color.values().length)];
    }

    @Override
    public Color chooseColorForHauntedCity() {
        return Color.values()[random.nextInt(Color.values().length)];
    }


    @Override
    public boolean wantToUseLaboratoryEffect(){
        return random.nextInt(2) == 0;
    }
    @Override
    public DistrictCard chooseHandCardToDiscard() {
        boolean wantToUseDistrictCard = random.nextBoolean();
        if (!getHands().isEmpty() && (wantToUseDistrictCard)) {
            return getHands().get(random.nextInt(getHands().size()));

        }
        return null;
    }

    @Override
    public void drawCard(Map<DispatchState, ArrayList<DistrictCard>> cardsThatThePlayerDontWantAndThatThePlayerWant, DistrictCard... cards) {
        int randomCard = random.nextInt(cards.length);
        int randomSecondCard = -1;
        if (this.getBoard().contains(DistrictCard.LIBRARY) && cards.length > 1) {
            do {
                randomSecondCard = random.nextInt(cards.length);
            }
            while (randomSecondCard == randomCard);
        }
        LOGGER.info("Cartes piochées : " + Arrays.toString(cards));
        for (int i = 0; i < cards.length; i++) {
            if (i == randomCard || i == randomSecondCard) {
                cardsThatThePlayerDontWantAndThatThePlayerWant.get(DispatchState.CARDS_WANTED).add(cards[i]);
            } else {
                cardsThatThePlayerDontWantAndThatThePlayerWant.get(DispatchState.CARDS_NOT_WANTED).add(cards[i]);
            }
        }
        LOGGER.info("Cartes jetées : " + cardsThatThePlayerDontWantAndThatThePlayerWant.get(DispatchState.CARDS_NOT_WANTED));
    }


    @Override
    public DispatchState whichWarlordEffect(List<Player> players) {
        int randomIndex = random.nextInt(3);
        switch (randomIndex) {
            case 0 -> {
                return DispatchState.KILL;
            }
            case 1 -> {
                return DispatchState.EARNDISTRICT_WARLORD;
            }
            default -> {
                return null;
            }
        }
    }

    @Override
    public DispatchState whichMagicianEffect(List<Player> players) {
        int randomIndex = random.nextInt(2);
        switch (randomIndex) {
            case 0 -> {
                return DispatchState.EXCHANGE_PLAYER;
            }
            case 1 -> {
                return DispatchState.EXCHANGE_DECK;
            }
            default -> {
                return null;
            }
        }
    }

    @Override
    public boolean wantToUseEffect(boolean beforePuttingADistrict) {
        int randomIndex = random.nextInt(2);
        return randomIndex == 0;
    }

    @Override
    public boolean wantsToUseSmithyEffect() {
        int randomIndex = random.nextInt(2);
        return randomIndex == 0;
    }

    @Override
    public List<DistrictCard> chooseCardsToChange() {
        List<DistrictCard> cardsToExchange = new ArrayList<>();
        if (getHands().isEmpty())
            return cardsToExchange;
        int nbCards = random.nextInt(this.getHands().size()) + 1;
        for (int i = 0; i < nbCards; i++) {
            cardsToExchange.add(this.getHands().get(i));
        }
        return cardsToExchange;
    }

    @Override
    public Player selectMagicianTarget(List<Player> players) {
        return players.get(random.nextInt(players.size()));
    }

    @Override
    public boolean wantToUseGraveyardEffect() {
        int choice = random.nextInt(2);
        return choice == 0;
    }

    public CharacterCard getTarget(){
        return target;
    }
}


