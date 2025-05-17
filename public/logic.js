let timerDuration = 180; // Durata del gioco in secondi
let timerInterval;
/*
function startTimer() {
    const timerElement = document.getElementById("timer");

    timerInterval = setInterval(() => {
        if (timerDuration <= 0) {
            clearInterval(timerInterval);
            endGame();
        } else {
            const minutes = Math.floor(timerDuration / 60);
            const seconds = timerDuration % 60;
            timerElement.textContent = `Time Left: ${minutes}:${seconds < 10 ? '0' : ''}${seconds}`;
            timerDuration--;
        }
    }, 1000);
}
*/
function endGame() {
    document.getElementById("game-over-overlay").classList.remove("hidden");
    document.getElementById("final-score").textContent = `Your Score: ${document.getElementById("score").textContent}`;
}

function playSound(audioId, volume = 0.2) {
    const audio = document.getElementById(audioId);
    audio.volume = volume;
    audio.currentTime = 0;
    audio.play().catch(err => console.warn("Audio error:", err));
}

function startMusic() {
    const audio = document.getElementById("music");
    if (audio.paused) {
        audio.volume = 0.05;
        audio.play().catch(err => console.warn("Music error:", err));
    }
}

let playerX, playerY;

function renderGrid(grid) {
    const gridElement = document.getElementById("grid");
    let html = '<table>';
    for (let y = 0; y < grid.length; y++) {
        html += '<tr>';
        for (let x = 0; x < grid[y].length; x++) {
            const cell = grid[y][x];
            if (cell.includes('🧍') || cell.includes('🚶')) {
                playerX = x;
                playerY = y;
            }
            html += `<td>${cell}</td>`;
        }
        html += '</tr>';
    }
    html += '</table>';
    gridElement.innerHTML = html;
}

document.addEventListener("keydown", (e) => {
    if (["w", "a", "s", "d"].includes(e.key)) {
        movePlayer(e.key.toUpperCase());
    }
});

async function movePlayer(direction) {
    const response = await fetch("/move", {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: `dir=${direction}`,
    });
    if (response.ok) {
        updateWorld(await response.json());
    }
}

async function updateWorld(data) {
    renderGrid(data.grid);
    document.getElementById("score").textContent = data.score;
    if (data.collected) playSound("collectSound");
    if (!data.gameActive) endGame();
}
document.getElementById("restart-button").addEventListener("click", restartGame);
document.getElementById("play-again-button").addEventListener("click", restartGame);

function restartGame() {
    fetch('/restart', { method: 'POST' })
        .then(res => {
            if (res.ok) {
                return res.json();
            } else {
                throw new Error("Errore durante il riavvio del gioco.");
            }
        })
        .then(data => {
            if (data.gameActive) {
                // Nascondi overlay Game Over
                document.getElementById("game-over-overlay").classList.add("hidden");

                // Mostra Griglia e HUD
                document.getElementById("grid-container").style.display = "block";
                document.getElementById("hud").style.display = "flex";

                // Imposta il timer con il valore del server
                timerDuration = data.timeRemaining;
                clearInterval(timerInterval);
                startTimer(); // Riavvia il timer sul client

                loadWorld(); // Ricarica il mondo per sincronizzare la griglia e il gameplay


            }
        })
        .catch(error => console.error("Errore nel riavvio del gioco:", error));
        location.reload();
}
async function loadWorld() {
    const response = await fetch('/world');
    if (response.ok) {
        const data = await response.json();

        // Aggiorna il timer dal valore restituito dal server
        timerDuration = data.timeRemaining;

        // Aggiorna la griglia
        renderGrid(data.grid);

        // Aggiorna il punteggio
        document.getElementById("score").textContent = data.score;

        // Aggiorna il timer nell'HUD
        const minutes = Math.floor(timerDuration / 60);
        const seconds = timerDuration % 60;
        document.getElementById("timer").textContent = `Time Left: ${minutes}:${seconds < 10 ? "0" : ""}${seconds}`;

        // Se il gioco è terminato, mostra la schermata Game Over
        if (!data.gameActive) {
            endGame();
        }
    } else {
        console.error("Errore nel caricamento del mondo.");
    }
}
setInterval(() => {
    loadWorld(); // Chiama la funzione per aggiornare la griglia
}, 100); // Intervallo di tempo: 100ms