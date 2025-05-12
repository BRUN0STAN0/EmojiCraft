import java.util.logging.Logger;

import game.GamePhysics;
import game.GameWorld;
import game.Player;
import server.ServerManager;
import util.GameStateManager;
import util.LoggerUtil;

public class Main {
    private static final Logger logger = LoggerUtil.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Inizializzazione del server...");

        // Carica lo stato del gioco
        GameStateManager.GameState gameState = GameStateManager.loadGameState();
        GameWorld gameWorld = new GameWorld();
        Player player = new Player(2, 5);

        if (gameState != null) {
            gameWorld = new GameWorld(gameState.items); // Passa gli oggetti al costruttore
            player = new Player(gameState.playerX, gameState.playerY);
        }

        // Avvia il server
        ServerManager serverManager = new ServerManager(gameWorld, player);
        serverManager.startServer();

        // Avvia la fisica del gioco
        GamePhysics gamePhysics = new GamePhysics(gameWorld, player);
        Thread physicsThread = new Thread(gamePhysics);
        physicsThread.start();

        // Aggiungi un hook per fermare il thread della fisica quando il programma termina
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Arresto del thread della fisica...");
            gamePhysics.stop();
        }));
    }
}
