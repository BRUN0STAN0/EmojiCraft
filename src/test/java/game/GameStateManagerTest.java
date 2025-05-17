package game;


import org.testng.annotations.Test;
import game.GameState;

import static org.junit.jupiter.api.Assertions.*;

public class GameStateManagerTest {
    @Test
    void testSaveAndLoadGameState() {
        // Creazione stato di gioco di esempio
        GameState expected = new GameState(2, 5, 100, new String[][]{{"C"}}, 10);

        // Salva e ricarica lo stato del gioco
        GameStateManager.saveGameStateToJson(expected);
        GameState loaded = GameStateManager.loadGameStateFromJson();

        // Verifica i contenuti
        assertNotNull(loaded, "Lo stato del gioco dovrebbe essere caricato");
        assertEquals(expected.getPlayerX(), loaded.getPlayerX());
        assertEquals(expected.getGrid(), loaded.getGrid());
        assertEquals(expected.getTimeRemaining(), loaded.getTimeRemaining());
    }

}