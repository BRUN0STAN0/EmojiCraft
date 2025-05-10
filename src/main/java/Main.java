import java.util.Random;

import com.google.gson.Gson;

import factory.ItemFactory;
import map.ItemGroup;
import map.MapComponent;
import model.Item;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFiles;

public class Main {
    // Mappa e Giocatore
    private static final int WIDTH = 24;
    private static final int HEIGHT = 10;
    private static final int GRAVITY_DELAY_MS = 300;
    private static int score = 0;
    private static boolean lastCollected = false;


    private static int playerX = 2;
    private static int playerY = 5;
    private static String playerState = "idle";
    private static boolean runNext = false;

    // Oggetto raccoglibile
    private static final ItemGroup items = new ItemGroup();
    private static final Random random = new Random();

    public static void main(String[] args) {
        // Aggiungi un oggetto iniziale
        items.add(ItemFactory.createRandomItem());
        createGround();
        // Configura il server
        staticFiles.externalLocation("C:/Users/bruno/Desktop/OOP/EmojiCraftMaven/public");
        port(4567);

        
        // Thread per la gravità
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(GRAVITY_DELAY_MS);
                } catch (InterruptedException e) {
                    return; // Termina il thread in caso di interruzione
                }
                if (playerY < HEIGHT - 3) {
                    playerY++;
                    checkItemCollision();
                }
            }
        }).start();

        Gson gson = new Gson();

        // Endpoint per ottenere lo stato del mondo
        get("/world", (req, res) -> {
            res.type("application/json");
            String[][] grid = new String[HEIGHT][WIDTH];
            for (int y = 0; y < HEIGHT; y++) {
                for (int x = 0; x < WIDTH; x++) {
                    grid[y][x] = " ";
                }
            }

            // Disegna gli oggetti
            items.render(grid);

            // Emoji del giocatore
            String emoji = switch (playerState) {
                case "idle" -> "🧍🏻‍♂️";
                case "walk" -> "🚶🏻‍♂️‍➡️";
                case "walk-left" -> "🚶🏻‍♂️";
                case "run" -> "🏃🏻‍♂️‍➡️";
                case "run-left" -> "🏃🏻‍♂️";
                case "jump" -> "🧎🏻‍♂️‍➡️";
                case "jump-left" -> "🧎🏻‍♂️";
                case "rotate" -> "🤸🏻‍♂️";
                default -> "🧍🏻‍♂️";
            };
            grid[playerY][playerX] = emoji;
            boolean collected = lastCollected;
            lastCollected = false; // reset dopo invio
            return gson.toJson(new WorldResponse(grid, score, collected));

        });

        // Endpoint per il movimento del giocatore
        post("/move", (req, res) -> {
            String dir = req.queryParams("dir");

            if (dir != null) {
                switch (dir.toUpperCase()) {
                    case "W" -> {
                        new Thread(() -> {
                            setTemporaryState("jump", 600);
                            for (int i = 0; i < 5; i++) {
                                if (playerY > 0) {
                                    playerY--;
                                    checkItemCollision(); // Raccogli oggetti durante il salto
                                }
                                try {
                                    Thread.sleep(50);
                                } catch (InterruptedException ignored) {
                                }
                            }
                        }).start();
                    }
                    case "S" -> {
                        playerY = Math.min(HEIGHT - 1, playerY + 1);
                        setTemporaryState("jump", 1000);
                    }
                    case "A" -> {
                        playerX = Math.max(0, playerX - 1);
                        setTemporaryState(runNext ? "run-left" : "walk-left", 800);
                        runNext = !runNext;
                    }
                    case "D" -> {
                        playerX = Math.min(WIDTH - 1, playerX + 1);
                        setTemporaryState(runNext ? "run" : "walk", 800);
                        runNext = !runNext;
                    }
                }
            }

            // Controlla collisioni dopo il movimento
            boolean collected = checkItemCollision();
            return collected ? "collected" : "OK";
        });
    }

    // Imposta uno stato temporaneo per il giocatore
    private static void setTemporaryState(String newState, int durationMs) {
        playerState = newState;
        new Thread(() -> {
            try {
                Thread.sleep(durationMs);
                playerState = "idle";
            } catch (InterruptedException ignored) {
            }
        }).start();
    }

    // Controlla se il giocatore ha raccolto un oggetto
    private static boolean checkItemCollision() {
        var iterator = items.getComponents().iterator();
        while (iterator.hasNext()) {
            MapComponent item = iterator.next();
            if (item instanceof Item i) {
                if (i.getX() == playerX && i.getY() == playerY) {
                    iterator.remove();
                    score++;
                    lastCollected = true; // <-- FLAG impostato

                    new Thread(() -> {
                        try {
                            Thread.sleep(1000);
                            items.add(ItemFactory.createRandomItem());
                        } catch (InterruptedException ignored) {}
                    }).start();

                    return true;
                }
            }
        }
        return false;
    }

    private static class WorldResponse {
        String[][] grid;
        int score;
        boolean collected; // nuovo campo

        public WorldResponse(String[][] grid, int score, boolean collected) {
            this.grid = grid;
            this.score = score;
            this.collected = collected;
        }
    }

private static void createGround() {
    for (int y = HEIGHT - 2; y < HEIGHT; y++) {  // da HEIGHT-3 a HEIGHT-1
        for (int x = 0; x < WIDTH; x++) {
            items.add(new model.Wall(x, y));
        }
    }
}

}
