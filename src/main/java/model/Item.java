package model;

import map.MapComponent;

public class Item implements MapComponent {

    @Override
    public String getSymbol() {
        return emoji;
    }
    private int x;
    private int y;
    private String emoji;
    private boolean visible;

    public Item(int x, int y, String emoji) {
        this.x = x;
        this.y = y;
        this.emoji = emoji;
        this.visible = true;
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

    @Override
    public void render(String[][] grid) {
        if (visible) {
            grid[y][x] = emoji;
        }
    }
}
