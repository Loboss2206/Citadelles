package fr.cotedazur.univ.polytech.view;

import fr.cotedazur.univ.polytech.logger.LamaLevel;
import fr.cotedazur.univ.polytech.logger.LamaLogger;
import fr.cotedazur.univ.polytech.model.bot.DispatchState;
import fr.cotedazur.univ.polytech.model.bot.Player;
import fr.cotedazur.univ.polytech.model.card.CharacterCard;
import fr.cotedazur.univ.polytech.model.card.Color;
import fr.cotedazur.univ.polytech.model.card.DistrictCard;
import fr.cotedazur.univ.polytech.model.card.PurpleEffectState;
import fr.cotedazur.univ.polytech.model.deck.Deck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class GameViewTest {
    private GameView gameView;
    private Logger logger;
    private Player player;

    @BeforeEach
    public void setup() {
        logger = Mockito.mock(Logger.class);
        gameView = new GameView(logger);
        player = Mockito.mock(Player.class);
    }

    @Test
    public void shouldPrintStartRound() {
        gameView.printStartRound(1);
    }

    @Test
    public void shouldPrintEndRound() {
        gameView.printEndRound(1);
    }

    @Test
    public void shouldPrintPlayerActionForTwoGolds() {
        when(player.getName()).thenReturn("Player1");
        when(player.getGolds()).thenReturn(2);
        when(player.getPlayerRole()).thenReturn(CharacterCard.ASSASSIN);
        gameView.printPlayerAction(DispatchState.TWO_GOLDS, player);
    }

    @Test
    public void shouldPrintPlayerActionForDrawCard() {
        when(player.getName()).thenReturn("Player1");
        when(player.getHands()).thenReturn(Collections.emptyList());
        when(player.getPlayerRole()).thenReturn(CharacterCard.ASSASSIN);
        when(player.getBoard()).thenReturn(List.of(DistrictCard.LIBRARY));
        gameView.printPlayerAction(DispatchState.DRAW_CARD, player);
    }

    @Test
    public void shouldPrintPlayerActionForDrawCardWithoutLibrary() {
        when(player.getName()).thenReturn("Player1");
        when(player.getHands()).thenReturn(Collections.emptyList());
        when(player.getPlayerRole()).thenReturn(CharacterCard.ASSASSIN);
        gameView.printPlayerAction(DispatchState.DRAW_CARD, player);
    }

    @Test
    public void shouldPrintPlayerActionForPlaceDistrict() {
        when(player.getName()).thenReturn("Player1");
        when(player.getBoard()).thenReturn(List.of(DistrictCard.LIBRARY));
        when(player.getPlayerRole()).thenReturn(CharacterCard.ASSASSIN);
        gameView.printPlayerAction(DispatchState.PLACE_DISTRICT, player);
    }

    @Test
    public void shouldPrintPlayerActionForCantPlay() {
        when(player.getName()).thenReturn("Player1");
        when(player.getPlayerRole()).thenReturn(CharacterCard.ASSASSIN);
        gameView.printPlayerAction(DispatchState.CANT_PLAY, player);
    }

    @Test
    public void shouldPrintPurpleEffectForLaboratoryEffect() {
        gameView.printPurpleEffect(player, PurpleEffectState.LABORATORY_EFFECT);
    }

    @Test
    public void shouldPrintPurpleEffectForGraveyardEffect() {
        gameView.printPurpleEffect(player, DistrictCard.LIBRARY, PurpleEffectState.GRAVEYARD_EFFECT);
    }

    @Test
    public void shouldPrintPurpleEffectForHauntedCity() {
        gameView.printPurpleEffect(player, Color.BLUE, PurpleEffectState.HAUNTED_CITY);
    }

    @Test
    public void shouldPrintEndTurnOfPlayer() {
        when(player.getName()).thenReturn("Player1");
        when(player.getPlayerRole()).thenReturn(CharacterCard.ASSASSIN);
        gameView.printEndTurnOfPlayer(player);
    }

    @Test
    public void shouldPrintPlayersRanking() {
        when(player.getName()).thenReturn("Player1");
        when(player.getPoints()).thenReturn(10);
        when(player.getGolds()).thenReturn(5);
        when(player.getBoard()).thenReturn(List.of(DistrictCard.LIBRARY));
        gameView.printPlayersRanking(Collections.singletonList(player));
    }

    @Test
    public void shouldPrintCharacterCard() {
        gameView.printCharacterCard(1, "Character1", "Description1");
    }

    @Test
    public void shouldPrintPlayerPickACard() {
        when(player.getName()).thenReturn("Player1");
        gameView.printPlayerPickACard("Player1", List.of(CharacterCard.ASSASSIN));
    }

    @Test
    public void shouldPrintEndOfPicking() {
        gameView.printEndOfPicking("Player1");
    }

    @Test
    public void shouldPrintCharacterUsedEffect() {
        when(player.getName()).thenReturn("Player1");
        when(player.getUsedEffect()).thenReturn("ASSASSIN_effect");
        gameView.printCharacterUsedEffect(player);
    }

    @Test
    public void shouldPrintRecapOfAllPlayers() {
        when(player.getName()).thenReturn("Player1");
        when(player.getGolds()).thenReturn(5);
        when(player.getHands()).thenReturn(Collections.emptyList());
        when(player.getPlayerRole()).thenReturn(CharacterCard.ASSASSIN);
        gameView.printRecapOfAllPlayers(Collections.singletonList(player));
    }

    @Test
    public void shouldPrintBoardOfAllPlayers() {
        when(player.getName()).thenReturn("Player1");
        when(player.getBoard()).thenReturn(List.of(DistrictCard.LIBRARY));
        gameView.printBoardOfAllPlayers(Collections.singletonList(player));
    }

    @Test
    public void shouldPrintCharacterGetGolds() {
        when(player.getName()).thenReturn("Player1");
        gameView.printCharacterGetGolds(player, Color.BLUE, 5);
    }

    @Test
    public void shouldPrintGraveyardEffect() {
        when(player.getName()).thenReturn("Player1");
        gameView.printGraveyardEffect(player, DistrictCard.LIBRARY);
    }

    @Test
    public void shouldLogStatsWithSingleLine() {
        String[] line = {"0", "Player1", "Wins", "10"};
        gameView.displayStats(line);
        verify(logger, times(2)).log(eq(LamaLevel.DEMO), anyString());
    }

    @Test
    public void shouldLogStatsWithMultipleLines() {
        String[] line = {"0", "Player1", "Wins", "10", "Player2", "Wins", "8", "Player3", "Wins", "6", "Player4", "Wins", "4", "Player5", "Wins", "2"};
        gameView.displayStats(line);
        verify(logger, times(3)).log(eq(LamaLevel.DEMO), anyString());
    }

    @Test
    public void shouldGetErrorLogStatsWithEmptyLine() {
        String[] line = {};
        gameView.displayStats(line);
        verify(logger, times(1)).log(eq(LamaLevel.SEVERE), anyString());
    }

    @Test
    public void shouldNotLogStatsWithNullLine() {
        gameView.displayStats(null);
        verify(logger, times(1)).log(eq(LamaLevel.SEVERE), anyString());
    }

    @Test
    public void shouldPrintExchangeDeckCardMessageWhenPlayerExchangesCardsWithDeck() {
        Player playerMagician = Mockito.mock(Player.class);
        when(playerMagician.getName()).thenReturn("Player1");

        List<DistrictCard> cards = Arrays.asList(DistrictCard.LIBRARY, DistrictCard.MANOR);

        gameView.exchangeDeckCard(playerMagician, cards);

        verify(logger, times(1)).log(eq(LamaLevel.VIEW), eq("Le joueur : Player1 a échanger 2 cartes avec le deck"));
    }

    @Test
    public void shouldPrintExchangeDeckCardMessageWhenPlayerExchangesNoCardsWithDeck() {
        Player playerMagician = Mockito.mock(Player.class);
        when(playerMagician.getName()).thenReturn("Player1");

        List<DistrictCard> cards = Collections.emptyList();

        gameView.exchangeDeckCard(playerMagician, cards);

        verify(logger, times(1)).log(eq(LamaLevel.VIEW), eq("Le joueur : Player1 a échanger 0 cartes avec le deck"));
    }

    @Test
    public void shouldPrintExchangeDeckCardMessageWhenPlayerIsNull() {
        List<DistrictCard> cards = Arrays.asList(DistrictCard.LIBRARY, DistrictCard.MANOR);

        gameView.exchangeDeckCard(null, cards);

        verify(logger, times(1)).log(eq(LamaLevel.SEVERE), anyString());
    }

    @Test
    public void shouldPrintExchangeDeckCardMessageWhenCardsIsNull() {
        Player playerMagician = Mockito.mock(Player.class);
        when(playerMagician.getName()).thenReturn("Player1");

        gameView.exchangeDeckCard(playerMagician, null);

        verify(logger, times(1)).log(eq(LamaLevel.VIEW), eq("Le joueur : Player1 a échanger 0 cartes avec le deck"));
    }

    @Test
    public void shouldDisplayBotComparaisonWithNoDraws() {
        Map<String, Integer> winnerPerPlayer = new HashMap<>();
        winnerPerPlayer.put("Bot1", 500);
        winnerPerPlayer.put("Bot2", 500);
        winnerPerPlayer.put("Draw", 10);

        Map<String, Integer> scoringPerPlayer = new HashMap<>();
        scoringPerPlayer.put("Bot1", 1000);
        scoringPerPlayer.put("Bot2", 1000);

        gameView.diplayBotComparaison(winnerPerPlayer, scoringPerPlayer);

        verify(logger, times(7)).log(eq(LamaLevel.DEMO), anyString());
        verify(logger, times(2)).log(eq(LamaLevel.DEMO), contains("wins"));
        verify(logger, times(3)).log(eq(LamaLevel.DEMO), contains("%"));
        verify(logger, times(1)).log(eq(LamaLevel.DEMO), contains("Scoring avg per player"));
    }

    @Test
    public void shouldDisplayBotComparaisonWithDraws() {
        Map<String, Integer> winnerPerPlayer = new HashMap<>();
        winnerPerPlayer.put("Bot1", 400);
        winnerPerPlayer.put("Bot2", 400);
        winnerPerPlayer.put("Draw", 200);

        Map<String, Integer> scoringPerPlayer = new HashMap<>();
        scoringPerPlayer.put("Bot1", 1000);
        scoringPerPlayer.put("Bot2", 1000);

        gameView.diplayBotComparaison(winnerPerPlayer, scoringPerPlayer);

        verify(logger, times(7)).log(eq(LamaLevel.DEMO), anyString());
        verify(logger, times(2)).log(eq(LamaLevel.DEMO), contains("wins"));
        verify(logger, times(2)).log(eq(LamaLevel.DEMO), contains("Draw"));
        verify(logger, times(3)).log(eq(LamaLevel.DEMO), contains("%"));
        verify(logger, times(1)).log(eq(LamaLevel.DEMO), contains("Scoring avg per player"));
    }

    @Test
    public void shouldDisplayBotComparaisonWithEmptyMaps() {
        Map<String, Integer> winnerPerPlayer = new HashMap<>();
        Map<String, Integer> scoringPerPlayer = new HashMap<>();

        gameView.diplayBotComparaison(winnerPerPlayer, scoringPerPlayer);

        verify(logger, times(1)).log(eq(LamaLevel.SEVERE), anyString());
    }

    @Test
    public void shouldPrintKillPlayerMessage() {
        gameView.killPlayer(CharacterCard.ASSASSIN);
        verify(logger, times(1)).log(eq(LamaLevel.VIEW), eq("Le joueur ayant le role: " + CharacterCard.ASSASSIN.getCharacterName() + " est mort."));
    }

    @Test
    public void shouldPrintStolenPlayerMessage() {
        gameView.stolenPlayer(CharacterCard.THIEF);
        verify(logger, times(1)).log(eq(LamaLevel.VIEW), eq("Le joueur ayant le rôle: " + CharacterCard.THIEF.getCharacterName() + " s'est fait voler"));
    }

    @Test
    public void shouldPrintDistrictDestroyedMessage() {
        Player player1 = Mockito.mock(Player.class);
        Player player2 = Mockito.mock(Player.class);
        when(player1.getName()).thenReturn("Player1");
        when(player1.getPlayerRole()).thenReturn(CharacterCard.ASSASSIN);
        when(player2.getName()).thenReturn("Player2");
        when(player2.getPlayerRole()).thenReturn(CharacterCard.THIEF);

        gameView.printDistrictDestroyed(player1, player2, DistrictCard.LIBRARY);
        verify(logger, times(1)).log(eq(LamaLevel.VIEW), eq("Le joueur Player1 (" + CharacterCard.ASSASSIN.getCharacterName() + ") a détruit le quartier " + DistrictCard.LIBRARY.getDistrictName() + " du joueur Player2 (" + CharacterCard.THIEF.getCharacterName() + ")"));
    }

    @Test
    public void shouldPrintExchangePlayerCardMessage() {
        Player player1 = Mockito.mock(Player.class);
        Player player2 = Mockito.mock(Player.class);
        when(player1.getName()).thenReturn("Player1");
        when(player2.getName()).thenReturn("Player2");

        gameView.exchangePlayerCard(player1, player2);
        verify(logger, times(1)).log(eq(LamaLevel.VIEW), eq("Le joueur : Player1 s'est fait échanger ces cartes avec : Player2"));
    }

    @Test
    public void shouldPrintPickARoleCardError() {
        gameView.pickARoleCardError();
        verify(logger, times(1)).log(eq(LamaLevel.VIEW), eq("Aucun personnage avec ce nombre, Réessayez"));
    }


    @Test
    public void shouldPrintPurpleEffectForSchoolOfMagicEffect() {
        Player player = Mockito.mock(Player.class);
        when(player.getName()).thenReturn("Player1");
        gameView.printPurpleEffect(player, null, Color.BLUE, PurpleEffectState.SCHOOL_OF_MAGIC_EFFECT);
        verify(logger, times(1)).log(eq(LamaLevel.VIEW), contains("utilise l'effet de l'école de magie"));
    }

    @Test
    public void shouldPrintPurpleEffectForUnknownEffect() {
        Player player = Mockito.mock(Player.class);
        when(player.getName()).thenReturn("Player1");

        gameView.printPurpleEffect(player, null, null, null);
        verify(logger, times(1)).log(eq(LamaLevel.VIEW), contains("Effet violet non connu"));
    }

    @Test
    public void shouldPrintDrawDistrictEffectMessage() {
        Player player = Mockito.mock(Player.class);
        when(player.getName()).thenReturn("Player1");
        when(player.getUsedEffect()).thenReturn("ARCHITECT_drawDistrict");

        gameView.printCharacterUsedEffect(player);

        verify(logger, times(1)).log(eq(LamaLevel.VIEW), contains("utilise l'effet de l' l'architecte et pioche 2 cartes."));
    }

    @Test
    public void shouldPrintPlaceDistrictEffectMessage() {
        Player player = Mockito.mock(Player.class);
        when(player.getName()).thenReturn("Player1");
        when(player.getUsedEffect()).thenReturn("ARCHITECT_placeDistrict");

        gameView.printCharacterUsedEffect(player);

        verify(logger, times(1)).log(eq(LamaLevel.VIEW), contains("utilise l'effet de l'architecte et peut placer 3 quartiers."));
    }

    @Test
    public void shouldThrowExceptionForUnknownEffect() {
        Player player = Mockito.mock(Player.class);
        when(player.getName()).thenReturn("Player1");
        when(player.getUsedEffect()).thenReturn("ARCHITECT_unknownEffect");

        assertThrows(IllegalStateException.class, () -> gameView.printCharacterUsedEffect(player));
    }

    @Test
public void shouldPrintSingleDiscardedCardFaceUp() {
    Deck<CharacterCard> discard = new Deck<>();
    discard.add(CharacterCard.ASSASSIN);

    gameView.printDiscardedCardFaceUp(discard);

    verify(logger, times(1)).log(eq(LamaLevel.VIEW), eq("\nCarte defaussée face visible : " + CharacterCard.ASSASSIN.getCharacterName()));
}

@Test
public void shouldPrintMultipleDiscardedCardsFaceUp() {
    Deck<CharacterCard> discard = new Deck<>();
    discard.add(CharacterCard.ASSASSIN);
    discard.add(CharacterCard.THIEF);

    gameView.printDiscardedCardFaceUp(discard);

    verify(logger, times(1)).log(eq(LamaLevel.VIEW), eq("\nCartes defaussées visibles : " + CharacterCard.ASSASSIN.getCharacterName() + " et " + CharacterCard.THIEF.getCharacterName()));
}

@Test
public void shouldPrintDiscardedCardFaceDown() {
    gameView.printDiscardedCardFaceDown(CharacterCard.ASSASSIN);

    verify(logger, times(1)).log(eq(LamaLevel.VIEW), eq("Carte defaussée face cachée : " + CharacterCard.ASSASSIN.getCharacterName() + "\n"));
}


@Test
public void shouldPrintPurpleEffectForDragonGateEffect() {
    Player player = Mockito.mock(Player.class);
    when(player.getName()).thenReturn("Player1");
    gameView.printPurpleEffect(player, null, null, PurpleEffectState.DRAGON_GATE_EFFECT);
    verify(logger, times(1)).log(eq(LamaLevel.VIEW), contains("possède la carte 'Dracoport'"));
}

@Test
public void shouldPrintPurpleEffectForLibraryEffect() {
    Player player = Mockito.mock(Player.class);
    when(player.getName()).thenReturn("Player1");
    gameView.printPurpleEffect(player, null, null, PurpleEffectState.LIBRARY_EFFECT);
    verify(logger, times(1)).log(eq(LamaLevel.VIEW), contains("peut piocher 2 cartes"));
}

@Test
public void shouldPrintPurpleEffectForUniversityEffect() {
    Player player = Mockito.mock(Player.class);
    when(player.getName()).thenReturn("Player1");
    gameView.printPurpleEffect(player, null, null, PurpleEffectState.UNIVERSITY_EFFECT);
    verify(logger, times(1)).log(eq(LamaLevel.VIEW), contains("possède la carte 'Université'"));
}

@Test
public void shouldPrintPurpleEffectForObservatoryEffect() {
    Player player = Mockito.mock(Player.class);
    when(player.getName()).thenReturn("Player1");
    gameView.printPurpleEffect(player, null, null, PurpleEffectState.OBSERVATORY_EFFECT);
    verify(logger, times(1)).log(eq(LamaLevel.VIEW), contains("peut choisir entre 3 cartes"));
}


@Test
public void shouldPrintPurpleEffectForSmithyEffect() {
    Player player = Mockito.mock(Player.class);
    when(player.getName()).thenReturn("Player1");
    gameView.printPurpleEffect(player, null, null, PurpleEffectState.SMITHY_EFFECT);
    verify(logger, times(1)).log(eq(LamaLevel.VIEW), contains("utilise l'effet de la manufacture"));
}

@Test
public void shouldPrintPurpleEffectForKeepEffect() {
    Player player = Mockito.mock(Player.class);
    when(player.getName()).thenReturn("Player1");
    gameView.printPurpleEffect(player, null, null, PurpleEffectState.KEEP_EFFECT);
    String playerNameAndRole = "Le joueur " + player.getName() + " (" + (player.getPlayerRole() != null ? player.getPlayerRole().getCharacterName() : "") + ")";
    verify(logger, times(1)).log(eq(LamaLevel.VIEW), contains(playerNameAndRole + " possède un donjon qui ne peut pas être détruit par le warlord"));
}

@Test
public void shouldPrintPurpleEffectForNullPlayer() {
    gameView.printPurpleEffect(null, null, null, PurpleEffectState.LABORATORY_EFFECT);
    verify(logger, times(1)).log(eq(LamaLevel.VIEW), contains("Effet violet non connu"));
}
@Test
public void shouldPrintStartGameMessage() {
    gameView.printStartGame();
    verify(logger, times(1)).log(eq(LamaLevel.VIEW), eq("Début du jeu\n"));
}

@Test
public void shouldInitializeLoggerInConstructor() {
    GameView gameView = new GameView();
    Logger expectedLogger = Logger.getLogger(LamaLogger.class.getName());
    assertEquals(expectedLogger, gameView.getLogger());
}
}