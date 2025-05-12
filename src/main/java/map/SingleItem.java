package map;

public class SingleItem implements  MapComponent {
    private final int x;
    private final int y;
    private final String emoji;
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

    @Override
    public int getX() { return x; }
    @Override
    public int getY() { return y; }
    public String getEmoji() { return emoji; }

    @Override
    public void render(String[][] grid) {
        if (visible) {
            grid[y][x] = emoji;
        }
    }
}
