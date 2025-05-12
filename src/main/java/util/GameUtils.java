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
    int timeRemaining = initialDuration;

    try {
        logger.info("Timer del gioco avviato.");
        while (timeRemaining > 0 && gameActive.get()) {
            Thread.sleep(1000); // Aspetta un secondo
            timeRemaining--; // Decrementa il tempo in secondi

            gameWorld.setTimeRemaining(timeRemaining); // Aggiorna il tempo rimanente
            logger.info("Tempo rimanente: " + timeRemaining + " secondi");
        }

        // Quando il timer scade
        if (gameActive.get()) {
            gameActive.set(false); // Cambia stato del gioco
            logger.info("Il gioco è terminato. GameActive = " + gameActive.get());
        }

        gameWorld.setTimeRemaining(0);
        logger.info("Tempo scaduto.");
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        logger.log(Level.SEVERE, "Timer interrotto: {0}", e.getMessage());
    }
}
}