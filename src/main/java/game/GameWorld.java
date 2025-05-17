package game;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import factory.ItemFactory;
import factory.NegativeItemFactory;
import map.ItemGroup;
import map.MapComponent;
import model.Item;
import model.Wall;
import response.MoveResponse;
import util.GameSettings;

public class GameWorld {
    private static final int WIDTH = 24;
    private static final int HEIGHT = 10;
    private final ItemGroup items = new ItemGroup();
    private List<Item> itemList;
    private int score = 0;
    private int recentScoreGained = 0;
    private boolean itemCollected = false;
    private final Map<Item, Long> itemTimers = new ConcurrentHashMap<>(); // Mappa per tracciare il tempo di vita degli oggetti
    private static util.GameSettings GameSettings;
    private static final util.GameSettings gameSettings = util.GameSettings.getInstance();
    private static final long ITEM_LIFETIME = gameSettings.getSpawnItemInterval(); // Intervallo spawn item

    private int timeRemaining;
    // Contatore per tracciare lo spawn degli oggetti
    private int itemSpawnCounter = 0;
    private boolean gameActive = true; // Lo stato di attivazione del gioco
    private boolean spawnNegativeNext = false;
    private static final int PLAYER_START_X = 5;
    private static final int PLAYER_START_Y = 3;

    public GameWorld() {
        createGround();
        spawnNewItem();
    }

    public synchronized boolean isGameActive() {
        return gameActive;
    }

    public synchronized void setGameActive(boolean isActive) {
        this.gameActive = isActive;
    }

    public synchronized int getTimeRemaining() {
        return timeRemaining;
    }

    public synchronized void setTimeRemaining(int timeRemaining) {
        this.timeRemaining = timeRemaining;
    }

    // Getter per gli oggetti
    public List<Item> getItems() {
        return itemList;
    }

    // Metodo per impostare gli oggetti (se necessario)
    public void setItems(List<Item> items) {
        this.itemList = items;
    }



    public GameWorld(ItemGroup items) {
        this.items.getComponents().addAll(items.getComponents());
    }


