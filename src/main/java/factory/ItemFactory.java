package factory;
import java.util.Random;

import model.Item;

public class ItemFactory {
    public static Item createRandomItem() {
        String[] emojis = { "💸", "🍎", "💎" };
        String emoji = emojis[new Random().nextInt(emojis.length)];
        int screenWidth = 20; // Replace with actual screen width
        int screenHeight = 10; // Replace with actual screen height
        int x = new Random().nextInt(screenWidth - 1);
        int y = new Random().nextInt(screenHeight - 2);
        return new Item(x, y, emoji);
    }

     public static Item createGround() {
        String[] emojis = { "🎛️", "🎛️", "🎛️" };
        String emoji = emojis[new Random().nextInt(emojis.length)];
        int screenWidth = 20; // Replace with actual screen width
        int screenHeight = 10; // Replace with actual screen height
        int x = new Random().nextInt(screenWidth - 1);
        int y = new Random().nextInt(screenHeight - 2);
        return new Item(x, y, emoji);
    }
}