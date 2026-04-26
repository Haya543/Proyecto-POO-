import { useState } from "react";
import type { GameState } from "./types/game";
import TitleScreen from "./components/TitleScreen";
import WorldMap from "./components/WorldMap";
import BattleScreen from "./components/BattleScreen";

// Root component. Routes between game phases based on the fase field from the backend.
export default function App() {
  const [gameState, setGameState] = useState<GameState | null>(null);

  function handleUpdate(state: GameState) {
    setGameState(state);
  }

  if (!gameState || gameState.fase === "INICIO") {
    return <TitleScreen onStart={handleUpdate} />;
  }

  if (gameState.fase === "BATALLA" && gameState.batalla && gameState.jugador) {
    return (
      <BattleScreen
        battle={gameState.batalla}
        player={gameState.jugador}
        onUpdate={handleUpdate}
      />
    );
  }

  if (gameState.fase === "JUEGO_TERMINADO") {
    return (
      <div
        style={{
          minHeight: "100vh",
          background: "#0a0a14",
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          justifyContent: "center",
          color: "#f0f0f0",
        }}
      >
        <div style={{ color: "#f5a623", fontSize: 48, fontWeight: 900, marginBottom: 16 }}>
          CAMPEON
        </div>
        <div style={{ color: "#aaa", fontSize: 18, marginBottom: 8 }}>
          {gameState.jugador?.nombre} ha derrotado al Campeon Rex!
        </div>
        <div style={{ color: "#666", fontSize: 14, marginBottom: 32 }}>
          Criaturas capturadas: {gameState.jugador?.capturadas.length ?? 0}
        </div>
        <button
          style={{
            background: "linear-gradient(135deg, #1a5a8a, #0d3a5a)",
            color: "#fff",
            border: "2px solid #3a8ab8",
            borderRadius: 10,
            padding: "14px 36px",
            fontSize: 16,
            fontWeight: 700,
            cursor: "pointer",
          }}
          onClick={() => setGameState(null)}
        >
          JUGAR DE NUEVO
        </button>
      </div>
    );
  }

  return <WorldMap gameState={gameState} onUpdate={handleUpdate} />;
}
