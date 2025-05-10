package map;

public class SingleItem implements  MapComponent {
    private int x, y;
    private String emoji;
    private boolean visible;

    @Override
    public String getSymbol() {
        return emoji;
    }

    public SingleItem(int x, int y, String emoji) {
        this.x = x;
        this.y = y;
        this.emoji = emoji;
        this.visible = true;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean v) {
        visible = v;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public String getEmoji() { return emoji; }

    @Override
    public void render(String[][] grid) {
        if (visible) {
            grid[y][x] = emoji;
        }
    }
}
