package fr.cotedazur.univ.polytech.model.bot;


import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import fr.cotedazur.univ.polytech.model.card.Color;
import fr.cotedazur.univ.polytech.model.card.DistrictCard;
import fr.cotedazur.univ.polytech.model.card.DistrictCardComparator;

import java.util.*;


public class Richard extends Player implements GameActions {

    private Random random = new Random();

    private Player targetedPlayerWhenIsLastBefore = null;

    public Richard() {
        super();
    }

    private CharacterCard target;
    private Player targetOfTheMagician;

    private List<Player> playersThatIsSetToWin = new ArrayList<>();

    private boolean isBeforeLastRound = false;

    @Override
    public DispatchState startChoice() {
        discoverValidCard();
        Set<Color> colorsOnBoard = colorInList(getBoard());
        for (DistrictCard districtCard : validCards) {
            if (!colorsOnBoard.contains(districtCard.getDistrictColor())) {
                return DispatchState.TWO_GOLDS;

            }
        }
        if (getGolds() <= 4) {
            return DispatchState.TWO_GOLDS;
        }
        if (getHands().isEmpty() || validCards.isEmpty() || getGolds() >= 6) {
            return DispatchState.DRAW_CARD;
        }
        return DispatchState.TWO_GOLDS;
    }


    @Override
    public DistrictCard choiceHowToPlayDuringTheRound() {
        return putADistrict();
    }

    @Override
    public DistrictCard putADistrict() {
        discoverValidCard();
        //List des différentes couleurs sur le Terrain
        Set<Color> colorsOnBoard = colorInList(getBoard());
        if (!validCards.isEmpty()) {
            List<DistrictCard> purpleCard = new ArrayList<>();
            List<DistrictCard> colorNotOnBoard = new ArrayList<>();
            List<DistrictCard> cardsThatMatchWithRoleColor = new ArrayList<>();
            for (DistrictCard districtCard : validCards) {
                if (districtCard.getDistrictColor() == Color.PURPLE) {
                    purpleCard.add(districtCard);
                }
                if (!colorsOnBoard.contains(districtCard.getDistrictColor())) {
                    colorNotOnBoard.add(districtCard);
                }
                if (districtCard.getDistrictColor() == getPlayerRole().getCharacterColor()) {
                    cardsThatMatchWithRoleColor.add(districtCard);
                }
            }
            if (!purpleCard.isEmpty()) return maxPrice(purpleCard);

            if (!cardsThatMatchWithRoleColor.isEmpty()) return maxPrice(cardsThatMatchWithRoleColor);

            if (!colorNotOnBoard.isEmpty()) return maxPrice(colorNotOnBoard);

            return maxPrice(validCards);
        }

        return null;
    }

    public Set<Color> colorInList(List<DistrictCard> districtCards) {
        Set<Color> listeUnique = new HashSet<>();
        for (DistrictCard districtCard : districtCards) {
            listeUnique.add(districtCard.getDistrictColor());
        }
        return listeUnique;
    }

    public DistrictCard maxPrice(List<DistrictCard> districtCards) {
        if (!districtCards.isEmpty()) {
            DistrictCard maxValue = districtCards.get(0);
            for (DistrictCard card : districtCards) {
                if (maxValue.getDistrictValue() < card.getDistrictValue()) {
                    maxValue = card;
                }
            }
            return maxValue;
        }
        return null;
    }

