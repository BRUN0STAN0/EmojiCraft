package game;

import java.io.*;
import java.util.List;
import java.util.logging.Logger;
import model.Item;
import util.LoggerUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;



/**
 * Gestisce il salvataggio e caricamento dello stato del gioco.
 */
public class GameStateManager {
    private static final Logger logger = LoggerUtil.getLogger(GameState.class);
    private static final String SAVE_FILE = System.getProperty("user.dir") + "/game_state.dat";
    private static final String SAVE_FILE_JSON = "game_state.json"; // File JSON
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Salva lo stato del gioco su file.
     */
    public static void saveGameStateDual(int playerX, int playerY, int score, List<Item> items, int timeRemaining) {
        if (items == null) {
            items = List.of(); // Usa una lista vuota se manca la lista degli oggetti
        }
        GameState gameState = new GameState(playerX, playerY, score, items, timeRemaining);

        // Serializza in binario
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            oos.writeObject(gameState);
            System.out.println("Stato del gioco salvato correttamente in formato binario.");
        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio nel file binario: " + e.getMessage());
        }

        // Serializza in JSON
        try (FileWriter writer = new FileWriter(SAVE_FILE_JSON)) {
            gson.toJson(gameState, writer);
            System.out.println("Stato del gioco salvato correttamente in formato JSON.");
        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio del file JSON: " + e.getMessage());
        }
    }

    public static void saveGameStateToJson(GameState gameState) {
        try (FileWriter writer = new FileWriter(SAVE_FILE_JSON)) {
            gson.toJson(gameState, writer); // Serializza in JSON
            System.out.println("Stato del gioco salvato correttamente in formato JSON.");
        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio in JSON: " + e.getMessage());
        }
    }

    public static GameState loadGameStateFromJson() {
        try (FileReader reader = new FileReader(SAVE_FILE_JSON)) {
            return gson.fromJson(reader, GameState.class); // Deserializza dal JSON
        } catch (IOException e) {
            System.err.println("Errore durante il caricamento dello stato dal JSON: " + e.getMessage());
        }
        return null;
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

    public static GameState loadGameStateWithFallback() {
        GameState gameState = null;

        // Prova a caricare dallo stato JSON
        gameState = loadGameStateFromJson();
        if (gameState != null) {
            System.out.println("Caricato lo stato del gioco da JSON.");
            return gameState;
        }

        // Se il caricamento JSON fallisce, prova il file binario
        gameState = loadGameState();
        if (gameState != null) {
            System.out.println("Caricato lo stato del gioco da file binario.");
            return gameState;
        }

        // Se entrambi falliscono, restituisci null
        System.err.println("Errore: impossibile trovare uno stato del gioco valido.");
        return null;
    }
}