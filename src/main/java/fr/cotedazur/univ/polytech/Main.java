package fr.cotedazur.univ.polytech;


import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.opencsv.*;
import com.opencsv.exceptions.CsvValidationException;
import fr.cotedazur.univ.polytech.controller.Game;
import fr.cotedazur.univ.polytech.logger.LamaLevel;
import fr.cotedazur.univ.polytech.logger.LamaLogger;
import fr.cotedazur.univ.polytech.model.bot.*;
import fr.cotedazur.univ.polytech.view.GameView;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.*;

public class Main {

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(LamaLogger.class.getName());

    public static void main(String... args) {
        JCommanderSpoke jCommanderSpoke = new JCommanderSpoke();
        JCommander.newBuilder().addObject(jCommanderSpoke).build().parse(args);

        // Logger setup
        String logFile = "game.log";
        LamaLogger.setupFileLog(true, logFile);
        LamaLogger.setupConsole(true, true);
        LOGGER.setLevel(LamaLevel.INFO); // Change to OFF to disable the logger or INFO to enable all, VIEW to just the view and DEMO to disable all but the demo

        // View setup
        GameView view = new GameView();

        // CSV setup
        Path path = FileSystems.getDefault().getPath("stats", "gamestats.csv");
        CSVWriter writer = null;

        if (jCommanderSpoke.csvMode) {
            writer = createNewCSVFile(path);
        }

        if (jCommanderSpoke.demoMode) {
            LOGGER.setLevel(LamaLevel.VIEW);
            LamaLogger.setupFileLog(false, logFile);
            LamaLogger.setupConsole(false, false);

            startDemoGame(jCommanderSpoke, writer, view);
        } else if (jCommanderSpoke.twoThousands) {
            LOGGER.setLevel(LamaLevel.DEMO);
            LamaLogger.setupFileLog(false, logFile);
            LamaLogger.setupConsole(false, false);

            startTwoThousandsGame(jCommanderSpoke, writer, view);
        } else if (jCommanderSpoke.csvMode) {
            LOGGER.setLevel(LamaLevel.DEMO);
            LamaLogger.setupFileLog(false, logFile);
            LamaLogger.setupConsole(false, false);

            startCSVGame(writer, view);
        } else {
            startBaseGame(jCommanderSpoke, writer, view);
        }

        if (jCommanderSpoke.csvMode) {
            assert writer != null;
            writeLastLineAndDisplayCSV(writer, path, view);
        }
    }

    /**
     * Start the base game when no command line arguments are given
     * @param jCommanderSpoke the command line arguments
     * @param writer the csv writer
     * @param view the view
     */
    private static void startBaseGame(JCommanderSpoke jCommanderSpoke, CSVWriter writer, GameView view) {
        Player bot1 = new BotStrong();
        Player bot2 = new BotWeak();
        Player bot3 = new BotStrong();
        Player bot4 = new BotWeak();

        List<Player> players = new ArrayList<>(List.of(bot1, bot2, bot3, bot4));

        Game game = new Game(players, view);
        game.startGame();

        // Write in csv if csv mode is enabled
        if (jCommanderSpoke.csvMode) {
            try {
                writePlayersStats2Strong2Weak(writer, players);
            } catch (NullPointerException e) {
                LOGGER.log(LamaLevel.SEVERE, "Error while writing in the csv file");
            }
        }
    }

    /**
     * Start the demo game
     * @param jCommanderSpoke the command line arguments
     * @param writer the csv writer
     * @param view the view
     */
    public static void startDemoGame(JCommanderSpoke jCommanderSpoke, CSVWriter writer, GameView view) {
        Player bot1 = new Richard();
        Player bot2 = new BotRandom();
        Player bot3 = new BotWeak();
        Player bot4 = new BotRandom();

        List<Player> players = new ArrayList<>(List.of(bot1, bot2, bot3, bot4));

        Game game = new Game(players, view);
        game.startGame();

        // Write in csv if csv mode is enabled
        if (jCommanderSpoke.csvMode) {
            try {
                writePlayersStats2Random1Richard1Weak(writer, players);
            } catch (NullPointerException e) {
                LOGGER.log(LamaLevel.SEVERE, "Error while writing in the csv file");
            }
        }
    }