    @Override
    public CharacterCard selectWhoWillBeAffectedByThiefEffect(List<Player> players, List<CharacterCard> characterCards) {
        if (isBeforeLastRound) {
            if (getCurrentChoiceOfCharactersCardsDuringTheRound().contains(CharacterCard.WARLORD) && !getDiscardedCardDuringTheRound().contains(CharacterCard.WARLORD)) {
                for (Player player : playersThatIsSetToWin) {
                    if (players.indexOf(player) == 0) {
                        return CharacterCard.WARLORD;
                    }
                }
            } else if (getCurrentChoiceOfCharactersCardsDuringTheRound().contains(CharacterCard.BISHOP) && !getDiscardedCardDuringTheRound().contains(CharacterCard.BISHOP)) {
                for (Player player : playersThatIsSetToWin) {
                    if (players.indexOf(player) == 0) {
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

        if (isBeforeLastRound) {
            if (getCurrentChoiceOfCharactersCardsDuringTheRound().contains(CharacterCard.WARLORD) && !getDiscardedCardDuringTheRound().contains(CharacterCard.WARLORD)) {
                for (Player player : playersThatIsSetToWin) {
                    if (players.indexOf(player) == 0) {
                        return CharacterCard.WARLORD;
                    }
                }
            } else if (getCurrentChoiceOfCharactersCardsDuringTheRound().contains(CharacterCard.BISHOP) && !getDiscardedCardDuringTheRound().contains(CharacterCard.BISHOP)) {
                for (Player player : playersThatIsSetToWin) {
                    if (players.indexOf(player) == 0) {
                        return CharacterCard.BISHOP;
                    }
                }
            }
        }
        if (target != null) return target;

        if (target == CharacterCard.ARCHITECT) return target;
        if (target == CharacterCard.MAGICIAN) return target;
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


    public boolean isLastRound() {
        for (Player player : getListCopyPlayers()) {
            if (player != this && player.getBoard().size() == 7) {
                return true;
            }
        }
        return false;
    }


    public boolean isFirst(List<Player> players) {
        return numberOfDistrictOfFirstPlayer(players) == this.getBoard().size();
    }

    public int numberOfDistrictOfFirstPlayer(List<Player> players) {
        int countPlayer = 0;
        int maxCountPlayer = 0;
        for (Player player : players) {
            countPlayer = player.getBoard().size();
            if (countPlayer > maxCountPlayer) maxCountPlayer = countPlayer;
        }
        return maxCountPlayer;
    }


    public List<Player> numberOfDistrictInOrder(List<Player> players) {
        List<Player> playersInOrder = players;
        Collections.sort(playersInOrder, Comparator.comparingInt(Player::getNbCardsInHand));
        Collections.reverse(playersInOrder);
        return playersInOrder;
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
        return cards.isEmpty() ? 0 : cards.get(0).getDistrictValue();
    }

    private List<Player> playerThatCanNotChooseArchitect() {
        List<Player> playerThatDoNotChooseArchitect = new ArrayList<>();
        for (Player player : getListCopyPlayers()) {
            if ((player.getGolds() < 4 && (player.getBoard().size() < 6) && (player.getHands().size() < 2))) {
                playerThatDoNotChooseArchitect.add(player);
            }
        }
        return playerThatDoNotChooseArchitect;
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
                if (!getDiscardedCardDuringTheRound().contains(CharacterCard.KING)) {
                    for (Player player : playersThatIsSetToWin) {
                        if (getListCopyPlayers().indexOf(player) == 0) {
                            target = CharacterCard.KING;
                        }
                    }
                }
                return cards.indexOf(CharacterCard.ASSASSIN);
            } else if (cards.contains(CharacterCard.WARLORD)) {
                //If the king is not in the hand we target the king
                if (!getDiscardedCardDuringTheRound().contains(CharacterCard.KING) && !getDiscardedCardDuringTheRound().contains(CharacterCard.ASSASSIN)) {
                    for (Player player : playersThatIsSetToWin) {
                        //If the player is first
                        if (getListCopyPlayers().indexOf(player) == 0) {
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


        if (isLastRound()) {
            List<Player> playersOrdered = getListCopyPlayers();
            int playerGonnaWinIndex = playersOrdered.indexOf(playerWhoIsGonnaWin(playersOrdered));
            int selfIndex = playersOrdered.indexOf(this);

            // If the player  who gonna win is the first or second player
            if (selfIndex == 0 && playerGonnaWinIndex == 1 && (!cards.contains(CharacterCard.BISHOP) || !cards.contains(CharacterCard.WARLORD)) && cards.contains(CharacterCard.ASSASSIN)) {
                target = cards.contains(CharacterCard.BISHOP) ? CharacterCard.BISHOP : CharacterCard.WARLORD;
                return cards.indexOf(CharacterCard.ASSASSIN);

            }

            // If Richard is the third player and more
            Integer cardToChoose = combo(cards, playersOrdered, playerGonnaWinIndex);
            if (cardToChoose != null) return cardToChoose;

        }

        //King
        if ((cards.contains(CharacterCard.KING) && countNumberOfSpecifiedColorCard(Color.YELLOW) > 0) || (cards.contains(CharacterCard.KING) && this.isCrowned() && getListCopyPlayers().size() < 5)) {
            return cards.indexOf(CharacterCard.KING);
        } else if (cards.contains(CharacterCard.BISHOP) && (countNumberOfSpecifiedColorCard(Color.BLUE) > 0 || (hasValidCard() && getBoard().size() >= 5))) {
            return cards.indexOf(CharacterCard.BISHOP);
        }//To have 3 golds directly
        else if ((cards.contains(CharacterCard.MERCHANT) && countNumberOfSpecifiedColorCard(Color.GREEN) > 0) || (cards.contains(CharacterCard.MERCHANT) && getGolds() < 2)) {
            return cards.indexOf(CharacterCard.MERCHANT);
        } else if (cards.contains(CharacterCard.WARLORD) && (countNumberOfSpecifiedColorCard(Color.RED) > 0 || (firstHas1GoldDistrict(getListCopyPlayers()) && getCurrentNbRound() > 3))) {
            return cards.indexOf(CharacterCard.WARLORD);
        } else if (!getDiscardedCardDuringTheRound().contains(CharacterCard.ARCHITECT)) {
            List<Player> playersThatCanNotChooseArchitect = playerThatCanNotChooseArchitect();
            List<Player> playersOrdered = getListCopyPlayers();
            for (Player player : playersThatCanNotChooseArchitect) {
                if (playersOrdered.indexOf(player) > playersOrdered.indexOf(this) && playersOrdered.get(playersOrdered.indexOf(player)).getPlayerRole() != CharacterCard.ARCHITECT) {
                    if (!getDiscardedCardDuringTheRound().contains(CharacterCard.ASSASSIN) && cards.contains(CharacterCard.ASSASSIN)) {
                        target = CharacterCard.ARCHITECT;
                        return cards.indexOf(CharacterCard.ASSASSIN);
                    }
                } else if (playersOrdered.indexOf(player) >= playersOrdered.indexOf(this) && getGolds() >= 4 && cards.contains(CharacterCard.ARCHITECT)) {
                    return cards.indexOf(CharacterCard.ARCHITECT);
                }
            }
        } else if (cards.contains(CharacterCard.MAGICIAN) && getHands().isEmpty() && thereIsSomeoneWithALotOfCards()) {
            return cards.indexOf(CharacterCard.MAGICIAN);
        }
        //Thief is interesting at first but when the game progresses he is not interesting (according to tt-22a5e3f98e5243b9f1135d1caadc4cc7)
        else if (cards.contains(CharacterCard.THIEF) && getCurrentNbRound() <= 3 && getGolds() <= 2 && thereIsSomeoneWithALotOfGolds()) {
            return cards.indexOf(CharacterCard.THIEF);
        } else if (cards.contains(CharacterCard.ASSASSIN) && ((this.getHands().size() >= 4 && someoneHasNoCards(getListCopyPlayers())))) {
            target = CharacterCard.MAGICIAN;
            return cards.indexOf(CharacterCard.ASSASSIN);
        }
        return random.nextInt(cards.size()); //return a random number between 0 and the size of the list
    }

    Integer combo(List<CharacterCard> cards, List<Player> playersOrdered, int playerGonnaWinIndex) {
        if (playerGonnaWinIndex >= 2) {
            int selfIndex = playersOrdered.indexOf(this);
            if (cards.contains(CharacterCard.ASSASSIN) && cards.contains(CharacterCard.WARLORD) && cards.contains(CharacterCard.BISHOP)) {
                if (selfIndex == 0) {
                    return cards.indexOf(CharacterCard.WARLORD);
                } else if (selfIndex == 1) {
                    target = CharacterCard.BISHOP;
                    return cards.indexOf(CharacterCard.ASSASSIN);
                }
            } else if (!cards.contains(CharacterCard.BISHOP) && selfIndex == 0 && cards.contains(CharacterCard.ASSASSIN) && playersOrdered.get(1).getHands().size() >= playersOrdered.get(playerGonnaWinIndex - 1).getHands().size()) {
                target = CharacterCard.MAGICIAN;
                return cards.indexOf(CharacterCard.ASSASSIN);
            } else if (!cards.contains(CharacterCard.BISHOP) && selfIndex == 1 && playersOrdered.get(0).getHands().size() < 2 && cards.contains(CharacterCard.MAGICIAN)) {
                targetOfTheMagician = playersOrdered.get(playerGonnaWinIndex - 1);
                return cards.indexOf(CharacterCard.MAGICIAN);
            } else if (!cards.contains(CharacterCard.ASSASSIN) && selfIndex == 0 && cards.contains(CharacterCard.WARLORD)) {
                return cards.indexOf(CharacterCard.WARLORD);
            } else if (!cards.contains(CharacterCard.ASSASSIN) && selfIndex == 1 && cards.contains(CharacterCard.BISHOP)) {
                return cards.indexOf(CharacterCard.BISHOP);
            }
        }
        return null;
    }

    private Player playerWhoIsGonnaWin(List<Player> playersOrdered) {
        for (Player player : playersOrdered) {
            if (player.getBoard().size() == 7) {
                return player;
            }
        }
        return null;
    }

    public boolean firstHas1GoldDistrict(List<Player> players) {

        for (Player player : players) {
            if (player.equals(this)) continue;
            if (numberOfDistrictOfFirstPlayer(players) == player.getBoard().size()) {
                for (DistrictCard districtCard : player.getBoard()) {
                    if (districtCard.getDistrictValue() == 1) {
                        return true;
                    }
                }
            }
        }
        return false;
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
        List<Player> playersInOrder = numberOfDistrictInOrder(players);
        for (Player player : playersInOrder) {
            if (player.equals(this)) continue;
            if (has1CardDistrictOnBoard(player)) return player;
        }
        return null;
    }

    public boolean has1CardDistrictOnBoard(Player player) {
        for (DistrictCard districtCard : player.getBoard()) {
            if (districtCard.getDistrictValue() == 1) {
                return true;
            }
        }
        return false;
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
        if (getPlayerRole() == CharacterCard.KING || getPlayerRole() == CharacterCard.BISHOP || getPlayerRole() == CharacterCard.MERCHANT || getPlayerRole() == CharacterCard.WARLORD) {
            return getPlayerRole().getCharacterColor();
        }
        return Color.PURPLE;
    }

    @Override
    public Color chooseColorForHauntedCity() {
        Set<Color> colorsOnBoard = colorInList(getBoard());
        for (Color color : Color.values()) {
            if (!colorsOnBoard.contains(color)) {
                return color;
            }
        }
        return Color.PURPLE;
    }


    @Override
    public boolean wantToUseLaboratoryEffect() {
        discoverValidCard();
        for (DistrictCard card : this.getHands()) {
            if (!validCards.contains(card)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public DistrictCard chooseHandCardToDiscard() {
        if (!getHands().isEmpty()) {
            ArrayList<DistrictCard> listOfCardsForSort = (ArrayList<DistrictCard>) getHands();
            DistrictCardComparator districtCardComparator = new DistrictCardComparator();
            listOfCardsForSort.sort(districtCardComparator);
            return listOfCardsForSort.get(0);
        }
        return null;
    }

    @Override
    public void drawCard(Map<DispatchState, ArrayList<DistrictCard>> cardsThatThePlayerDontWantAndThatThePlayerWant, DistrictCard... cards) {
        ArrayList<DistrictCard> listOfCardsForSort = new ArrayList<>(List.of(cards));
        LOGGER.info("Cartes piochées : " + Arrays.toString(cards));
        DistrictCardComparator districtCardComparator = new DistrictCardComparator();
        listOfCardsForSort.sort(districtCardComparator);
        for (int i = 0; i < listOfCardsForSort.size(); i++) {
            if (i == 0 || (this.getBoard().contains(DistrictCard.LIBRARY) && i == 1)) {
                cardsThatThePlayerDontWantAndThatThePlayerWant.get(DispatchState.CARDS_WANTED).add(listOfCardsForSort.get(listOfCardsForSort.size() - 1 - i));
            } else {
                cardsThatThePlayerDontWantAndThatThePlayerWant.get(DispatchState.CARDS_NOT_WANTED).add(listOfCardsForSort.get(listOfCardsForSort.size() - 1 - i));
            }
        }
        LOGGER.info("Cartes jetées : " + cardsThatThePlayerDontWantAndThatThePlayerWant.get(DispatchState.CARDS_NOT_WANTED));
    }


    @Override
    public DispatchState whichWarlordEffect(List<Player> players) {
        for (Player player : players) {
            for (DistrictCard districtCard : player.getBoard()) {
                if (districtCard.getDistrictValue() <= 1) return DispatchState.DESTROY;
            }
        }
        return DispatchState.EARNDISTRICT_WARLORD;
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
        return getGolds() >= 3 && validCards.isEmpty();
    }

    @Override
    public List<DistrictCard> chooseCardsToChange() {
        List<DistrictCard> districtCards = new ArrayList<>();
        for (DistrictCard districtCard : this.getHands()) {
            if (!validCards.contains(districtCard)) {
                districtCards.add(districtCard);
            }
        }
        return districtCards;
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
        if (targetOfTheMagician != null) {
            return targetOfTheMagician;
        }
        for (Player p : players) {
            //if equals we trade with someone who has the most district
            if ((p.getHands().size() == highNbCards.getHands().size() && p.getBoard().size() > highNbCards.getBoard().size()) || p.getHands().size() > highNbCards.getHands().size()) {
                highNbCards = p;
            }
        }
        return highNbCards;
    }

    @Override
    public boolean wantToUseGraveyardEffect() {
        return true;
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


