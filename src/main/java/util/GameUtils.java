package util;

import game.GameWorld;
import game.Player;
import game.GameStateManager;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameUtils {
    private static final Logger logger = LoggerUtil.getInstance().getGlobalLogger();

    public static void startGameTimer(GameWorld gameWorld, Player player, AtomicBoolean gameActive, int durationInSeconds) {
    int duration = durationInSeconds > 0 ? durationInSeconds : GameSettings.getInstance().getGameDurationInSeconds(); // Tempo dal JSON
    new Thread(() -> {
        try {
            for (int i = duration; i > 0; i--) {
                Thread.sleep(1000); // 1 secondo
                gameWorld.setTimeRemaining(i);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Timer interrotto: " + e.getMessage());
        } finally {
            gameActive.set(false); // Termina il gioco
            System.out.println("Tempo scaduto. Termina il gioco.");
        }
    }).start();
}
}