    /**
     * Start the game with 2000 games
     * @param jCommanderSpoke the command line arguments
     * @param writer the csv writer
     * @param view the view
     */
    private static void startTwoThousandsGame(JCommanderSpoke jCommanderSpoke, CSVWriter writer, GameView view) {
        Map<String, Integer> scoringPerPlayer = new HashMap<>();
        Map<String, Integer> winnerPerPlayer = new HashMap<>();

        Player bot1;
        Player bot2;
        Player bot3;
        Player bot4;
        List<Player> players = new ArrayList<>();

        LOGGER.log(LamaLevel.DEMO, "1000 games with 2 bots strong and 2 bots weak");
        for (int i = 0; i < 1000; i++) {
            players.clear();
            bot1 = new BotStrong();
            bot2 = new BotWeak();
            bot3 = new BotStrong();
            bot4 = new BotWeak();
            players.addAll(List.of(bot1, bot2, bot3, bot4));
            launchCustomGame(players, view, winnerPerPlayer, scoringPerPlayer);

            // Write in csv if csv mode is enabled
            if (jCommanderSpoke.csvMode) {
                try {
                    writePlayersStats2Strong2Weak(writer, players);
                } catch (NullPointerException e) {
                    LOGGER.log(LamaLevel.SEVERE, "Error while writing in the csv file");
                }
            }
        }

        //Making the average of the score of each player and displaying the results
        createStatsAndDisplay(view, scoringPerPlayer, winnerPerPlayer, players);

        LOGGER.log(LamaLevel.DEMO, "1000 games with 4 bots strong");
        scoringPerPlayer = new HashMap<>();
        winnerPerPlayer.clear();

        for (int i = 0; i < 1000; i++) {
            players.clear();
            bot1 = new BotStrong();
            bot2 = new BotStrong();
            bot3 = new BotStrong();
            bot4 = new BotStrong();
            players.addAll(List.of(bot1, bot2, bot3, bot4));
            launchCustomGame(players, view, winnerPerPlayer, scoringPerPlayer);

            // Write in csv if csv mode is enabled
            if (jCommanderSpoke.csvMode) {
                try {
                    writePlayersStats4Strong(writer, players);
                } catch (NullPointerException e) {
                    LOGGER.log(LamaLevel.SEVERE, "Error while writing in the csv file");
                }
            }
        }
        //Making the average of the score of each player and displaying the results
        createStatsAndDisplay(view, scoringPerPlayer, winnerPerPlayer, players);
    }

    /**
     * Create the stats and display them
     * @param view the view
     * @param scoringPerPlayer the scoring per player
     * @param winnerPerPlayer the winner per player
     * @param players the list of players
     */
    public static void createStatsAndDisplay(GameView view, Map<String, Integer> scoringPerPlayer, Map<String, Integer> winnerPerPlayer, List<Player> players) {
        for (Player player : players) {
            scoringPerPlayer.put(player.getName(), scoringPerPlayer.get(player.getName()) / 1000);
        }

        //Displaying the results
        view.diplayBotComparaison(winnerPerPlayer, scoringPerPlayer);
    }

    /**
     * Start the game with 40 games and write the stats in the csv file
     * @param writer the csv writer
     * @param view the view
     */
    private static void startCSVGame(CSVWriter writer, GameView view) {
        Player bot1;
        Player bot2;
        Player bot3;
        Player bot4;
        List<Player> players = new ArrayList<>();
        Game game;

        for (int i = 0; i < 20; i++) {
            players.clear();

            bot1 = new BotStrong();
            bot2 = new BotWeak();
            bot3 = new BotStrong();
            bot4 = new BotWeak();

            players.addAll(List.of(bot1, bot2, bot3, bot4));

            game = new Game(players, view);
            game.startGame();

            try {
                writePlayersStats2Strong2Weak(writer, players);
            } catch (NullPointerException e) {
                LOGGER.log(LamaLevel.SEVERE, "Error while writing in the csv file");
            }

            players.clear();

            bot1 = new BotStrong();
            bot2 = new BotStrong();
            bot3 = new BotStrong();
            bot4 = new BotStrong();

            players.addAll(List.of(bot1, bot2, bot3, bot4));

            game = new Game(players, view);
            game.startGame();

            try {
                writePlayersStats4Strong(writer, players);
            } catch (NullPointerException e) {
                LOGGER.log(LamaLevel.SEVERE, "Error while writing in the csv file");
            }
        }
    }

