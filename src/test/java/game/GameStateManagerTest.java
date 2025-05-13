package game;

import model.Item;
import org.testng.annotations.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameStateManagerTest {
    @Test
    void testSaveAndLoadGameState() {
        // Creazione stato di gioco di esempio
        GameState expected = new GameState(2, 5, 100, List.of(new Item(1, 1, "🙂", 10)), 60);

        // Salva e ricarica lo stato del gioco
        GameStateManager.saveGameStateToJson(expected);
        GameState loaded = GameStateManager.loadGameStateFromJson();

        // Verifica i contenuti
        assertNotNull(loaded, "Lo stato del gioco dovrebbe essere caricato");
        assertEquals(expected.getPlayerX(), loaded.getPlayerX());
        assertEquals(expected.getItems().size(), loaded.getItems().size());
        assertEquals(expected.timeRemaining, loaded.timeRemaining);
    }

}
