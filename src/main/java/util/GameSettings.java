package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

public class GameSettings {
    private static final Logger logger = Logger.getLogger(GameSettings.class.getName());
    private static final String SETTINGS_FILE = "src/main/resources/GameSettings.json"; // Percorso file
    private static GameSettings instance;

    private int spawnItemInterval;
    private int gameDurationInSeconds;
    private int physicsStrength;
    private int gameUpdateFPS;

    // Singleton per ottenere l'istanza caricata
    public static GameSettings getInstance() {
        if (instance == null) {
            instance = loadSettings();
        }
        return instance;
    }

    // Metodo per caricare le impostazioni
    private static GameSettings loadSettings() {
        try (FileReader reader = new FileReader(SETTINGS_FILE)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            return gson.fromJson(reader, GameSettings.class);
        } catch (IOException e) {
            logger.severe("Impossibile caricare il file delle impostazioni: " + e.getMessage());
            // Configura valori predefiniti in caso di errore
            GameSettings defaultSettings = new GameSettings();
            defaultSettings.spawnItemInterval = 4000;
            defaultSettings.gameDurationInSeconds = 180;
            defaultSettings.physicsStrength = 200;
            defaultSettings.gameUpdateFPS = 60;
            return defaultSettings;
        }
    }

    // Getter per le impostazioni
    public int getSpawnItemInterval() {
        return spawnItemInterval;
    }

    public int getGameDurationInSeconds() {
        return gameDurationInSeconds;
    }

    public int getPhysicsStrength() {
        return physicsStrength;
    }

    public int getGameUpdateFPS() {
        return gameUpdateFPS;
    }
}
