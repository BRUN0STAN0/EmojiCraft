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
import java.io.ObjectInputStream;

/**
 * Gestisce il salvataggio e caricamento dello stato del gioco.
 */
public class GameStateManager {
    private static final Logger logger = LoggerUtil.getLogger(GameState.class);
    private static final String SAVE_FILE = System.getProperty("user.dir") + "/game_state.dat";
    private static final String SAVE_FILE_JSON = "game_state.json"; // File JSON
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    

public static void saveGameStateDual(int playerX, int playerY, int score, String[][] grid, int timeRemaining) {
    try {
        Gson gson = new GsonBuilder().setPrettyPrinting().create(); // Oggetto Gson per JSON leggibile

        // Crea un oggetto GameState con i dati da salvare
        GameState gameState = new GameState(playerX, playerY, score, grid, timeRemaining);

        // Salva l'oggetto GameState direttamente nel file come JSON
        try (FileWriter writer = new FileWriter("game_state.json")) {
            gson.toJson(gameState, writer); // Converte l'oggetto in JSON e lo salva
            System.out.println("Game state salvato correttamente in game_state.json!");
        }
    } catch (IOException e) {
        e.printStackTrace();
        System.err.println("Errore durante il salvataggio dello stato di gioco.");
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
    try (FileReader reader = new FileReader("game_state.json")) {
        Gson gson = new Gson(); // Creazione di un oggetto Gson
        GameState gameState = gson.fromJson(reader, GameState.class); // Converti il JSON in GameState
        System.out.println("Game state caricato correttamente da game_state.json!");
        return gameState; // Ritorna lo stato del gioco
    } catch (IOException e) {
        System.err.println("Errore durante il caricamento dello stato di gioco dal file JSON: " + e.getMessage());
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