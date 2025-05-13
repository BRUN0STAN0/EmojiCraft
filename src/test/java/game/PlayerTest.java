package game;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import game.Player;

class PlayerTest {

    @Test
    void testMove() {
        Player player = new Player(3, 3);

        // Muovi verso destra e in basso
        player.move(1, 1, "🧍");
        assertEquals(4, player.getX(), "Il giocatore dovrebbe spostarsi a destra");
        assertEquals(4, player.getY(), "Il giocatore dovrebbe spostarsi in basso");

        // Cambia posizione direttamente
        player.setPosition(2, 2);
        assertEquals(2, player.getX());
        assertEquals(2, player.getY());
    }

    @Test
    void testEmojiChange() {
        Player player = new Player(0, 0);

        // Cambia emoji del giocatore
        player.setEmoji("🚶‍♂️");
        assertEquals("🚶‍♂️", player.getEmoji(), "L'emoji dovrebbe essere corretta");
    }
}