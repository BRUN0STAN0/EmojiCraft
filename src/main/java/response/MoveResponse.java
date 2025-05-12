package response;

public class MoveResponse {
    private final int playerX;
    private final int playerY;
    private final int score;
    private final boolean itemCollected;

    public MoveResponse(int playerX, int playerY, int score, boolean itemCollected) {
        this.playerX = playerX;
        this.playerY = playerY;
        this.score = score;
        this.itemCollected = itemCollected;
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

    public boolean isItemCollected() {
        return itemCollected;
    }
}
