package util;

import game.GameWorld;
import game.Player;
import game.GameStateManager;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameUtils {
    private static final Logger logger = LoggerUtil.getLogger(GameUtils.class);

    public static void startGameTimer(GameWorld gameWorld, Player player, AtomicBoolean gameActive, int initialDuration) {
    int timeRemaining = initialDuration;  // Tempo iniziale

    try {
        logger.info("Timer del gioco avviato.");
        while (timeRemaining > 0 && gameActive.get()) {
            Thread.sleep(1000); // Aspetta un secondo
            timeRemaining--; // Decrementa il tempo rimanente
            gameWorld.setTimeRemaining(timeRemaining); // Aggiorna il tempo rimanente in GameWorld

            // Salva periodicamente lo stato del gioco
            if (timeRemaining % 10 == 0 || timeRemaining == 1) { // Salva ogni 10 secondi o all'ultimo secondo
                GameStateManager.saveGameState(
                    player.getX(), 
                    player.getY(), 
                    gameWorld.getScore(), 
                    gameWorld.getItems(), 
                    timeRemaining
                );
                logger.info("Stato del gioco con minutaggio salvato: " + timeRemaining + " secondi rimanenti");
            }
        }

        // Quando il tempo scade
        gameActive.set(false);
        gameWorld.setTimeRemaining(0); // Assicurati che arrivi a 0
        logger.info("Tempo scaduto. Termina il gioco.");
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        logger.log(Level.SEVERE, "Timer interrotto: {0}", e.getMessage());
    }
}
}