    public boolean movePlayer(Player player, String direction, GamePhysics gamePhysics) {
        itemCollected = false;

        // Notifica che il movimento manuale è in corso
        gamePhysics.startManualMovement();

        int newX = player.getX();
        int newY = player.getY();

        // Calcola la nuova posizione in base alla direzione
        switch (direction) {
            case "W" -> newY -= 1; // Movimento verso l'alto
            case "S" -> newY += 1; // Movimento verso il basso
            case "A" -> newX -= 1; // Movimento verso sinistra
            case "D" -> newX += 1; // Movimento verso destra
        }

        // Controlla se la nuova posizione è valida
        if (newX >= 0 && newX < WIDTH && newY >= 0 && newY < HEIGHT - 2) {
            player.move(newX - player.getX(), newY - player.getY(), directionToEmoji(direction));
            itemCollected = checkItemCollision(player);
        } else {
            System.out.println("Movimento non valido: il giocatore ha raggiunto il limite della griglia.");
        }

        // Fine del movimento manuale, riattiva la fisica
        gamePhysics.endManualMovement();

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
            default -> "🧍‍♂️";
        };
    }

    public synchronized boolean checkItemCollision(Player player) {
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

    public void createGround() {
        for (int y = HEIGHT - 2; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Wall wall = new Wall(x, y);
                items.add(wall);
                System.out.println("Muro aggiunto in posizione X=" + x + ", Y=" + y);
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

    public synchronized void updateItems() {
        if (!gameActive) {
            return; // Se il gioco non è attivo, interrompe l'aggiornamento
        }

        long currentTime = System.currentTimeMillis();
        Iterator<Map.Entry<Item, Long>> iterator = itemTimers.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Item, Long> entry = iterator.next();
            Item item = entry.getKey();
            long creationTime = entry.getValue();

            // Controlla se il tempo di vita dell'oggetto è scaduto
            if (currentTime - creationTime > ITEM_LIFETIME) {
                items.remove(item); // Rimuovi l'oggetto dalla griglia
                iterator.remove(); // Rimuovi l'oggetto dalla mappa dei timer
                System.out.println("Oggetto scaduto rimosso: " + item.getSymbol());

                // Genera un nuovo oggetto solo se il gioco è attivo
                if (gameActive) {
                    spawnNewItem();
                }
            }
        }
    }

    public synchronized void spawnNewItem() {
        if (!gameActive) return;

        Random random = new Random();
        int x, y;

        do {
            x = random.nextInt(WIDTH);
            y = random.nextInt(HEIGHT - 2);
        } while (!isCellEmpty(x, y));

        // Alternare tra oggetto positivo e negativo
        Item newItem = spawnNegativeNext
            ? NegativeItemFactory.createRandomNegativeItem(x, y)
            : ItemFactory.createRandomItem(x, y);

        spawnNegativeNext = !spawnNegativeNext;

        items.add(newItem);
        itemTimers.put(newItem, System.currentTimeMillis());
        System.out.println("Oggetto registrato: " + newItem.getSymbol() + " in posizione X=" + x + ", Y=" + y);
    }

    public void render(String[][] grid) {
        for (MapComponent component : items.getComponents()) {
            int x = component.getX();
            int y = component.getY();

            // Verifica che gli indici siano validi
            if (x >= 0 && x < grid[0].length && y >= 0 && y < grid.length) {
                grid[y][x] = component.getSymbol(); // Usa il simbolo del componente
                System.out.println("Render Oggetto: " + component.getSymbol() + " in posizione X=" + x + ", Y=" + y);
            } else {
                System.err.println("Errore: componente fuori dai limiti! X=" + x + ", Y=" + y);
            }
        }
    }

    public void saveGame(Player player) {
    try {
        // Salva lo stato del gioco con i dati aggiornati
        GameStateManager.saveGameStateDual(
            player.getX(),
            player.getY(),
            score,
            getWorldState(player), // Usa la griglia corrente del mondo
            timeRemaining
        );
        System.out.println("Stato del gioco salvato correttamente.");
    } catch (Exception e) {
        System.err.println("Errore durante il salvataggio dello stato del gioco: " + e.getMessage());
    }
}

public void loadGame(Player player) {
    try {
        GameState gameState = GameStateManager.loadGameState();
        if (gameState != null) {
            // Ripristina la posizione del giocatore
            player.move(gameState.getPlayerX() - player.getX(), gameState.getPlayerY() - player.getY(), "🧍");

            // Ripristina il punteggio
            score = gameState.getScore();

            // Ripristina il tempo rimanente
            timeRemaining = gameState.getTimeRemaining();

            // Ripulisci gli oggetti esistenti e ricarica lo stato della griglia
            items.getComponents().clear();

            String[][] loadedGrid = gameState.getGrid(); // Ottiene la griglia salvata

            if (loadedGrid != null) {
                for (int y = 0; y < loadedGrid.length; y++) {
                    for (int x = 0; x < loadedGrid[0].length; x++) {
                        String symbol = loadedGrid[y][x];
                        if (symbol != null && !symbol.equals(" ")) {
                            // Ricrea gli oggetti del mondo inew Wall("")n base ai simboli
                            MapComponent mapComponent = new Wall(x,y);
                            if (mapComponent != null) {
                                items.add(mapComponent);
                            }
                        }
                    }
                }
            } else {
                System.err.println("Attenzione: non è stata trovata alcuna griglia salvata!");
            }

            System.out.println("Stato del gioco caricato con successo.");
        } else {
            System.err.println("Errore: impossibile caricare lo stato del gioco!");
        }
    } catch (Exception e) {
        System.err.println("Errore durante il caricamento dello stato del gioco: " + e.getMessage());
    }
}

    public void registerShutdownHook(Player player) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            saveGame(player);
        }));
    }

    public static void validateOrInitializePlayer(GameWorld gameWorld, Player player) {
        // Controlla validità posizione del giocatore
        if (!gameWorld.isValidPosition(player.getX(), player.getY())) {
            System.out.println("Giocatore fuori dai confini. Reinserito nella posizione iniziale.");
            player.setPosition(5, 3); // Posizione di spawn iniziale
        }
    }
    public boolean isValidPosition(int x, int y) {
        return x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT - 2;
    }

    public void resetGame() {
    // Rimuovi tutti gli oggetti esistenti
    this.items.getComponents().clear(); 
    this.itemTimers.clear(); // Pulisci la mappa dei timer
    this.score = 0; // Reimposta il punteggi
    this.timeRemaining = 60;
    this.recentScoreGained = 0; // Resetta il punteggio recente
    this.itemCollected = false; // Resetta il flag raccolta oggetto

    // Crea nuovamente il terreno
    createGround(); 

    // Genera un nuovo oggetto
    spawnNewItem(); 

    // Log di debug
    System.out.println("Gioco completamente ripristinato. Oggetti, timer e punteggio resettati.");
}



}