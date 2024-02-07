package fr.cotedazur.univ.polytech.model.bot;

import fr.cotedazur.univ.polytech.model.card.*;

import java.util.*;


public class Richard extends Player implements GameActions {

    private Random random = new Random();

    private Player targetedPlayerWhenIsLastBefore = null;

    public Richard() {
        super();
    }

    private CharacterCard target;

    private List<Player> playersThatIsSetToWin = new ArrayList<>();

    private boolean isBeforeLastRound = false;

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
        if (hasValidCard()) {
            int randomIndex = random.nextInt(validCards.size());
            return validCards.get(randomIndex);
        }
        return null;
    }

    @Override
    public CharacterCard selectWhoWillBeAffectedByThiefEffect(List<Player> players, List<CharacterCard> characterCards) {
        if(isBeforeLastRound){
            if(getCurrentChoiceOfCharactersCardsDuringTheRound().contains(CharacterCard.WARLORD) && !getDiscardedCardDuringTheRound().contains(CharacterCard.WARLORD)){
                for(Player player : playersThatIsSetToWin){
                    if(players.indexOf(player) == 0){
                        return CharacterCard.WARLORD;
                    }
                }
            } else if(getCurrentChoiceOfCharactersCardsDuringTheRound().contains(CharacterCard.BISHOP) && !getDiscardedCardDuringTheRound().contains(CharacterCard.BISHOP)){
                for(Player player : playersThatIsSetToWin){
                    if(players.indexOf(player) == 0){
                        return CharacterCard.BISHOP;
                    }
                }
            }
        }


        //Avoid aggressive characters and opportunist characters (warlord, thief, assassin, magician, bishop) and remove the visible discarded cards and the character that has been killed
        List<CharacterCard> characterCardsCopy = new ArrayList<>(characterCards);
        characterCardsCopy.removeIf(element -> (element != CharacterCard.ARCHITECT && element != CharacterCard.KING && element != CharacterCard.MERCHANT) || (getDiscardedCardDuringTheRound().contains(element)) || element == getRoleKilledByAssassin());
        if (!characterCardsCopy.isEmpty()) {
            return characterCardsCopy.get(random.nextInt(characterCardsCopy.size()));
        }
        return characterCards.get(random.nextInt(characterCards.size()));
    }

    @Override
    public CharacterCard selectWhoWillBeAffectedByAssassinEffect(List<Player> players, List<CharacterCard> characterCards) {
        if(isBeforeLastRound){
            if(getCurrentChoiceOfCharactersCardsDuringTheRound().contains(CharacterCard.WARLORD) && !getDiscardedCardDuringTheRound().contains(CharacterCard.WARLORD)){
                for(Player player : playersThatIsSetToWin){
                    if(players.indexOf(player) == 0){
                        return CharacterCard.WARLORD;
                    }
                }
            } else if(getCurrentChoiceOfCharactersCardsDuringTheRound().contains(CharacterCard.BISHOP) && !getDiscardedCardDuringTheRound().contains(CharacterCard.BISHOP)){
                for(Player player : playersThatIsSetToWin){
                    if(players.indexOf(player) == 0){
                        return CharacterCard.BISHOP;
                    }
                }
            }
        }
        if (target != null)
            return target;
        if (isFirst(players) || whatCharacterGotTookByGoodPlayer(players, CharacterCard.WARLORD) || onlyOneWith1GoldDistrict(players)) {
            return CharacterCard.WARLORD;
        }
        if (someoneIsGoingToGetRich(players) || whatCharacterGotTookByGoodPlayer(players, CharacterCard.THIEF)) {
            return CharacterCard.THIEF;
        }
        return characterCards.get(random.nextInt(characterCards.size()));
    }

    public boolean whatCharacterGotTookByGoodPlayer(List<Player> players, CharacterCard card) {
        if (getDiscardedCardDuringTheRound().contains(card)) {
            return false;
        }
        List<Player> playersInOrder = getListCopyPlayers();
        for (Player player : players) {
            if (player.equals(this)) continue;
            if (player.getBoard().size() >= 6) {
                if (playersInOrder.indexOf(player) < playersInOrder.indexOf(this)) {
                    return !getCurrentChoiceOfCharactersCardsDuringTheRound().contains(card);
                } else {
                    return getCurrentChoiceOfCharactersCardsDuringTheRound().contains(card);
                }
            }
        }
        return false;
    }

    public boolean someoneIsGoingToGetRich(List<Player> players) {
        int count = 0;
        boolean someonePoor = false;
        for (Player player : players) {
            if (player.getGolds() >= 4) {
                count++;
            }
            if (player.getGolds() <= 1) {
                if (player.equals(this)) continue;
                someonePoor = true;
            }
        }
        return someonePoor && count >= 2;
    }

    public boolean onlyOneWith1GoldDistrict(List<Player> players) {
        for (Player player : players) {
            if (player.equals(this)) continue;
            for (DistrictCard districtCard : player.getBoard()) {
                if (districtCard.getDistrictValue() == 1) {
                    return false;
                }
            }
        }
        for (DistrictCard districtCard : getBoard()) {
            if (districtCard.getDistrictValue() == 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * Search if there is player that have 6/8 district on the board
     *
     * @return return true if there is a player with 6/8 card
     */
    public boolean ifIsBeforeLastRound() {
        for (Player player : getListCopyPlayers()) {
            if (player != this && player.getBoard().size() == 6) {
                playersThatIsSetToWin.add(player);
            }
        }
        return !playersThatIsSetToWin.isEmpty();
    }

    public boolean isFirst(List<Player> players) {
        int countThis = 0;
        int countPlayer = 0;
        int maxPlayer = 0;
        for (Player player : players) {
            if (player.equals(this)) {
                countThis = getBoard().size();
            } else {
                countPlayer = player.getBoard().size();
            }
            if (countPlayer > maxPlayer) maxPlayer = countPlayer;
        }
        return maxPlayer <= countThis;
    }


    public boolean someoneHasNoCards(List<Player> players) {
        for (Player player : players) {
            if (player.equals(this)) continue;
            if (player.getHands().isEmpty()) {
                return true;
            }
        }
        return false;
    }


    private int countNumberOfSpecifiedColorCard(Color color) {
        int count = 0;
        for (DistrictCard card : getBoard()) {
            if (card.getDistrictColor().getColorName().equals(color.getColorName())) count++;
        }
        for (DistrictCard card : getHands()) {
            if (card.getDistrictColor().getColorName().equals(color.getColorName())) {
                count++;
                break;
            }
        }
        return count;
    }

    private Player playerThatCanWinWithTheLeastExpensiveDistrict() {
        Player resPlayer = playersThatIsSetToWin.get(0);
        for (Player player : playersThatIsSetToWin) {
            if (this.cheaperDistrictValue(resPlayer) < this.cheaperDistrictValue(player)) {
                resPlayer = player;
            }
        }
        return resPlayer;
    }

    private int cheaperDistrictValue(Player player) {
        DistrictCardComparator districtCardComparator = new DistrictCardComparator();
        List<DistrictCard> cards = player.getBoard();
        cards.sort(districtCardComparator);
        return cards.get(0).getDistrictValue();
    }

    @Override
    public int chooseCharacter(List<CharacterCard> cards) {
        discoverValidCard();
        target = null;
        targetedPlayerWhenIsLastBefore = null;

        //Before last round
        isBeforeLastRound = ifIsBeforeLastRound();
        if ((isBeforeLastRound)) {
            if (cards.contains(CharacterCard.KING)) {
                return cards.indexOf(CharacterCard.KING);
            } else if (cards.contains(CharacterCard.ASSASSIN)) {
                //If the king is not in the hand we target the king
                if(!getDiscardedCardDuringTheRound().contains(CharacterCard.KING)){
                    for(Player player : playersThatIsSetToWin) {
                        if (getListCopyPlayers().indexOf(player) == 0){
                            target = CharacterCard.KING;
                        }
                    }
                }
                return cards.indexOf(CharacterCard.ASSASSIN);
            } else if (cards.contains(CharacterCard.WARLORD)) {
                //If the king is not in the hand we target the king
                if(!getDiscardedCardDuringTheRound().contains(CharacterCard.KING) && !getDiscardedCardDuringTheRound().contains(CharacterCard.ASSASSIN)){
                    for(Player player : playersThatIsSetToWin) {
                        //If the player is first
                        if (getListCopyPlayers().indexOf(player) == 0){
                            targetedPlayerWhenIsLastBefore = playerThatCanWinWithTheLeastExpensiveDistrict();
                        }
                    }
                }
                return cards.indexOf(CharacterCard.WARLORD);
            } else if (cards.contains(CharacterCard.BISHOP)) {
                return cards.indexOf(CharacterCard.BISHOP);
            } else if (cards.contains(CharacterCard.MAGICIAN)) {
                return cards.indexOf(CharacterCard.MAGICIAN);
            }
        }


        //King
        if ((cards.contains(CharacterCard.KING) && countNumberOfSpecifiedColorCard(Color.YELLOW) > 0) || (cards.contains(CharacterCard.KING) && this.isCrowned() && getListCopyPlayers().size() < 5)) {
            return cards.indexOf(CharacterCard.KING);
        } else if (cards.contains(CharacterCard.BISHOP) && (countNumberOfSpecifiedColorCard(Color.BLUE) > 0 || (hasValidCard() && getCurrentNbRound() > 3))) {
            return cards.indexOf(CharacterCard.BISHOP);
        }//To have 3 golds directly
        else if ((cards.contains(CharacterCard.MERCHANT) && countNumberOfSpecifiedColorCard(Color.GREEN) > 0) || (cards.contains(CharacterCard.MERCHANT) && getGolds() < 2)) {
            return cards.indexOf(CharacterCard.MERCHANT);
        } else if (cards.contains(CharacterCard.MAGICIAN) && getHands().isEmpty() && thereIsSomeoneWithALotOfCards()) {
            return cards.indexOf(CharacterCard.MAGICIAN);
        }
        //Thief is interesting at first but when the game progresses he is not interesting (according to tt-22a5e3f98e5243b9f1135d1caadc4cc7)
        else if (cards.contains(CharacterCard.THIEF) && getCurrentNbRound() <= 3 && getGolds() <= 2 && thereIsSomeoneWithALotOfGolds()) {
            return cards.indexOf(CharacterCard.THIEF);
        } else if (cards.contains(CharacterCard.ASSASSIN)) {
            if ((this.getHands().size() >= 5 && someoneHasNoCards(getListCopyPlayers()))) {
                target = CharacterCard.MAGICIAN;
                return cards.indexOf(CharacterCard.ASSASSIN);

            }
            //TODO
        }
        return random.nextInt(cards.size()); //return a random number between 0 and the size of the list
    }


    public boolean thereIsSomeoneWithALotOfGolds() {
        for (Player player : getListCopyPlayers()) {
            if (player.getGolds() >= 3 && player != this) {
                return true;
            }
        }
        return false;
    }

    public boolean thereIsSomeoneWithALotOfCards() {
        for (Player player : getListCopyPlayers()) {
            if (player.getHands().size() > this.getHands().size() && player != this) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Player choosePlayerToDestroy(List<Player> players) {
        if (targetedPlayerWhenIsLastBefore != null) {
            return targetedPlayerWhenIsLastBefore;
        }
        int rand = random.nextInt(2);
        if (rand == 0) {
            return null;
        } else {
            return players.get(random.nextInt(players.size()));
        }
    }

    @Override
    public DistrictCard chooseDistrictToDestroy(Player player, List<DistrictCard> districtCards) {
        DistrictCardComparator districtCardComparator = new DistrictCardComparator();
        List<DistrictCard> cards = player.getBoard();
        cards.sort(districtCardComparator);
        return cards.get(0);
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
    public boolean wantToUseLaboratoryEffect() {
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
                return DispatchState.DESTROY;
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
        int nbCardPlayer = this.getHands().size();
        for (Player p : players) {
            int nbCardOther = p.getHands().size();
            if (nbCardOther > nbCardPlayer) {
                return DispatchState.EXCHANGE_PLAYER;
            }
        }
        return DispatchState.EXCHANGE_DECK;
    }

    @Override
    public boolean wantToUseEffect(boolean beforePuttingADistrict) {
        discoverValidCard();
        for (DistrictCard districtCard : validCards) {
            if (districtCard.getDistrictColor() == this.getPlayerRole().getCharacterColor() && beforePuttingADistrict) {
                return getPlayerRole() == CharacterCard.WARLORD;
            }
        }
        return true;
    }

    @Override
    public boolean wantsToUseSmithyEffect() {
        return random.nextInt(2) == 0;
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
        if (isBeforeLastRound) {
            //If a player that is set to win take the assassin and kill the warlord
            for (Player player : players) {
                if (player.getPlayerRole() == CharacterCard.ASSASSIN && playersThatIsSetToWin.contains(player) && getRoleKilledByAssassin() == CharacterCard.WARLORD && this.getNbCardsInHand() < player.getNbCardsInHand()) {
                    return player;
                }
            }
            if ((getCurrentChoiceOfCharactersCardsDuringTheRound().contains(CharacterCard.WARLORD) && !getDiscardedCardDuringTheRound().contains(CharacterCard.WARLORD)) || (getCurrentChoiceOfCharactersCardsDuringTheRound().contains(CharacterCard.BISHOP) && !getDiscardedCardDuringTheRound().contains(CharacterCard.BISHOP))) {
                for (Player player : playersThatIsSetToWin) {
                    if (players.indexOf(player) == 0) {
                        return player;
                    }
                }
            }
        }
        Player highNbCards = players.get(0);
        for (Player p : players) {
            //if equals we trade with someone who has the most district
            if (p.getHands().size() == highNbCards.getHands().size() && p.getBoard().size() > highNbCards.getBoard().size()) {
                highNbCards = p;
            } else if (p.getHands().size() > highNbCards.getHands().size()) {
                highNbCards = p;
            }
        }
        return highNbCards;
    }

    @Override
    public boolean wantToUseGraveyardEffect() {
        int choice = random.nextInt(2);
        return choice == 0;
    }

    public CharacterCard getTarget() {
        return target;
    }

    public Player getTargetedPlayerWhenIsLastBefore() {
        return targetedPlayerWhenIsLastBefore;
    }

    public void setTargetedPlayerWhenIsLastBefore(Player targetedPlayerWhenIsLastBefore) {
        this.targetedPlayerWhenIsLastBefore = targetedPlayerWhenIsLastBefore;
    }

    public void setTarget(CharacterCard target) {
        this.target = target;
    }

    public List<Player> getPlayersThatIsSetToWin() {
        return playersThatIsSetToWin;
    }

    public void setPlayersThatIsSetToWin(List<Player> playersThatIsSetToWin) {
        this.playersThatIsSetToWin = playersThatIsSetToWin;
    }

    public boolean isBeforeLastRound() {
        return isBeforeLastRound;
    }

    public void setBeforeLastRound(boolean beforeLastRound) {
        isBeforeLastRound = beforeLastRound;
    }
}


