# EmojiCraft

**EmojiCraft** è un'app Java SE che permette di gestire elementi grafici in stile "emozionale" con strutture gerarchiche e dinamiche. Il progetto è stato sviluppato per rispettare i requisiti
dell'esame di _Object Oriented Programming_.

## 📄 Funzionalità Principali

- Creazione di oggetti grafici (Item) tramite una fabbrica astratta
- Composizione di oggetti in strutture ad albero (Composite)
- Iterazione sugli elementi della struttura
- Logging delle operazioni
- Gestione controllata delle eccezioni

## 🎓 Tecnologie e Pattern Utilizzati

### Design Patterns

- **Factory Pattern** ✅ Per la creazione flessibile di oggetti `Item` tramite `ItemFactory`
- **Composite Pattern** ✅ Permette di comporre strutture ad albero con `MapComponent`, `ItemGroup`, `SingleItem`
- **Iterator Pattern** ✅ Iteratore personalizzato `ItemGroupIterator` per scorrere i componenti
- **Exception Shielding** ✅ Eccezioni gestite con `EmojiCraftException` e log protetti tramite `LoggerUtil`

### Tecnologie Java Core

- **Collections Framework** ✅ Uso di `List<MapComponent>` per comporre elementi
- **Generics** ✅ In `Iterator<MapComponent>` e `List<MapComponent>`
- **Java I/O** ❌ _(Da integrare se necessario: al momento non usato)_
- **Logging** ✅ Logging centralizzato tramite `LoggerUtil`
- **JUnit Testing** ❌ _(Test non presenti nel progetto attuale)_

### Sicurezza

- Nessun dato sensibile hardcoded ✅
- Gestione controllata delle eccezioni ✅
- Sanitizzazione input: ❌ _(non applicabile direttamente, da integrare se ci sono input utente)_

## ⚙️ Setup ed Esecuzione

1. Clona o scarica il progetto
2. Compila con un JDK Java 8 o superiore
3. Esegui `Main.java` dal tuo IDE o da terminale con:

   ```bash
   javac Main.java
   java Main
   ```

## 📊 UML

Sono disponibili i diagrammi UML di:

- Classi principali
- Architettura ad albero del Composite
- Iterazione

_(non inclusi in questa versione: da generare con PlantUML, StarUML o simili)_

## ⚠️ Limiti Noti

- Manca persistenza su file (Java I/O)
- Nessuna interfaccia utente
- Manca test coverage JUnit
- Mancano controlli sull'input da sorgenti esterne

## ✨ Miglioramenti Futuri

- Aggiunta di una GUI in Swing o JavaFX
- Persistenza con file JSON o XML
- Suite di test con JUnit 5
- Logging più dettagliato (es. per ogni operazione)
- Espansione dei tipi `Item`

---

© Progetto sviluppato da Bruno per l'esame di Object Oriented Programming - BSc Computer Science
