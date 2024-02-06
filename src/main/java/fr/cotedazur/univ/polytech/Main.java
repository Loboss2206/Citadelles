package fr.cotedazur.univ.polytech;


import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.opencsv.*;
import com.opencsv.exceptions.CsvValidationException;
import fr.cotedazur.univ.polytech.controller.Game;
import fr.cotedazur.univ.polytech.logger.LamaLevel;
import fr.cotedazur.univ.polytech.logger.LamaLogger;
import fr.cotedazur.univ.polytech.model.bot.BotRandom;
import fr.cotedazur.univ.polytech.model.bot.BotStrong;
import fr.cotedazur.univ.polytech.model.bot.BotWeak;
import fr.cotedazur.univ.polytech.model.bot.Player;
import fr.cotedazur.univ.polytech.view.GameView;

import java.io.*;
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

        if (jCommanderSpoke.csvMode) {
            boolean newFile = false;

            try {
                // create directory stats
                path.getParent().toFile().mkdirs();
                newFile = path.toFile().createNewFile();
                writer = new CSVWriter(new FileWriter(path.toFile(), true));
            } catch (IOException ignored) {
            }

            if (newFile) writeFirstLine(writer);
            else {
                try {
                    removeLastLine(path);
                } catch (IOException | CsvValidationException e) {
                    throw new RuntimeException(e);
                }
            }
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
            if (jCommanderSpoke.csvMode) writePlayersStats(writer, players);
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
                if (jCommanderSpoke.csvMode) writePlayersStats(writer, players);
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
                if (jCommanderSpoke.csvMode) writePlayersStats(writer, players);
            }
            for (Player player : players) {
                scoringPerPlayer.put(player.getName(), scoringPerPlayer.get(player.getName()) / 1000);
            }
            view.diplayBotComparaison(winnerPerPlayer, scoringPerPlayer);

        } else if (jCommanderSpoke.csvMode) {
            LOGGER.setLevel(LamaLevel.DEMO);

            for (int i = 0; i < 20; i++) {
                players.clear();

                bot1 = new BotStrong();
                bot2 = new BotWeak();
                bot3 = new BotStrong();
                bot4 = new BotWeak();

                players.addAll(List.of(bot1, bot2, bot3, bot4));

                game = new Game(players, view);
                game.startGame();
                writePlayersStats(writer, players);
            }
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
            if (jCommanderSpoke.csvMode) writePlayersStats(writer, players);
        }

        if (jCommanderSpoke.csvMode) {
            assert writer != null;
            try {
                writer.close();
                addLastLine(path);
            } catch (
                    IOException e) {
                throw new RuntimeException(e);
            }
            List<String[]> lines = recoverAllLines(path);
            view.displayStats(lines);
        }
    }

    private static void launchCustomGame(List<Player> players, GameView view, Map<String, Integer> winnerPerPlayer, Map<String, Integer> scoringPerPlayer, Player bot1, Player bot2, Player bot3, Player bot4) {
        bot1.setName(bot1.getClass().getName().contains("BotStrong") ? "BotStrong1" : (bot1.getClass().getName().contains("BotWeak") ? "BotWeak1" : "BotRandom1"));
        bot2.setName(bot2.getClass().getName().contains("BotStrong") ? "BotStrong2" : (bot2.getClass().getName().contains("BotWeak") ? "BotWeak2" : "BotRandom2"));
        bot3.setName(bot3.getClass().getName().contains("BotStrong") ? "BotStrong3" : (bot3.getClass().getName().contains("BotWeak") ? "BotWeak3" : "BotRandom3"));
        bot4.setName(bot4.getClass().getName().contains("BotStrong") ? "BotStrong4" : (bot4.getClass().getName().contains("BotWeak") ? "BotWeak4" : "BotRandom4"));
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

    public static void writeFirstLine(CSVWriter writer) {
        List<String> line = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            line.add("BotType");
            line.add("BotName");
            line.add("Points");
        }
        writer.writeNext(line.toArray(String[]::new));
    }

    public static void removeLastLine(Path path) throws IOException, CsvValidationException {
        List<String[]> lines = recoverAllLines(path);

        if (!lines.isEmpty()) lines.remove(lines.size() - 1);

        try (CSVWriter writer = new CSVWriter(new FileWriter(path.toFile()))) {
            writer.writeAll(lines);
        }
    }

    public static List<String[]> recoverAllLines(Path path) {
        List<String[]> lines = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(path.toFile()))) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                lines.add(nextLine);
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException(e);
        }

        return lines;
    }

    public static void addLastLine(Path path) {
        CSVWriter writer;
        try {
            writer = new CSVWriter(new FileWriter(path.toFile(), true));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<String[]> lines = recoverAllLines(path);
        Map<String, Integer> countPoints = new HashMap<>();
        Map<String, Integer> countGamesBot = new HashMap<>();
        Map<String, Integer> countGamesBotUnit = new HashMap<>();
        Map<String, Integer> countWin = new HashMap<>();
        int max = 0;
        int countGames = 0;

        for (int i = 1; i < lines.size(); i++) {
            Map<String, Integer> countBotInGame = new HashMap<>();

            String[] s = lines.get(i);
            for (int j = 0; j < s.length; j += 3) {
                String botType = s[j];
                int botPoints = Integer.parseInt(s[j + 2]);

                if (countBotInGame.containsKey(botType)) countBotInGame.put(botType, countBotInGame.get(botType) + 1);
                else countBotInGame.put(botType, 1);

                if (botPoints > max) {
                    max = botPoints;
                    if (countWin.containsKey(botType)) countWin.put(botType, countWin.get(botType) + 1);
                    else countWin.put(botType, 1);
                }
                if (countGamesBotUnit.containsKey(botType))
                    countGamesBotUnit.put(botType, countGamesBotUnit.get(botType) + 1);
                else countGamesBotUnit.put(botType, 1);

                if (countPoints.containsKey(botType))
                    countPoints.put(botType, countPoints.get(botType) + botPoints);
                else countPoints.put(botType, botPoints);
            }

            for (String s1 : countBotInGame.keySet()) {
                if (countGamesBot.containsKey(s1)) countGamesBot.put(s1, countGamesBot.get(s1) + 1);
                else countGamesBot.put(s1, 1);
            }
            max = 0;
            countGames++;
        }

        List<String> line = new ArrayList<>();
        line.add("Nombre de parties");
        line.add(String.valueOf(countGames));
        for (String s : countGamesBotUnit.keySet()) {
            int countPointsBot;
            if (countPoints.get(s) == null) countPointsBot = 0;
            else countPointsBot = countPoints.get(s);

            int countWinBot;
            if (countWin.get(s) == null) countWinBot = 0;
            else countWinBot = countWin.get(s);

            float average = (float) countPointsBot / countGamesBotUnit.get(s);
            float winRate = (float) countWinBot / countGamesBot.get(s) * 100;

            line.add(s);
            line.add("Nombre bots");
            line.add(String.valueOf(countGamesBotUnit.get(s)));
            line.add("Points");
            line.add(String.valueOf(countPointsBot));
            line.add("Nombre de victoires");
            line.add(String.valueOf(countWinBot));
            line.add("Moyenne de points");
            line.add(String.valueOf(average));
            line.add("Taux de victoire");
            line.add(String.valueOf(winRate));
        }

        writer.writeNext(line.toArray(String[]::new));
        try {
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static class JCommanderSpoke {
        @Parameter(names = {"--demo"})
        public boolean demoMode;

        @Parameter(names = {"--2thousands", "-2k"})
        public boolean twoThousands;

        @Parameter(names = {"--csv"})
        public boolean csvMode;
    }
}

