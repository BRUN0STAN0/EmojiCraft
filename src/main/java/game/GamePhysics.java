package game;

import java.util.logging.Logger;

import util.LoggerUtil;

public class GamePhysics implements Runnable {
    private static final Logger logger = LoggerUtil.getLogger(GamePhysics.class);
    private final GameWorld gameWorld;
    private final Player player;
    private boolean running = true;

    public GamePhysics(GameWorld gameWorld, Player player) {
        this.gameWorld = gameWorld;
        this.player = player;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(400); // Ritardo tra ogni applicazione della gravità
                applyGravity();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Thread della fisica interrotto: " + e.getMessage());
            }
        }
    }

    private void applyGravity() {
        int playerX = player.getX();
        int playerY = player.getY();

        // Continua a spostare il giocatore verso il basso finché non raggiunge il terreno o un oggetto
        if (playerY + 1 < gameWorld.getHeight() -2 ) {
            player.move(0, 1, "🧎🏻‍♂️‍➡️"); // Sposta il giocatore verso il basso
            playerY = player.getY(); // Aggiorna la posizione del giocatore

            // Controlla se il giocatore entra in collisione con un oggetto
            if (gameWorld.checkItemCollision(player)) {
                System.out.println("Oggetto raccolto durante la caduta!");
               
            }
        }

        System.out.println("Il giocatore si è fermato in posizione: X=" + player.getX() + ", Y=" + player.getY());
    }

    public void stop() {
        running = false;
    }
}