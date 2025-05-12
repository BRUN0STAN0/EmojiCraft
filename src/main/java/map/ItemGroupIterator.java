
package map;

import java.util.List;
import java.util.NoSuchElementException;

public class ItemGroupIterator implements MapIterator {
    private final List<MapComponent> components;
    private int position = 0;

    public ItemGroupIterator(List<MapComponent> components) {
        this.components = components;
    }

    @Override
    public boolean hasNext() {
        return position < components.size();
    }

    @Override
    public MapComponent next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more components.");
        }
        return components.get(position++);
    }
}
