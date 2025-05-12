package response;

public class WorldResponse {
    private final String[][] grid;
    private final int score;
    private final int recentScoreGained;
    private final boolean collected;

    public WorldResponse(String[][] grid, int score, int recentScoreGained, boolean collected) {
        this.grid = grid;
        this.score = score;
        this.recentScoreGained = recentScoreGained;
        this.collected = collected;
    }

    public String[][] getGrid() {
        return grid;
    }

    public int getScore() {
        return score;
    }

    public int getRecentScoreGained() {
        return recentScoreGained;
    }

    public boolean isCollected() {
        return collected;
    }
}