package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class ItemTest {

    @Test
    public void testItemCreation() {
        Item item = new Item(1, 2, "🍎", 5);
        assertEquals(1, item.getX());
        assertEquals(2, item.getY());
        assertEquals("🍎", item.getEmoji());
        assertEquals(5, item.getScore());
    }

    @Test
    public void testSetPosition() {
        Item item = new Item(1, 2, "🍎", 5);
        item.setPosition(3, 4);
        assertEquals(3, item.getX());
        assertEquals(4, item.getY());
    }

    @Test
    public void testVisibility() {
        Item item = new Item(1, 2, "🍎", 5);
        assertTrue(item.isVisible());
        item.setVisible(false);
        assertFalse(item.isVisible());
    }
}
