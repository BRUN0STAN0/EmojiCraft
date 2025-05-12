package factory;

import java.util.Random;

import model.Item;

public class NegativeItemFactory {
    private static final String[] EMOJIS = {"💀", "☠️", "👻", "🕷️", "🦂"}; // Emoji negative
    private static final int[] SCORES = {-10, -15, -20, -25, -30}; // Punteggi negativi
    private static final Random random = new Random();

    // Metodo per creare un oggetto negativo in una posizione specifica
    public static Item createRandomNegativeItem(int x, int y) {
        int index = random.nextInt(EMOJIS.length);
        String emoji = EMOJIS[index];
        int score = SCORES[index];
        return new Item(x, y, emoji, score);
    }
}