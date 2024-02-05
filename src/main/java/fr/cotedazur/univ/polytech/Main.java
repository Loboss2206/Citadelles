package fr.cotedazur.univ.polytech;


import com.beust.jcommander.JCommander;
import com.opencsv.CSVWriter;
import fr.cotedazur.univ.polytech.controller.Game;
import fr.cotedazur.univ.polytech.logger.LamaLevel;
import fr.cotedazur.univ.polytech.logger.LamaLogger;
import fr.cotedazur.univ.polytech.model.bot.BotRandom;
import fr.cotedazur.univ.polytech.model.bot.BotStrong;
import fr.cotedazur.univ.polytech.model.bot.BotWeak;
import fr.cotedazur.univ.polytech.model.bot.Player;
import fr.cotedazur.univ.polytech.view.GameView;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(LamaLogger.class.getName());

    public static void main(String... args) {
        JCommanderSpoke jCommanderSpoke = new JCommanderSpoke();
        JCommander.newBuilder().addObject(jCommanderSpoke).build().parse(args);
        // Logger setup
        LamaLogger.setupFileLog(true, "game.log");
        LamaLogger.setupConsole(true, true);
        LOGGER.setLevel(LamaLevel.VIEW); // Change to OFF to disable the logger or INFO to enable all, VIEW to just the view and DEMO to disable all but the demo
        // View setup
        GameView view = new GameView();
        Game game;
        List<Player> players = new ArrayList<>();

        // CSV setup
        Path path = FileSystems.getDefault().getPath("stats", "gamestats.csv");
        CSVWriter writer = null;

        try {
            // create directory stats
            path.getParent().toFile().mkdirs();
            path.toFile().createNewFile();
            writer = new CSVWriter(new FileWriter(path.toFile(), true));
        } catch (IOException ignored) {
        }

        Player bot1;
        Player bot2;
        Player bot3;
        Player bot4;
        if (jCommanderSpoke.demoMode) {
            // specials game
            LOGGER.setLevel(LamaLevel.VIEW);
            LamaLogger.setupFileLog(false, "game.log");
            LamaLogger.setupConsole(false, false);
            bot1 = new BotStrong();
            bot2 = new BotWeak();
            bot3 = new BotStrong();
            bot4 = new BotWeak();

            players.addAll(List.of(bot1, bot2, bot3, bot4));

            game = new Game(players, view);
            game.startGame();
            writePlayersStats(writer, players);
        } else if (jCommanderSpoke.twoThousands) {
            LOGGER.setLevel(LamaLevel.DEMO);
            LamaLogger.setupFileLog(false, "game.log");
            LamaLogger.setupConsole(false, false);
            Map<String, Integer> scoringPerPlayer = new HashMap<>();
            Map<String, Integer> winnerPerPlayer = new HashMap<>();

            LOGGER.log(LamaLevel.DEMO, "1000 games with 2 bots strong and 2 bots weak");
            for (int i = 0; i < 1000; i++) {
                bot1 = new BotStrong();
                bot2 = new BotWeak();
                bot3 = new BotStrong();
                bot4 = new BotWeak();
                launchCustomGame(players, view, winnerPerPlayer, scoringPerPlayer, bot1, bot2, bot3, bot4);
                writePlayersStats(writer, players);
            }

            //Making the average of the score of each player
            for (Player player : players) {
                scoringPerPlayer.put(player.getName(), scoringPerPlayer.get(player.getName()) / 1000);
            }

            //Displaying the results
            view.diplayBotComparaison(winnerPerPlayer, scoringPerPlayer);

            LOGGER.log(LamaLevel.DEMO, "1000 games with 4 bots strong");
            scoringPerPlayer = new HashMap<>();
            winnerPerPlayer.clear();

            for (int i = 0; i < 1000; i++) {
                bot1 = new BotStrong();
                bot2 = new BotStrong();
                bot3 = new BotStrong();
                bot4 = new BotStrong();
                launchCustomGame(players, view, winnerPerPlayer, scoringPerPlayer, bot1, bot2, bot3, bot4);
                writePlayersStats(writer, players);
            }
            for (Player player : players) {
                scoringPerPlayer.put(player.getName(), scoringPerPlayer.get(player.getName()) / 1000);
            }
            view.diplayBotComparaison(winnerPerPlayer, scoringPerPlayer);

        } else {
            // Normal game

            // Players setup
            for (int i = 0; i < 2; i++) {
                Player player = new BotRandom();
                Player player2 = new BotWeak();
                players.add(player);
                players.add(player2);
            }
            // Game setup
            game = new Game(players, view);
            game.startGame();
            writePlayersStats(writer, players);
        }

        assert writer != null;
        try {
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void launchCustomGame(List<Player> players, GameView view, Map<String, Integer> winnerPerPlayer, Map<String, Integer> scoringPerPlayer, Player bot1, Player bot2, Player bot3, Player bot4) {
        bot1.setName("BotStrong1");
        bot2.setName("BotStrong2");
        bot3.setName("BotStrong3");
        bot4.setName("BotStrong4");
        players.clear();
        players.addAll(List.of(bot1, bot2, bot3, bot4));
        for (Player player : players) {
            winnerPerPlayer.put(player.getName(), winnerPerPlayer.getOrDefault(player.getName(), 0));
        }
        winnerPerPlayer.put("Draw", winnerPerPlayer.getOrDefault("Draw", 0));

        Game game = new Game(players, view);
        game.startGame();

        Player winner = game.getWinner();

        if (winner.getName().equals(bot1.getName())) {
            winnerPerPlayer.put(bot1.getName(), winnerPerPlayer.get(bot1.getName()) + 1);
        } else if (winner.getName().equals(bot2.getName())) {
            winnerPerPlayer.put(bot2.getName(), winnerPerPlayer.get(bot2.getName()) + 1);
        } else if (winner.getName().equals(bot3.getName())) {
            winnerPerPlayer.put(bot3.getName(), winnerPerPlayer.get(bot3.getName()) + 1);
        } else if (winner.getName().equals(bot4.getName())) {
            winnerPerPlayer.put(bot4.getName(), winnerPerPlayer.get(bot4.getName()) + 1);
        } else {
            winnerPerPlayer.put("Draw", winnerPerPlayer.getOrDefault("Draw", 0) + 1);
        }
        for (Player player : players) {
            scoringPerPlayer.put(player.getName(), scoringPerPlayer.getOrDefault(player.getName(), 0) + player.getPoints());
        }
    }

    public static void writePlayersStats(CSVWriter writer, List<Player> players) {
        List<String> line = new ArrayList<>();
        for (Player player : players) {
            line.add(player.getClass().getSimpleName());
            line.add(player.getName());
            line.add(String.valueOf(player.getPoints()));
        }

        try {
            writer.writeNext(line.toArray(String[]::new));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
