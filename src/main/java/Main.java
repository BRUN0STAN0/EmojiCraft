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
    
    // Costanti di configurazione
    private static final int GAME_DURATION_IN_SECONDS = 60; // 1 minuto
    private static final int DEFAULT_PLAYER_X = 2;
    private static final int DEFAULT_PLAYER_Y = 5;

    // Stato globale del gioco
    private static final AtomicBoolean gameActive = new AtomicBoolean(true);
    private static int timerDuration;
    private static GamePhysics gamePhysics; // Riferimento a GamePhysics

    public static void main(String[] args) {
        logger.info("Inizializzazione del server e della logica di gioco...");

        // Inizializziamo il mondo e il giocatore
        GameWorld gameWorld = new GameWorld();
        Player player = new Player(DEFAULT_PLAYER_X, DEFAULT_PLAYER_Y);

        // Carichiamo lo stato del gioco esistente o creiamo uno stato iniziale
        initializeGameState(gameWorld, player);

        // Eseguiamo i componenti principali del gioco
        startGameThreads(gameWorld, player);

        // Configuriamo il server e iniziamo a rispondere alle richieste
        startServer(gameWorld, player);

        // Impostiamo il salvataggio dello stato su shutdown del programma
        setupShutdownHook(gameWorld, player);
    }

    /**
     * Carica lo stato del gioco o crea uno stato iniziale.
     */
    private static void initializeGameState(GameWorld gameWorld, Player player) {
        GameState gameState = GameStateManager.loadGameStateWithFallback();

        if (gameState != null) {
            logger.info("Stato del gioco trovato. Caricamento in corso...");
            gameWorld.loadGame(player); // Carichiamo i dati del mondo
            timerDuration = gameState.timeRemaining; // Tempo rimanente
        } else {
            logger.info("Nessun stato salvato trovato. Creazione di uno stato iniziale...");
            timerDuration = GAME_DURATION_IN_SECONDS; // Imposta il tempo di default
            createInitialGameState(gameWorld); // Passa il GameWorld
        }
    }

    /**
     * Crea e salva uno stato iniziale del gioco.
     */
    private static void createInitialGameState(GameWorld gameWorld) {
        gameWorld.resetGame(); // Resetta il mondo e crea il terreno e un oggetto iniziale
        GameStateManager.saveGameStateDual( // Salva il nuovo stato iniziale
            DEFAULT_PLAYER_X, 
            DEFAULT_PLAYER_Y, 
            0, 
            gameWorld.getItems(), // Ottiene gli item generati dal GameWorld
            GAME_DURATION_IN_SECONDS 
        );
        logger.info("Stato iniziale creato e salvato con successo tramite GameWorld.");
    }

    /**
     * Avvia i thread principali del gioco come fisica e timer.
     */
    private static void startGameThreads(GameWorld gameWorld, Player player) {
        // Motore di fisica del gioco
        gamePhysics = new GamePhysics(gameWorld, player);
        Thread physicsThread = new Thread(gamePhysics);
        physicsThread.start();

        // Timer del gioco
        Thread timerThread = new Thread(() -> {
            GameUtils.startGameTimer(gameWorld, player, gameActive, timerDuration);
        });
        timerThread.start();
    }

    /**
     * Configura e avvia il server per la gestione client-server.
     */
    private static void startServer(GameWorld gameWorld, Player player) {
        ServerManager serverManager = new ServerManager(gameWorld, player, gameActive, timerDuration);
        serverManager.startServer();
        logger.info("Server avviato correttamente e pronto a gestire le richieste.");
    }

    /**
     * Configura un hook per salvare automaticamente lo stato del gioco
     * alla chiusura del programma.
     */
    private static void setupShutdownHook(GameWorld gameWorld, Player player) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            gameWorld.setGameActive(false); // Ferma ogni logica dipendente dallo stato del gioco
            saveGameState(gameWorld, player);
            gamePhysics.stop(); // Ferma il thread della fisica
            logger.info("Il gioco è stato terminato correttamente.");
        }));
    }

    /**
     * Salva lo stato corrente del gioco.
     */
    private static void saveGameState(GameWorld gameWorld, Player player) {
        try {
            GameStateManager.saveGameStateDual(
                player.getX(),
                player.getY(),
                gameWorld.getScore(),
                gameWorld.getItems(),
                gameWorld.getTimeRemaining()
            );
            logger.info("Stato del gioco salvato correttamente.");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Errore durante il salvataggio dello stato del gioco: {0}", e.getMessage());
        }
    }
}