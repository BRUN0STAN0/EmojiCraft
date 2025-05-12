package game;

public class Player {
    private int x;
    private int y;
    private String emoji = "🧍🏻‍♂️"; // Emoji predefinita

    public Player(int startX, int startY) {
        this.x = startX;
        this.y = startY;
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

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public void move(int deltaX, int deltaY, String newEmoji) {
        this.x += deltaX;
        this.y += deltaY;
        this.emoji = newEmoji; // Aggiorna l'emoji in base alla direzione
    }

    public void setPosition(int x, int y) {

        this.x = x;
        this.y = y;
    }
}