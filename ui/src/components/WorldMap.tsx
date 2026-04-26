import { useState } from "react";
import type { GameState, PlayerInfo } from "../types/game";
import { gameApi } from "../api/gameApi";
import { TYPE_COLORS } from "./HPBar";

interface WorldMapProps {
  gameState: GameState;
  onUpdate: (state: GameState) => void;
}

const ZONE_NAMES = ["", "Pradera de los Inicios", "Bosque de las Profundidades"];
const NPC_NAMES = ["Rival Chato", "Lider Marina", "Lider Brenno", "Campeon Rex"];

// Main overworld screen. Shows player status and action buttons.
export default function WorldMap({ gameState, onUpdate }: WorldMapProps) {
  const [loading, setLoading] = useState<string | null>(null);
  const [localMsg, setLocalMsg] = useState<string | null>(null);
  const [showTeam, setShowTeam] = useState(false);
  const [showCaptured, setShowCaptured] = useState(false);

  const player = gameState.jugador!;

  async function run(label: string, action: () => Promise<GameState>) {
    setLoading(label);
    setLocalMsg(null);
    try {
      const state = await action();
      if (state.mensaje) setLocalMsg(state.mensaje);
      onUpdate(state);
    } catch (e: unknown) {
      if (e instanceof Error) setLocalMsg("Error: " + e.message);
    } finally {
      setLoading(null);
    }
  }

  const nextNpc = NPC_NAMES[gameState.siguienteNpcIndex] ?? null;

  return (
    <div style={styles.screen}>
      {/* Header */}
      <div style={styles.header}>
        <div>
          <span style={{ color: "#00d4ff", fontWeight: 700, fontSize: 18 }}>{player.nombre}</span>
          <span style={{ color: "#666", marginLeft: 16 }}>Zona: {ZONE_NAMES[gameState.zona]}</span>
        </div>
        <div style={{ display: "flex", gap: 24 }}>
          <span style={{ color: "#f5a623" }}>Medallas: {player.medallas}</span>
          <span style={{ color: "#6bffb8" }}>${player.dinero}</span>
          <span style={{ color: "#aaa" }}>Equipo: {player.equipo.length}</span>
        </div>
      </div>

      {/* Message banner */}
      {localMsg && (
        <div style={styles.msgBanner} onClick={() => setLocalMsg(null)}>
          {localMsg}
        </div>
      )}

      {/* Action menu */}
      <div style={styles.menu}>
        <div style={{ color: "#666", fontSize: 12, letterSpacing: 2, marginBottom: 16 }}>
          QUE DESEAS HACER?
        </div>

        <ActionButton
          label="Explorar zona"
          sub={ZONE_NAMES[gameState.zona]}
          color="#3a8ab8"
          disabled={!!loading}
          loading={loading === "explorar"}
          onClick={() => run("explorar", () => gameApi.explore())}
        />

        <ActionButton
          label={nextNpc ? `Retar a: ${nextNpc}` : "No hay mas entrenadores"}
          sub={nextNpc ? "Siguiente entrenador de la historia" : "Ya eres el campeon!"}
          color="#8b3ab8"
          disabled={!!loading || !nextNpc}
          loading={loading === "retar"}
          onClick={() => run("retar", () => gameApi.challengeNpc())}
        />

        <ActionButton
          label="Ver equipo"
          sub={`${player.equipo.length} criaturas`}
          color="#3ab84a"
          disabled={!!loading}
          loading={false}
          onClick={() => setShowTeam(!showTeam)}
        />

        <ActionButton
          label="Curar equipo"
          sub={gameState.puedesCurar ? "Centro de Criaturas disponible" : "Ya curaste tu equipo"}
          color={gameState.puedesCurar ? "#b8843a" : "#444"}
          disabled={!!loading || !gameState.puedesCurar}
          loading={loading === "curar"}
          onClick={() => run("curar", () => gameApi.heal())}
        />

        <ActionButton
          label="Criaturas capturadas"
          sub={`${player.capturadas.length} capturadas`}
          color="#b83a7a"
          disabled={!!loading}
          loading={false}
          onClick={() => setShowCaptured(!showCaptured)}
        />
      </div>

      {/* Team panel */}
      {showTeam && (
        <div style={styles.panel}>
          <div style={styles.panelTitle}>Equipo Activo</div>
          {player.equipo.map((c, i) => (
            <div key={i} style={styles.criaturaRow}>
              <span
                style={{
                  background: TYPE_COLORS[c.tipo],
                  color: "#fff",
                  borderRadius: 4,
                  padding: "2px 7px",
                  fontSize: 10,
                  fontWeight: 700,
                  marginRight: 10,
                }}
              >
                {c.tipo}
              </span>
              <span style={{ color: c.estaViva ? "#e8e8e8" : "#555", fontWeight: 600 }}>
                {c.nombre}
              </span>
              <span style={{ color: "#aaa", fontSize: 12, marginLeft: 8 }}>Nv.{c.nivel}</span>
              <span style={{ color: c.hp < c.hpMax * 0.25 ? "#e53935" : "#6bffb8", fontSize: 12, marginLeft: "auto" }}>
                {c.hp}/{c.hpMax} HP
              </span>
            </div>
          ))}

          {player.bolsa.length > 0 && (
            <>
              <div style={{ ...styles.panelTitle, marginTop: 16 }}>Bolsa</div>
              {player.bolsa.map((obj, i) => (
                <div key={i} style={styles.criaturaRow}>
                  <span style={{ color: "#c0c0d0" }}>{obj.nombre}</span>
                  <span style={{ color: "#666", fontSize: 12, marginLeft: 8 }}>x{obj.cantidad}</span>
                  <span style={{ color: "#555", fontSize: 11, marginLeft: 8 }}>{obj.descripcion}</span>
                </div>
              ))}
            </>
          )}
        </div>
      )}

      {/* Captured panel */}
      {showCaptured && (
        <div style={styles.panel}>
          <div style={styles.panelTitle}>Criaturas Capturadas ({player.capturadas.length})</div>
          {player.capturadas.length === 0 && (
            <div style={{ color: "#555", fontSize: 13 }}>Aun no has capturado ninguna criatura.</div>
          )}
          {player.capturadas.map((c, i) => (
            <div key={i} style={styles.criaturaRow}>
              <span
                style={{
                  background: TYPE_COLORS[c.tipo],
                  color: "#fff",
                  borderRadius: 4,
                  padding: "2px 7px",
                  fontSize: 10,
                  fontWeight: 700,
                  marginRight: 10,
                }}
              >
                {c.tipo}
              </span>
              <span style={{ color: "#e8e8e8", fontWeight: 600 }}>{c.nombre}</span>
              <span style={{ color: "#aaa", fontSize: 12, marginLeft: 8 }}>Nv.{c.nivel}</span>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

// Reusable action button with loading state
function ActionButton({
  label, sub, color, disabled, loading, onClick
}: {
  label: string; sub: string; color: string; disabled: boolean; loading: boolean; onClick: () => void;
}) {
  return (
    <button
      style={{
        display: "flex",
        flexDirection: "column",
        alignItems: "flex-start",
        width: "100%",
        background: disabled ? "rgba(20,20,30,0.6)" : "rgba(20,30,50,0.9)",
        border: `1px solid ${disabled ? "#222" : color + "66"}`,
        borderLeft: `4px solid ${disabled ? "#333" : color}`,
        borderRadius: 8,
        padding: "12px 16px",
        marginBottom: 8,
        cursor: disabled ? "not-allowed" : "pointer",
        opacity: disabled ? 0.5 : 1,
        transition: "background 0.15s",
      }}
      disabled={disabled}
      onClick={onClick}
    >
      <span style={{ color: disabled ? "#555" : "#e8e8e8", fontWeight: 700, fontSize: 15 }}>
        {loading ? "..." : label}
      </span>
      <span style={{ color: "#666", fontSize: 12, marginTop: 2 }}>{sub}</span>
    </button>
  );
}

const styles: Record<string, React.CSSProperties> = {
  screen: {
    minHeight: "100vh",
    background: "#0a0a14",
    padding: 24,
    maxWidth: 720,
    margin: "0 auto",
  },
  header: {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
    background: "rgba(15,20,35,0.95)",
    border: "1px solid #1e3050",
    borderRadius: 10,
    padding: "12px 20px",
    marginBottom: 20,
  },
  msgBanner: {
    background: "rgba(20,30,50,0.98)",
    border: "1px solid #3a6a9a",
    borderRadius: 8,
    padding: "12px 16px",
    color: "#c8d8e8",
    fontSize: 14,
    marginBottom: 16,
    cursor: "pointer",
    whiteSpace: "pre-line",
    lineHeight: 1.5,
  },
  menu: {
    background: "rgba(10,12,20,0.95)",
    border: "1px solid #1a2030",
    borderRadius: 12,
    padding: "20px 16px",
    marginBottom: 16,
  },
  panel: {
    background: "rgba(12,14,24,0.95)",
    border: "1px solid #1a2030",
    borderRadius: 12,
    padding: "16px 20px",
    marginBottom: 16,
  },
  panelTitle: {
    color: "#4a6fa5",
    fontSize: 11,
    fontWeight: 700,
    letterSpacing: 2,
    marginBottom: 12,
  },
  criaturaRow: {
    display: "flex",
    alignItems: "center",
    padding: "6px 0",
    borderBottom: "1px solid #111",
    fontSize: 13,
  },
};
