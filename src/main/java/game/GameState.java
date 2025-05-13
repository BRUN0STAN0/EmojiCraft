package game;

import java.io.Serializable;
import java.util.List;
import model.Item;

public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int playerX;
    private final int playerY;
    private final int score;
    private final List<Item> items; // Lista di oggetti salvati
    public final int timeRemaining;

    public GameState(int playerX, int playerY, int score, List<Item> items, int timeRemaining) {
        this.playerX = playerX;
        this.playerY = playerY;
        this.score = score;
        this.items = items;
        this.timeRemaining = timeRemaining;
    }

    public int getPlayerX() {
        return playerX;
    }

    public int getPlayerY() {
        return playerY;
    }

    public int getScore() {
        return score;
    }

    public List<Item> getItems() {
        return items;
    }
}