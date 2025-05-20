package util;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerUtil {
    private static LoggerUtil instance; // Istanza Singleton
    private static final Logger logger = Logger.getLogger("GlobalLogger");

    // Costruttore privato
    private LoggerUtil() {
        try {
            FileHandler fileHandler = new FileHandler("game_logs.log", true); // Scrive su file
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.setUseParentHandlers(false);
        } catch (IOException e) {
            System.err.println("Errore nel logger: " + e.getMessage());
        }
    }

    // Metodo statico per ottenere l'istanza
    public static synchronized LoggerUtil getInstance() {
        if (instance == null) {
            instance = new LoggerUtil();
        }
        return instance;
    }

    // Metodo per il logger globale
    public Logger getGlobalLogger() {
        return logger;
    }
}