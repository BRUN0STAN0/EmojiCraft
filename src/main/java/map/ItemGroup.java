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
            int x = component.getX();
            int y = component.getY();

            // Verifica che gli indici siano validi
            if (x >= 0 && x < grid[0].length && y >= 0 && y < grid.length) {
                grid[y][x] = component.getSymbol(); // Usa il simbolo del componente
            } else {
                System.err.println("Errore: componente fuori dai limiti! X=" + x + ", Y=" + y);
            }
        }
    }

    public boolean isCellEmpty(int x, int y) {
        // Controlla se la cella è vuota
        return components.stream()
                .noneMatch(component -> component.getX() == x && component.getY() == y);
    }
}