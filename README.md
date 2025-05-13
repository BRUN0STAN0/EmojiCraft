# EmojiCraft

**EmojiCraft** è un'applicazione Java SE progettata per gestire elementi grafici in uno stile emozionale. Utilizza strutture ad albero, design patterns e funzionalità core di Java per garantire un'applicazione robusta, modulare e manutenibile. Questo progetto può essere utilizzato come una dimostrazione delle competenze di programmazione orientata agli oggetti e sicurezza applicativa.

---

## 📄 Funzionalità Principali

- Creazione e gestione dinamica di oggetti grafici (emojis).
- Posizionamento e aggiornamento degli oggetti su una griglia.
- Movimento di un giocatore sulla griglia con rilevamento delle collisioni.
- Sistema di punteggio che premia/rimuove punti quando si interagisce con specifici oggetti.
- Timer che segna la durata complessiva di una sessione.
- Salvataggio e caricamento dello stato del gioco tramite file.
- Logging degli eventi significativi per indagini e debug.

---

## 🎓 Tecnologie e Pattern Utilizzati

### Design Patterns (Punti: 16)

1. **Factory Pattern** ✅ (3 punti)
   - Implementato in `ItemFactory` e `NegativeItemFactory` per creare dinamicamente oggetti di gioco (positivi e negativi).

2. **Composite Pattern** ✅ (4 punti)
   - Utilizzato in `ItemGroup` per gestire insiemi gerarchici di componenti della griglia.

3. **Iterator Pattern** ✅ (4 punti)
   - Implementato in `MapIterator` per iterare facilmente sui componenti nella struttura della griglia.

4. **Exception Shielding** ✅ (5 punti)
   - Gestione robusta delle eccezioni utilizzando wrapper customizzati (`EmojiCraftException`) e logging sicuro con `LoggerUtil`.

---

### Tecnologie obbligatorie (Punti: 11/14)

1. **Collections Framework** ✅ (3 punti)
   - Varianti di `List` utilizzate per contenere elementi e altri dati strutturati del gioco.

2. **Generics** ✅ (3 punti)
   - Utilizzati per rendere il codice delle collezioni e iteratori flessibile e riutilizzabile.

3. **Java I/O** ❌
   - Sistemi di input/output (non completamente gestiti; presente solo la persistenza del gioco in `GameStateManager`).

4. **Logging** ✅ (2 punti)
   - Implementazione completa di un sistema logging tramite `LoggerUtil`.

5. **JUnit Testing** ❌
   - Implementazioni di test JUnit 5 non ancora incluse.

---

### Sicurezza

- **Eccezioni gestite correttamente** ✅ (No crash o stack trace visibili all'utente).
- **Nessun dato sensibile hardcoded** ✅.
- **Sanitizzazione dell’input** ❌ (Non ancora implementata poiché non richiesto direttamente dal gioco).

---

## ⚙️ Setup ed Esecuzione

Per eseguire l'applicazione:

1. **Download del progetto**
   - Clona o scarica il progetto da [GitHub](https://github.com/user/emojicraft).
   - Assicurarsi di avere configurato `Maven`.

2. **Compilazione**
   - Da terminale, esegui:
     ```bash
     mvn clean compile
     ```

3. **Esecuzione**
   - Da terminale, esegui:
     ```bash
     mvn exec:java -Dexec.mainClass=Main
     ```

Per eseguire il front-end e avviare l'interfaccia utente, avvia il server con Spark integrato.

---

## 📊 Diagrammi UML

Diagrammi generati per rappresentare:

- **Classi principali:** Overview delle entità principali come `Player`, `GameWorld`, `Item`.
- **Architettura ad albero:** Diagramma del pattern Composite.
- **Flusso del gioco:** Diagramma che rappresenta come avviene l'interazione tra le classi.

⚠️ Diagrammi non inclusi in questa versione; utilizza tool come [PlantUML](https://plantuml.com) o [StarUML](https://staruml.io).

---

## ⚠️ Limitazioni Conosciute

1. **Persistenza incompleta**:
   - La persistenza è realizzata solo per lo stato del gioco. Manca una funzionalità più ricca con JSON o XML.

2. **Assenza di Test (JUnit)**:
   - La base di codice manca di una suite dedicata per test unitari.

3. **Controllo Input Utente**:
   - Sanitizzazione dell'input (`ServerManager`) da implementare per prevenire input imprevisti.

4. **Java I/O**:
   - Come menzionato nei requisiti, la funzionalità Java I/O resta limitata.

---

## ✨ Miglioramenti Futuri

1. **Test Coverage**:
   - Implementare test con JUnit 5.

2. **Miglioramenti al Logging**:
   - Inserire messaggi più granulari dagli eventi di gioco.

3. **UI Avanzata**:
   - Migrazione verso Swing o JavaFX per un’interfaccia user-friendly.

4. **Sanitizzazione Input**:
   - Implementare metodi di validazione.

5. **Persistenza Avanzata**:
   - Incorporare gestione di file JSON o XML per salvataggi più estensivi.

---

### 📞 Supporto

Per domande o segnalazioni su EmojiCraft, contatta _bruno@emojicraft.com_.

---

© Progetto sviluppato per un’esercitazione Object-Oriented Programming - **50/50 Version**.