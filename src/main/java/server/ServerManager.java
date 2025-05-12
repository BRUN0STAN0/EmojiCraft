package server;

import com.google.gson.Gson;
import java.util.logging.Level;
import java.util.logging.Logger;

import exception.EmojiCraftException;
import game.GamePhysics;
import game.GameWorld;
import game.Player;
import response.WorldResponse;
import util.GameUtils;
import util.LoggerUtil;

import java.util.concurrent.atomic.AtomicBoolean;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFiles;

public class ServerManager {
    private static final Logger logger = LoggerUtil.getLogger(ServerManager.class);
    private final GameWorld gameWorld;
    private final Player player;
    private final AtomicBoolean gameActive; // Stato del gioco
    private final Gson gson = new Gson();
    private final int timerRemaining;

    public ServerManager(GameWorld gameWorld, Player player, AtomicBoolean gameActive, int timerRemaining) {
        this.gameWorld = gameWorld;
        this.player = player;
        this.gameActive = gameActive;
        this.timerRemaining = timerRemaining;
    }

    public void startServer() {
        staticFiles.externalLocation("C:/Users/bruno/Desktop/OOP/EmojiCraftMaven/public");
        port(4567);

        // Rotta per ottenere lo stato del mondo
        get("/world", (req, res) -> {
            res.type("application/json");

            String[][] grid = gameWorld.getWorldState(player);
            int score = gameWorld.getScore();
            int recentScore = gameWorld.getRecentScoreGained();
            boolean collected = gameWorld.isItemCollected();

            // Usa il tempo rimanente aggiornato da GameWorld
            int currentTimeRemaining = gameWorld.getTimeRemaining();

            return gson.toJson(new WorldResponse(grid, score, recentScore, collected, gameActive.get(), currentTimeRemaining));
        });

        // Rotta per gestire il movimento del giocatore
        post("/move", (req, res) -> {
            if (!gameActive.get()) {
                return gson.toJson(new WorldResponse(gameWorld.getWorldState(player), gameWorld.getScore(), 0, false, false, timerRemaining));
            }

            String dir = req.queryParams("dir");
            if (dir == null || !dir.matches("[WASD]")) {
                throw new EmojiCraftException("Invalid direction: " + dir);
            }
            boolean itemCollected = gameWorld.movePlayer(player, dir.toUpperCase(), new GamePhysics(gameWorld, player));
            res.type("application/json");
            return gson.toJson(gameWorld.getMoveResponse(player, itemCollected));
        });

        // Nuova rotta per avviare la partita
        post("/start", (req, res) -> {
            if (gameActive.get()) {
                logger.info("Il gioco è già attivo. Ignora la richiesta.");
                return "{\"message\": \"Game is already active\", \"gameActive\": true}";
            }

            gameActive.set(true); // Imposta il gioco come attivo
            logger.info("Gioco e Timer Avviato. gameActive = " + gameActive.get());


            // Reinizializza il giocatore
            player.setPosition(2, 5);
            logger.info("Giocatore reinizializzato alle posizioni iniziali.");

            // Riavvia il timer utilizzando GameUtils
            new Thread(() -> GameUtils.startGameTimer(gameWorld, player, gameActive, 180)).start();

            res.type("application/json");
            return "{\"message\": \"Game started\", \"gameActive\": true}";
        });

        post("/restart", (req, res) -> {
            gameWorld.resetGame();
            return "{\"message\": \"Game restarted\"}";
        });
    }
}