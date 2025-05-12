package model;

import java.io.Serializable;

import map.MapComponent;

public class Wall implements   MapComponent ,  Serializable {
    private static final long serialVersionUID = 1L; // Aggiunto serialVersionUID
    private final int x, y;

    public Wall(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void render(String[][] grid) {
        grid[y][x] = "🧱";
    }
    @Override
    public String getSymbol() {
        return "🧱";
    }
}