    private static void launchCustomGame(List<Player> players, GameView view, Map<String, Integer> winnerPerPlayer, Map<String, Integer> scoringPerPlayer) {
        Player bot1 = players.get(0);
        Player bot2 = players.get(1);
        Player bot3 = players.get(2);
        Player bot4 = players.get(3);

        for (int i = 0; i < 4; i++) {
            Player bot = players.get(i);
            String b = bot instanceof BotWeak ? "BotWeak" + (i+1) : "BotRandom" + (i+1);
            b = bot instanceof BotStrong ? "BotStrong" + (i+1) : b;
            bot.setName(b);
        }

        players.clear();
        players.addAll(List.of(bot1, bot2, bot3, bot4));
        for (Player player : players) {
            winnerPerPlayer.put(player.getName(), winnerPerPlayer.getOrDefault(player.getName(), 0));
        }
        winnerPerPlayer.put("Draw", winnerPerPlayer.getOrDefault("Draw", 0));

        Game game = new Game(players, view);
        game.startGame();

        Player winner = game.getWinner();

        if (winner != null) {
            winnerPerPlayer.put(winner.getName(), winnerPerPlayer.get(winner.getName()) + 1);
        } else {
            winnerPerPlayer.put("Draw", winnerPerPlayer.get("Draw") + 1);
        }

        for (Player player : players) {
            scoringPerPlayer.put(player.getName(), scoringPerPlayer.getOrDefault(player.getName(), 0) + player.getPoints());
        }
    }

    /**
     * Create a new csv file
     *
     * @param path the path of the csv file
     * @return the csv writer for the file
     */
    public static CSVWriter createNewCSVFile(Path path) {
        CSVWriter writer = null;
        boolean newFile;

        try {
            // create directory stats
            path.getParent().toFile().mkdirs();
            newFile = path.toFile().createNewFile();
            writer = new CSVWriter(new FileWriter(path.toFile(), true));

            if (newFile) writeFirstLine(writer);
            else removeLastLinesOfStats(path);
        } catch (IOException e) {
            LOGGER.log(LamaLevel.SEVERE, "Error while writing in the csv file");
        }

        return writer;
    }

    /**
     * Write the last line of the csv file and display the stats
     *
     * @param writer the csv writer
     * @param path   the path of the csv file
     * @param view   the view
     */
    private static void writeLastLineAndDisplayCSV(CSVWriter writer, Path path, GameView view) {
        try {
            writer.close();
            addLastLines(path);
        } catch (IOException e) {
            LOGGER.log(LamaLevel.SEVERE, "Error while writing in the csv file");
        }
        List<String[]> lines = recoverAllLines(path);
        LOGGER.log(LamaLevel.DEMO, "");
        LOGGER.log(LamaLevel.DEMO, "Statistiques");
        for (String[] line : lines) {
            if (line[0].equals("STATS"))
                view.displayStats(line);
        }
    }

    /**
     * Write the players stats in the csv file for a game
     *
     * @param writer  the csv writer
     * @param players the list of players
     */
    public static List<String> addPlayersStats(CSVWriter writer, List<Player> players, List<String> line) {
        List<String> newLine = new ArrayList<>(line);
        for (Player player : players) {
            newLine.add(player.getClass().getSimpleName());
            newLine.add(player.getName());
            newLine.add(String.valueOf(player.getPoints()));
        }
        return newLine;
    }

    /**
     * Write the players stats in the csv file for a game with 2 strong and 2 weak bots
     * @param writer the csv writer
     * @param players the list of players
     */
    public static void writePlayersStats2Strong2Weak(CSVWriter writer, List<Player> players) {
        List<String> line = new ArrayList<>();
        line.add("2StrongVS2Weak");
        line = addPlayersStats(writer, players, line);

        writer.writeNext(line.toArray(String[]::new));
    }

    /**
     * Write the players stats in the csv file for a game with 4 strong bots
     * @param writer the csv writer
     * @param players the list of players
     */
    public static void writePlayersStats4Strong(CSVWriter writer, List<Player> players) {
        List<String> line = new ArrayList<>();
        line.add("4Strong");
        line = addPlayersStats(writer, players, line);

        writer.writeNext(line.toArray(String[]::new));
    }

    /**
     * Write the players stats in the csv file for a game with 2 random, 1 richard and 1 weak bot
     * @param writer the csv writer
     * @param players the list of players
     */
    public static void writePlayersStats2Random1Richard1Weak(CSVWriter writer, List<Player> players) {
        List<String> line = new ArrayList<>();
        line.add("2RandomVS1RichardVS1Weak");
        line = addPlayersStats(writer, players, line);

        writer.writeNext(line.toArray(String[]::new));
    }

    /**
     * Write the header of the csv file
     *
     * @param writer the csv writer
     */
    public static void writeFirstLine(CSVWriter writer) {
        List<String> line = new ArrayList<>();
        line.add("GameType");
        for (int i = 0; i < 4; i++) {
            line.add("BotType");
            line.add("BotName");
            line.add("Points");
        }
        try {
            writer.writeNext(line.toArray(String[]::new));
        } catch (NullPointerException e) {
            LOGGER.log(LamaLevel.SEVERE, "Error while writing in the csv file");
        }
    }

