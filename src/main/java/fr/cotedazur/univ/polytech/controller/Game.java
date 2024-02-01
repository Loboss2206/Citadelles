package fr.cotedazur.univ.polytech.controller;

import fr.cotedazur.univ.polytech.logger.LamaLogger;
import fr.cotedazur.univ.polytech.model.bot.Player;
import fr.cotedazur.univ.polytech.model.bot.PlayerComparator;
import fr.cotedazur.univ.polytech.model.card.Color;
import fr.cotedazur.univ.polytech.model.card.DistrictCard;
import fr.cotedazur.univ.polytech.model.deck.Deck;
import fr.cotedazur.univ.polytech.model.deck.DeckFactory;
import fr.cotedazur.univ.polytech.model.golds.StackOfGolds;
import fr.cotedazur.univ.polytech.view.GameView;

import java.util.*;

public class Game {

    // Logger
    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(LamaLogger.class.getName());

    // All the players that play in the game
    private final List<Player> players;

    // The view of the game
    private final GameView view;

    // The comparator of the players
    private final PlayerComparator playerComparator;
    private final StackOfGolds stackOfGolds;
    // Random Object
    private final Random random = new Random();
    // The round number
    int roundNumber = 0;
    // All the decks
    private Deck<DistrictCard> districtDeck;
    private Deck<DistrictCard> districtDiscardDeck;
    private int maxRound = 100; //prevent an infinite game


    public Game(List<Player> players, GameView view) {
        this.view = view;
        this.playerComparator = new PlayerComparator();

        // Add players
        this.players = players;

        this.stackOfGolds = new StackOfGolds();


        // Choose a random king among the players
        chooseRandomKing();

        // Build the decks and shuffle them
        buildDecks();
    }

    /**
     * Choose a random king among the players on the start of the game
     */
    public void chooseRandomKing() {
        int randomIndex = random.nextInt(players.size());
        players.get(randomIndex).setCrowned(true);
    }

    /**
     * Build all the decks and shuffle them
     */
    protected void buildDecks() {
        LOGGER.info("Construction des decks");
        this.districtDeck = DeckFactory.createDistrictDeck();
        this.districtDiscardDeck = DeckFactory.createEmptyDistrictDeck();
        this.districtDeck.shuffle();
    }

    /**
     * Set the crowned player as the first player of the list
     */
    public void setCrownedPlayerToFirstPlace() {
        while (!players.get(0).isCrowned()) {
            players.add(players.remove(0));
        }
        LOGGER.info("Le joueur " + players.get(0).getName() + " est le roi, il est donc le premier à jouer");
    }

    /**
     * Start the game and the rounds
     */
    public void startGame() {
        //Print the greeting
        view.printStartGame();
        LOGGER.info("La partie débute avec " + players.size() + " joueurs: " + players);

        //Draw 4 cards of District for each player at the beginning of the game
        for (Player player : players) {
            for (int i = 0; i < 4; i++) {
                player.getHands().add(this.districtDeck.draw());
            }
        }

        //Start the rounds until a player has won
        do {
            //Set the crowned player as the first player of the list
            setCrownedPlayerToFirstPlace();

            //Create and start the round
            Round round = new Round(this.players, this.view, this.districtDeck, this.districtDiscardDeck, ++roundNumber, this.stackOfGolds);
            round.startRound();
        } while (!isGameFinished() && roundNumber != maxRound);

        // End of the game
        calculatePoints();
        sortByPoints(players);
        view.printPlayersRanking(players);
    }

    /**
     * Check if the game is finished
     *
     * @return true if a player has 8 districts or more in his board, false otherwise
     */
    public boolean isGameFinished() {
        for (Player player : players) {
            if (player.getBoard().size() >= 8) {
                LOGGER.info("Le joueur " + player.getName() + " a gagné la partie");
                return true;
            }
        }
        LOGGER.info("Aucun joueur n'a plus de 8 quartiers, la partie continue");
        return false;
    }

    /**
     * Calculate the points of each player
     */
    public void calculatePoints() {
        for (Player player : players) {
            for (DistrictCard card : player.getBoard()) {
                int i = (card == DistrictCard.DRAGON_GATE || card == DistrictCard.UNIVERSITY) ? 2 : 0;
                player.setPoints(player.getPoints() + card.getDistrictValue() + i);
                LOGGER.info("Le joueur " + player.getName() + " a gagné " + (card.getDistrictValue() + i) + " points grâce à son quartier " + card.getDistrictName());
            }

            //If the player has 5 different colors
            addBonusPointsForPlayerWhoHas5DifferentColors(player);

            //If first to complete 8 quarters
            addBonusPointsForPlayerWhoIsFirstToAdd8Districts(player);

            //If the player complete 8 quarters
            addBonusPointsForPlayerWhoAdd8Districts(player);

            //Bonus off purple cards (will be implemented later)
        }
    }

    /**
     * Add 3 points to the player if he has 5 different colors
     *
     * @param player the player to check
     */
    public void addBonusPointsForPlayerWhoHas5DifferentColors(Player player) {
        Map<Color, Boolean> colors = new HashMap<>();
        for (Color color : Color.values()) {
            colors.put(color, false);
        }

        if (player.hasCardOnTheBoard(DistrictCard.HAUNTED_CITY) && player.getWhatIsTheRoundWhereThePlayerPutHisHauntedCity() != roundNumber) {
            Color hauntedColor = player.chooseColorForHauntedCity();
            colors.put(hauntedColor, true);
            LOGGER.info(player.getName() + " a choisi la couleur " + hauntedColor.getColorName() + " pour la 'cours des miracles' ");
        }

        for (DistrictCard card : player.getBoard()) {
            if (card == DistrictCard.HAUNTED_CITY) continue;
            colors.put(card.getDistrictColor(), true);
        }

        if (colors.values().stream().filter(b -> b).count() == 5) {
            player.setPoints(player.getPoints() + 3);
            LOGGER.info("Le joueur " + player.getName() + " a gagné 3 points grâce à ses 5 couleurs différentes");
        }
    }

    /**
     * Add 4 points to the player if he is the first to complete 8 districts
     *
     * @param player the player to check
     */
    public void addBonusPointsForPlayerWhoIsFirstToAdd8Districts(Player player) {
        if (player.isFirstToAdd8district()) {
            LOGGER.info("Le joueur " + player.getName() + " a gagné 4 points car il est le premier à avoir 8 quartiers");
            player.setPoints(player.getPoints() + 4);
        }
    }

    /**
     * Add 2 points to the player if he completes 8 districts
     *
     * @param player the player to check
     */
    public void addBonusPointsForPlayerWhoAdd8Districts(Player player) {
        if (player.getBoard().size() >= 8) {
            LOGGER.info("Le joueur " + player.getName() + " a gagné 2 points car il a 8 quartiers ou +");
            player.setPoints(player.getPoints() + 2);
        }
    }

    /**
     * Sort the players by their amount of points
     *
     * @param playersList the list of players to sort
     */
    public void sortByPoints(List<Player> playersList) {
        playersList.sort(playerComparator);
        Collections.reverse(playersList);
        LOGGER.info("Les joueurs ont été triés par ordre de points");
    }

    public List<Player> getPlayers() {
        return players;
    }

    public String startGameTest() {
        startGame();
        return players.get(0).getClass().getName();
    }

    public StackOfGolds getStackOfCoins() {
        return stackOfGolds;
    }
}
