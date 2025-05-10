package map;

// Other imports
public interface MapComponent {
    void render(String[][] grid);
    // Define contract for getters
    int getX();
    int getY();
    String getSymbol();
}
