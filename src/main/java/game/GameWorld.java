package game;

import java.util.Random;

import factory.ItemFactory;
import map.ItemGroup;
import map.MapComponent;
import model.Item;
import response.MoveResponse;

public class GameWorld {
    private static final int WIDTH = 24;
    private static final int HEIGHT = 10;
    private final ItemGroup items = new ItemGroup();
    private int score = 0;
    private int recentScoreGained = 0;
    private boolean itemCollected = false;

    public GameWorld() {
        createGround();
        Random random = new Random();
        int x = random.nextInt(WIDTH);
        int y = random.nextInt(HEIGHT - 2); // Avoid ground area
        items.add(ItemFactory.createRandomItem(x, y));
    }

    public GameWorld(ItemGroup items) {
        this.items.getComponents().addAll(items.getComponents());
    }

    public boolean movePlayer(Player player, String direction) {
        itemCollected = false;

        int newX = player.getX();
        int newY = player.getY();

        // Calcola la nuova posizione in base alla direzione
        switch (direction) {
            case "W" -> newY -= 1; // Movimento verso l'alto
            case "S" -> newY += 3; // Movimento verso il basso
            case "A" -> newX -= 1; // Movimento verso sinistra
            case "D" -> newX += 1; // Movimento verso destra
        }

        // Controlla se la nuova posizione è valida
        if (newX >= 0 && newX < WIDTH && newY >= 0 && newY < HEIGHT) {
            player.move(newX - player.getX(), newY - player.getY(), directionToEmoji(direction));
            itemCollected = checkItemCollision(player);
        } else {
            System.out.println("Movimento non valido: il giocatore ha raggiunto il limite della griglia.");
        }

        System.out.println("Posizione giocatore: X=" + player.getX() + ", Y=" + player.getY());
        return itemCollected;
    }

    // Metodo di supporto per ottenere l'emoji della direzione
    private String directionToEmoji(String direction) {
        return switch (direction) {
            case "W" -> "🤸🏻‍♂️";
            case "S" -> "🧎🏻‍♂️‍➡️";
            case "A" -> "🚶🏻‍♂️";
            case "D" -> "🚶🏻‍♂️‍➡️";
            default -> "🧍";
        };
    }

    public boolean checkItemCollision(Player player) {
        boolean collected = items.getComponents().removeIf(component -> {
            if (component instanceof Item item && item.getX() == player.getX() && item.getY() == player.getY()) {
                recentScoreGained = item.getScore();
                score += recentScoreGained;
                System.out.println("Oggetto raccolto: " + item.getSymbol() + " in posizione X=" + item.getX() + ", Y=" + item.getY());
                return true;
            }
            return false;
        });

        if (collected) {
            spawnNewItem(); // Genera un nuovo oggetto
        }

        return collected;
    }

    public String[][] getWorldState(Player player) {
        String[][] grid = new String[HEIGHT][WIDTH];
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                grid[y][x] = " ";
            }
        }

        items.render(grid);

        // Controlla che il giocatore sia all'interno dei limiti della griglia
        if (player.getY() >= 0 && player.getY() < HEIGHT && player.getX() >= 0 && player.getX() < WIDTH) {
            grid[player.getY()][player.getX()] = player.getEmoji(); // Usa l'emoji corrente del giocatore
        } else {
            System.err.println("Errore: il giocatore è fuori dai limiti della griglia!");
        }

        return grid;
    }

    public int getScore() {
        return score;
    }

    public int getRecentScoreGained() {
        return recentScoreGained;
    }

    public boolean isItemCollected() {
        return itemCollected;
    }

    private void createGround() {
        for (int y = HEIGHT - 2; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                items.add(new model.Wall(x, y));
            }
        }
    }

    public MoveResponse getMoveResponse(Player player, boolean itemCollected) {
        return new MoveResponse(player.getX(), player.getY(), score, itemCollected);
    }

    public boolean isCellEmpty(int x, int y) {
        // Controlla se la cella è vuota
        return items.getComponents().stream()
                .noneMatch(component -> component.getX() == x && component.getY() == y);
    }

    public int getHeight() {
        return HEIGHT;
    }

    private void spawnNewItem() {
        Random random = new Random();
        int x, y;

        // Trova una posizione vuota nella griglia
        do {
            x = random.nextInt(WIDTH); // Genera un valore tra 0 e WIDTH-1
            y = random.nextInt(HEIGHT - 2); // Evita di generare oggetti sul terreno (ultime due righe)
        } while (!isCellEmpty(x, y));

        // Crea un nuovo oggetto e aggiungilo alla lista
        items.add(ItemFactory.createRandomItem(x, y));
        System.out.println("Nuovo oggetto generato in posizione: X=" + x + ", Y=" + y);
    }
    public void render(String[][] grid) {
        for (MapComponent component : items.getComponents()) {
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
}
