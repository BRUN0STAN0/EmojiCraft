import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import game.GamePhysics;
import game.GameState;
import game.GameWorld;
import game.GameStateManager;
import game.Player;
import model.Item;
import server.ServerManager;
import util.GameUtils;
import util.LoggerUtil;

public class Main {
    private static final Logger logger = LoggerUtil.getLogger(Main.class);
    private static final int GAME_DURATION_IN_SECONDS = 60; // 1 minuto
    private static AtomicBoolean gameActive = new AtomicBoolean(true); // Stato del gioco (attivo o terminato)
    private static int timerDuration;

    public static void main(String[] args) {
        logger.info("Inizializzazione del server...");

        GameWorld gameWorld = new GameWorld();
        Player player = new Player(2, 5);

        // Carica lo stato del gioco se esiste
        try {
            GameState gameState = GameStateManager.loadGameState();
            if (gameState != null) {
                logger.info("Stato del gioco trovato. Caricamento del gioco...");
                gameWorld.loadGame(player); // Carica lo stato del gioco
                timerDuration = gameState.timeRemaining; // Tempo rimanente dal salvataggio
            } else {
                logger.info("Nessun stato del gioco trovato. Inizializzazione di una nuova partita...");
                timerDuration = GAME_DURATION_IN_SECONDS; // Tempo predefinito
                createInitialGameState(); // Inizializza stato di gioco
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Errore durante il caricamento dello stato del gioco: {0}", e.getMessage());
            gracefulExit();
        }

        GameState gameState = GameStateManager.loadGameState();
        if (gameState != null) {
            // Recupera tempo rimanente dal file di salvataggio
            timerDuration = gameState.timeRemaining;
            logger.info("Tempo rimanente caricato dal salvataggio: " + timerDuration + " secondi");
        } else {
            // Tempo di default (3 minuti) se non c'è un salvataggio
            timerDuration = GAME_DURATION_IN_SECONDS;
        }

        // Avvia il motore di fisica
        GamePhysics gamePhysics = new GamePhysics(gameWorld, player);
        Thread physicsThread = new Thread(gamePhysics);
        physicsThread.start();

        // Avvia il timer di 3 minuti
        Thread timerThread = new Thread(() -> GameUtils.startGameTimer(gameWorld, player, gameActive, GAME_DURATION_IN_SECONDS));
        timerThread.start();

        gameWorld.movePlayer(player, "W", gamePhysics); // Esempio di chiamata di movimento

        // Avvia il gioco
        ServerManager serverManager = new ServerManager(gameWorld, player, gameActive, timerDuration);
        serverManager.startServer();

        // Salva lo stato del gioco alla chiusura
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            saveGameState(gameWorld, player);
            gamePhysics.stop(); // Ferma il thread della fisica
        }));
    }

    private static void createInitialGameState() {
        List<Item> initialItems = List.of(
            new Item(5, 5, "🍎", 10),
            new Item(10, 3, "🍌", 15)
        );
        GameStateManager.saveGameState(2, 5, 0, initialItems, GAME_DURATION_IN_SECONDS);
        logger.info("File game_state.dat creato con uno stato iniziale.");
        logger.info("Percorso file di salvataggio: " + System.getProperty("user.dir") + "/game_state.dat");
    }

    private static void saveGameState(GameWorld gameWorld, Player player) {
        try {
            gameWorld.saveGame(player);
            logger.info("Stato del gioco salvato con successo.");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Errore durante il salvataggio dello stato del gioco: {0}", e.getMessage());
        }
    }

    private static void startGameTimer(GameWorld gameWorld, Player player) {
        try {
            logger.info("Timer del gioco avviato.");
            Thread.sleep(GAME_DURATION_IN_SECONDS * 1000); // Aspetta 3 minuti
            gameActive.set(false); // Imposta il gioco come terminato
            logger.info("Tempo scaduto. Termina il gioco.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.log(Level.SEVERE, "Errore nel timer del gioco: {0}", e.getMessage());
        }
    }

    private static void gracefulExit() {
        logger.severe("Terminazione del programma a causa di un errore critico.");
        System.exit(1);
    }
}