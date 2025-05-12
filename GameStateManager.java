package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.logging.Logger;

import map.ItemGroup;
import model.Item;

public class GameStateManager {
    private static final Logger logger = LoggerUtil.getLogger(GameStateManager.class);
    private static final String SAVE_FILE = System.getProperty("user.dir") + "/game_state.dat";

    public static void saveGameState(int playerX, int playerY, int score, List<Item> items, int timeRemaining) {
        GameState gameState = new GameState(playerX, playerY, score, items, timeRemaining); // Passa il minutaggio
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            oos.writeObject(gameState);
            System.out.println("Stato del gioco salvato con successo.");
        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio dello stato del gioco: " + e.getMessage());
        }
    }

    public static GameState loadGameState() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVE_FILE))) {
            return (GameState) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Nessun file di salvataggio trovato. Avvio di una nuova partita.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Errore durante il caricamento dello stato del gioco: " + e.getMessage());
        }
        return null;
    }

    // Classe interna per rappresentare lo stato del gioco
    public static class GameState {
        public final ItemGroup items;
        public final int playerX;
        public final int playerY;
        public final int timeRemaining;

        public GameState(ItemGroup items, int playerX, int playerY, int timeRemaining) {
            this.items = items;
            this.playerX = playerX;
            this.playerY = playerY;
            this.timeRemaining = timeRemaining;
        }
    }
}