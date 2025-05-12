package server;

import com.google.gson.Gson;

import exception.EmojiCraftException;
import game.GameWorld;
import game.Player;
import response.WorldResponse;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFiles;

public class ServerManager {
    private final GameWorld gameWorld;
    private final Player player;
    private final Gson gson = new Gson();

    public ServerManager(GameWorld gameWorld, Player player) {
        this.gameWorld = gameWorld;
        this.player = player;
    }

    public void startServer() {
        staticFiles.externalLocation("C:/Users/bruno/Desktop/OOP/EmojiCraftMaven/public");
        port(4567);

        get("/world", (req, res) -> {
            res.type("application/json");

            // Ottieni lo stato del mondo
            String[][] grid = gameWorld.getWorldState(player);
            int score = gameWorld.getScore();
            int recentScoreGained = gameWorld.getRecentScoreGained(); // Aggiungi un metodo per ottenere il punteggio recente
            boolean collected = gameWorld.isItemCollected(); // Aggiungi un metodo per verificare se un oggetto è stato raccolto

            // Crea la risposta
            WorldResponse response = new WorldResponse(grid, score, recentScoreGained, collected);

            // Serializza la risposta in JSON
            return gson.toJson(response);
        });

        post("/move", (req, res) -> {
            String dir = req.queryParams("dir");
            if (dir == null || !dir.matches("[WASD]")) {
                throw new EmojiCraftException("Invalid direction: " + dir);
            }

            boolean itemCollected = gameWorld.movePlayer(player, dir.toUpperCase());
            res.type("application/json");
            return gson.toJson(gameWorld.getMoveResponse(player, itemCollected));
        });
    }
}