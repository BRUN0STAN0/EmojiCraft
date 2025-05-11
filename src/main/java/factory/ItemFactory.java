package factory;

import java.util.Map;
import java.util.Random;

import model.Item;

public class ItemFactory {
    // Mappa Emoji -> Punteggio
    private static final Map<String, Integer> EMOJI_SCORES = Map.of(
        "💸", 5,
        "🍎", 1,
        "💎", 10
    );

    private static final String[] EMOJIS = EMOJI_SCORES.keySet().toArray(new String[0]);

    public static Item createRandomItem() {
        Random rand = new Random();
        String emoji = EMOJIS[rand.nextInt(EMOJIS.length)];
        int score = EMOJI_SCORES.get(emoji);
        int screenWidth = 20; // da aggiornare se necessario
        int screenHeight = 10;
        int x = rand.nextInt(screenWidth - 1);
        int y = rand.nextInt(screenHeight - 2);
        return new Item(x, y, emoji, score); // <-- Punteggio incluso
    }
}
