package util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Logger;

import map.ItemGroup;

public class GameStateManager {
    private static final Logger logger = LoggerUtil.getLogger(GameStateManager.class);
    private static final String SAVE_FILE = "game_state.dat";

    public static void saveGameState(ItemGroup items, int score, int playerX, int playerY) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            oos.writeObject(items);
            oos.writeInt(score);
            oos.writeInt(playerX);
            oos.writeInt(playerY);
            logger.info("Game state saved successfully.");
        } catch (IOException e) {
            logger.severe("Failed to save game state: " + e.getMessage());
        }
    }

    public static GameState loadGameState() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVE_FILE))) {
            ItemGroup items = (ItemGroup) ois.readObject();
            int score = ois.readInt();
            int playerX = ois.readInt();
            int playerY = ois.readInt();
            logger.info("Game state loaded successfully.");
            return new GameState(items, playerX, playerY);
        } catch (IOException | ClassNotFoundException e) {
            logger.warning("Failed to load game state: " + e.getMessage());
            return null; // Ritorna null se il caricamento fallisce
        }
    }

    // Classe interna per rappresentare lo stato del gioco
    public static class GameState {
        public final ItemGroup items;
        public final int playerX;
        public final int playerY;

        public GameState(ItemGroup items, int playerX, int playerY) {
            this.items = items;
            this.playerX = playerX;
            this.playerY = playerY;
        }
    }
}
