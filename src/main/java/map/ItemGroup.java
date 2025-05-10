package map;

import java.util.ArrayList;
import java.util.List;

public class ItemGroup {
    private final List<MapComponent> components = new ArrayList<>();

    // Aggiungi un componente
    public void add(MapComponent component) {
        components.add(component);
    }

    // Rimuovi un componente
    public void remove(MapComponent component) {
        components.remove(component);
    }

    // Restituisci tutti i componenti
    public List<MapComponent> getComponents() {
        return components;
    }

    // Metodo per disegnare i componenti sulla mappa
    public void render(String[][] grid) {
        for (MapComponent component : components) {
            grid[component.getY()][component.getX()] = component.getSymbol();
        }
    }
}