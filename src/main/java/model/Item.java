package model;

import map.MapComponent;

public class Item implements MapComponent {

    private int x;
    private int y;
    private String emoji;
    private boolean visible;
    private int score; // ✅ Nuovo campo

    public Item(int x, int y, String emoji, int score) {
        this.x = x;
        this.y = y;
        this.emoji = emoji;
        this.score = score;
        this.visible = true;
    }

    // 👇 Vecchio costruttore mantenuto per compatibilità, se ti serve ancora
    public Item(int x, int y, String emoji) {
        this(x, y, emoji, 1); // default score: 1
    }

    @Override
    public String getSymbol() {
        return emoji;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getEmoji() {
        return emoji;
    }

    public int getScore() {
        return score;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public void render(String[][] grid) {
        if (visible) {
            grid[y][x] = emoji;
        }
    }
}
