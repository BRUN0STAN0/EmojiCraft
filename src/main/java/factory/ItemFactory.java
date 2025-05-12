package factory;

import java.util.Random;

import model.Item;

public class ItemFactory {
    private static final String[] EMOJIS = {"🙂", "😄", "😁", "😍", "🤑"};
    private static final int[] SCORES = {5, 10, 20, 25, 50};
    private static final Random random = new Random();

    // Metodo per creare un oggetto in una posizione specifica
    public static Item createRandomItem(int x, int y) {
        int index = random.nextInt(EMOJIS.length);
        String emoji = EMOJIS[index];
        int score = SCORES[index];
        return new Item(x, y, emoji, score);
    }
}
