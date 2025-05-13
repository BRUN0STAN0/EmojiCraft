package game;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import model.Item;

class GameWorldTest {

    @Test
    void testMovePlayer() {
        GameWorld gameWorld = new GameWorld();

        // Inizializza il giocatore
        Player player = new Player(5, 5);

        // Muovi il giocatore a destra
        gameWorld.movePlayer(player, "D", null);
        assertEquals(6, player.getX(), "Il giocatore dovrebbe muoversi a destra");

        // Muovi il giocatore verso l'alto
        gameWorld.movePlayer(player, "W", null);
        assertEquals(4, player.getY(), "Il giocatore dovrebbe muoversi in alto");
    }

    @Test
    void testItemCollision() {
        GameWorld gameWorld = new GameWorld();

        // Aggiungi un oggetto in posizione (5, 5)
        Item item = new Item(5, 5, "🙂", 10);
        gameWorld.getItems().add(item);

        // Posiziona il giocatore sull'oggetto
        Player player = new Player(5, 5);

        // Controlla collisione
        boolean collected = gameWorld.checkItemCollision(player);
        assertTrue(collected, "Il giocatore dovrebbe raccogliere l'oggetto");
    }
}