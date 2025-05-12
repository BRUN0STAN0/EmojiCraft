package game;

import game.GameState;
import java.io.*;
import java.util.List;
import java.util.logging.Logger;
import model.Item;
import util.LoggerUtil;

/**
 * Gestisce il salvataggio e caricamento dello stato del gioco.
 */
public class GameStateManager {
    private static final Logger logger = LoggerUtil.getLogger(GameState.class);
    private static final String SAVE_FILE = System.getProperty("user.dir") + "/game_state.dat";

    /**
     * Salva lo stato del gioco su file.
     */
    public static void saveGameState(int playerX, int playerY, int score, List<Item> items, int timeRemaining) {
        GameState gameState = new GameState(playerX, playerY, score, items, timeRemaining);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            oos.writeObject(gameState);
            System.out.println("Stato del gioco salvato con successo.");
        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio dello stato del gioco: " + e.getMessage());
        }
    }

    /**
     * Carica lo stato del gioco da file.
     */
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
}