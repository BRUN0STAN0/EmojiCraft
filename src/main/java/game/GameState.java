package game;
import java.util.Arrays;

public class GameState {
    private int playerX;
    private int playerY;
    private int score;
    private String[][] grid;
    private int timeRemaining;

    // Costruttore
    public GameState(int playerX, int playerY, int score, String[][] grid, int timeRemaining) {
        this.playerX = playerX;
        this.playerY = playerY;
        this.score = score;
        this.grid = grid;
        this.timeRemaining = timeRemaining;
    }

    // Getter & Setter (opzionali, se necessario per manipolazione)
    public int getPlayerX() {
        return playerX;
    }

    public void setPlayerX(int playerX) {
        this.playerX = playerX;
    }

    public int getPlayerY() {
        return playerY;
    }

    public void setPlayerY(int playerY) {
        this.playerY = playerY;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String[][] getGrid() {
        return grid;
    }

    public void setGrid(String[][] grid) {
        this.grid = grid;
    }

    public int getTimeRemaining() {
        return timeRemaining;
    }

    public void setTimeRemaining(int timeRemaining) {
        this.timeRemaining = timeRemaining;
    }

    @Override
    public String toString() {
        return "GameState{" +
                "playerX=" + playerX +
                ", playerY=" + playerY +
                ", score=" + score +
                ", grid=" + Arrays.deepToString(grid) +
                ", timeRemaining=" + timeRemaining +
                '}';
    }
}