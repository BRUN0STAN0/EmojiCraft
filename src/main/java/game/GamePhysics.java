package game;

import java.util.logging.Logger;

import util.LoggerUtil;
import util.GameSettings;

public class GamePhysics implements Runnable {
    private static final Logger logger = LoggerUtil.getLogger(GamePhysics.class);
    private final GameWorld gameWorld;
    private final Player player;
    private boolean running = true;
    private long lastGroundTime = 0;
    private static final long GROUND_THRESHOLD = 1000; // Millisecondi
    private volatile boolean manualMovement = false; // Meccanismo di blocco per movimento manuale
    private static final long PHYSICS_INTERVAL = GameSettings.getInstance().getPhysicsStrength(); // Forza fisica

    public GamePhysics(GameWorld gameWorld, Player player) {
        this.gameWorld = gameWorld;
        this.player = player;
    }

    @Override
    public void run() {
        System.out.println("Thread della fisica avviato.");
        while (running) {
            try {
                Thread.sleep(PHYSICS_INTERVAL); // Usa l'intervallo configurato
                applyGravity();
                gameWorld.updateItems(); // Aggiorna gli oggetti scaduti
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Thread della fisica interrotto: " + e.getMessage());
            }
        }
    }

    private void applyGravity() {
        try {
            if (manualMovement) {
                // Salta il ciclo della fisica se il movimento manuale è in corso
                System.out.println("Fisica in pausa per movimento manuale.");
                return;
            }

            int playerX = player.getX();
            int playerY = player.getY();

            // Controlla se il giocatore può scendere di una posizione
            if (playerY + 1 < gameWorld.getHeight() - 2) {
                player.move(0, 1, "🧍‍♂️"); // Sposta il giocatore di una posizione verso il basso
                System.out.println("Il giocatore è sceso in posizione: X=" + player.getX() + ", Y=" + player.getY());

                // Aggiungi controllo collisione dopo il movimento
                boolean itemCollected = gameWorld.checkItemCollision(player);
                if (itemCollected) {
                    System.out.println("Oggetto raccolto tramite gravità!");
                }
            }
        } catch (Exception e) {
            System.err.println("Errore durante l'applicazione della gravità: " + e.getMessage());
        }
    }

    private void checkGroundState() {
        int playerY = player.getY();

        // Controlla se il giocatore è fermo a terra
        if (playerY + 1 >= gameWorld.getHeight() || !gameWorld.isCellEmpty(player.getX(), playerY + 1)) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastGroundTime > GROUND_THRESHOLD) {
                // Imposta l'animazione corretta per il giocatore fermo a terra
                player.setEmoji("🧍🏻‍♂️"); // Usa un'emoji o un'animazione per il giocatore fermo
                lastGroundTime = currentTime; // Aggiorna il tempo dell'ultimo movimento
                System.out.println("Il giocatore è fermo a terra con animazione: 🧎");
            }
        } else {
            // Aggiorna il tempo dell'ultimo movimento se il giocatore non è fermo
            lastGroundTime = System.currentTimeMillis();
        }
    }

    public void stop() {
        running = false;
    }

    public void startManualMovement() {
        manualMovement = true;
    }

    public void endManualMovement() {
        manualMovement = false;
    }
}