    /**
     * Remove the last line of the csv file
     *
     * @param path the path of the csv file
     */
    public static void removeLastLinesOfStats(Path path) {
        List<String[]> lines = recoverAllLines(path);

        if (!lines.isEmpty()) {
            while (lines.get(lines.size() - 1)[0].equals("STATS"))
                lines.remove(lines.size() - 1);
        }

        try (CSVWriter writer = new CSVWriter(new FileWriter(path.toFile()))) {
            writer.writeAll(lines);
        } catch (IOException e) {
            LOGGER.log(LamaLevel.SEVERE, "Error while writing in the csv file");
        }
    }

    /**
     * Recover all the lines of the csv file
     *
     * @param path the path of the csv file
     * @return the list of all the lines
     */
    public static List<String[]> recoverAllLines(Path path) {
        List<String[]> lines = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(path.toFile()))) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                lines.add(nextLine);
            }
        } catch (IOException | CsvValidationException e) {
            LOGGER.log(LamaLevel.SEVERE, "Error while writing in the csv file");
        }

        return lines;
    }

    /**
     * Add the last lines to the csv file
     * @param path the path of the csv file
     */
    public static void addLastLines(Path path) {
        // add all the type which are in the csv file in a list
        List<String> types = new ArrayList<>();
        List<String[]> lines = recoverAllLines(path);
        for (int i = 1; i < lines.size(); i++) {
            String[] s = lines.get(i);
            if (!types.contains(s[0])) types.add(s[0]);
        }
        for (String type : types) {
            addLineForType(path, type);
        }
    }

    /**
     * Compute the stats of the games and add the last line to the csv file
     *
     * @param path the path of the csv file
     */
    public static void addLineForType(Path path, String type) {
        CSVWriter writer = null;
        try {
            writer = new CSVWriter(new FileWriter(path.toFile(), true));
        } catch (IOException e) {
            LOGGER.log(LamaLevel.SEVERE, "Error while writing in the csv file");
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
            if (!s[0].equals(type)) continue;
            for (int j = 1; j < s.length; j += 3) {
                String botType = s[j];
                int botPoints = Integer.parseInt(s[j + 2]);

                countBotInGame.put(botType, countBotInGame.getOrDefault(botType, 0) + 1);

                if (botPoints > max) {
                    max = botPoints;
                    countWin.put(botType, countWin.getOrDefault(botType, 0) + 1);
                }

                countGamesBotUnit.put(botType, countGamesBotUnit.getOrDefault(botType, 0) + 1);
                countPoints.put(botType, countPoints.getOrDefault(botType, 0) + botPoints);
            }

            for (String s1 : countBotInGame.keySet()) {
                countGamesBot.put(s1, countGamesBot.getOrDefault(s1, 0) + 1);
            }

            max = 0;
            countGames++;
        }

        List<String> line = generateNewLastLine(countPoints, countGamesBotUnit, countGamesBot, countWin, countGames, type);

        try {
            assert writer != null;
            writer.writeNext(line.toArray(String[]::new));
            writer.close();
        } catch (IOException e) {
            LOGGER.log(LamaLevel.SEVERE, "Error while writing in the csv file");
        }
    }

    /**
     * Generate the last line of the csv file
     *
     * @param countPoints       the points of each type of bot
     * @param countGamesBotUnit the number of games for each bot of the type
     * @param countGamesBot     the number of games for each type of bot
     * @param countWin          the number of win for each type of bot
     * @param countGames        the number of games
     * @return the last line to add to the csv file
     */
    public static List<String> generateNewLastLine(Map<String, Integer> countPoints, Map<String, Integer> countGamesBotUnit, Map<String, Integer> countGamesBot, Map<String, Integer> countWin, int countGames, String type) {
        List<String> line = new ArrayList<>();
        line.add("STATS");
        line.add(type);
        line.add("Nombre de parties");
        line.add(String.valueOf(countGames));
        for (Map.Entry<String, Integer> entry : countGamesBotUnit.entrySet()) {
            String s = entry.getKey();
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
        return line;
    }

    /**
     * Class to parse the command line arguments
     */
    public static class JCommanderSpoke {
        @Parameter(names = {"--demo"})
        public boolean demoMode;

        @Parameter(names = {"--2thousands", "-2k"})
        public boolean twoThousands;

        @Parameter(names = {"--csv"})
        public boolean csvMode;
    }
}

