package fr.cotedazur.univ.polytech.model.bot;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import fr.cotedazur.univ.polytech.model.card.DistrictCard;
import fr.cotedazur.univ.polytech.model.deck.Deck;
import fr.cotedazur.univ.polytech.view.GameView;

public class BotRandom extends Player implements GameActions {

    private Random random = new Random();

    public BotRandom() {
        super();
    }

    @Override
    public String startChoice(Deck<DistrictCard> districtDeck) {
        if (getPlayerRole() == CharacterCard.ARCHITECT) useRoleEffect(Optional.of(districtDeck), Optional.empty());
        int randomIndex = random.nextInt(2);
        switch (randomIndex) {
            case 0 -> {
                return collectTwoGolds();
            }
            case 1 -> {
                return drawCard(districtDeck);
            }
            default -> {
                return null;
            }
        }
    }

    @Override
    public DistrictCard choiceHowToPlayDuringTheRound() {
        useRoleEffect(Optional.empty(), Optional.empty()); //Simple effects
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
    public void useRoleEffect(Optional<Deck<DistrictCard>> districtDeck, Optional<ArrayList<Player>> players) {
        int randomIndex = random.nextInt(2);
        if (randomIndex == 0) {
            if (districtDeck.isEmpty() && players.isPresent()) {
                if (getPlayerRole() == CharacterCard.THIEF) {
                    getPlayerRole().useEffect(this, players.get().get(random.nextInt(players.get().size())));
                }
            } else if (districtDeck.isPresent()) {
                getPlayerRole().useEffect(this, districtDeck.get());
            } else {
                getPlayerRole().useEffect(this);
            }
        }
    }

    @Override
    public int chooseCharacter(List<CharacterCard> cards) {
        return random.nextInt(cards.size()); //return a random number between 0 and the size of the list
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

    public Player copyPlayer() {
        return new BotRandom();
    }

    @Override
    public String WhichWarlordEffect() {
        return null;
    }
}
