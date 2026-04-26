import { useState } from "react";
import type { BattleState, PlayerInfo, GameState } from "../types/game";
import { gameApi } from "../api/gameApi";
import HPBar from "./HPBar";
import MoveMenu from "./MoveMenu";

interface BattleScreenProps {
  battle: BattleState;
  player: PlayerInfo;
  onUpdate: (state: GameState) => void;
}

// Full battle UI: enemy panel top, player panel bottom, action log on right
export default function BattleScreen({ battle, player, onUpdate }: BattleScreenProps) {
  const [log, setLog] = useState<string[]>([]);
  const [busy, setBusy] = useState(false);
  const [mode, setMode] = useState<"main" | "bag" | "spheres">("main");

  async function doAction(action: () => Promise<GameState>, label?: string) {
    setBusy(true);
    try {
      const state = await action();
      if (state.mensaje) {
        setLog(prev => [state.mensaje!, ...prev].slice(0, 20));
      }
      onUpdate(state);
    } catch (e: unknown) {
      if (e instanceof Error) setLog(prev => ["Error: " + e.message, ...prev]);
    } finally {
      setBusy(false);
      setMode("main");
    }
  }

  const rival = battle.criaturaRival;
  const myCriatura = battle.criaturaJugador;
  if (!rival || !myCriatura) return null;

  return (
    <div style={styles.screen}>
      {/* Enemy panel */}
      <div style={styles.panel}>
        <div style={{ color: "#ff6b6b", fontSize: 12, fontWeight: 700, marginBottom: 6, letterSpacing: 1 }}>
          {battle.esEncuentroSalvaje ? "CRIATURA SALVAJE" : (battle.nombreNpc ?? "RIVAL")}
        </div>
        <HPBar
          nombre={rival.nombre}
          hp={rival.hp}
          hpMax={rival.hpMax}
          nivel={rival.nivel}
          tipo={rival.tipo}
          isPlayer={false}
        />
      </div>

      {/* Turn divider */}
      <div style={styles.turnBadge}>Turno {battle.turno}</div>

      {/* Player panel */}
      <div style={styles.panel}>
        <div style={{ color: "#6bffb8", fontSize: 12, fontWeight: 700, marginBottom: 6, letterSpacing: 1 }}>
          TU CRIATURA
        </div>
        <HPBar
          nombre={myCriatura.nombre}
          hp={myCriatura.hp}
          hpMax={myCriatura.hpMax}
          nivel={myCriatura.nivel}
          tipo={myCriatura.tipo}
          isPlayer={true}
        />
      </div>

      {/* Action area */}
      <div style={styles.actions}>
        {mode === "main" && (
          <>
            <div style={{ marginBottom: 12 }}>
              <span style={{ color: "#aaa", fontSize: 13 }}>
                Que hara {myCriatura.nombre}?
              </span>
            </div>
            <MoveMenu
              movimientos={myCriatura.movimientos}
              disabled={busy}
              onSelect={i => doAction(() => gameApi.attack(i))}
            />
            <div style={{ display: "flex", gap: 8, marginTop: 12 }}>
              <button
                style={styles.secondaryBtn}
                disabled={busy || player.bolsa.length === 0}
                onClick={() => setMode("bag")}
              >
                Bolsa
              </button>
              {battle.esEncuentroSalvaje && (
                <button
                  style={styles.secondaryBtn}
                  disabled={busy || player.esferas.length === 0}
                  onClick={() => setMode("spheres")}
                >
                  Esferas ({player.esferas.length})
                </button>
              )}
              <button
                style={{ ...styles.secondaryBtn, color: "#ff6b6b", borderColor: "#6a2020" }}
                disabled={busy || !battle.esEncuentroSalvaje}
                onClick={() => doAction(() => gameApi.flee())}
              >
                Huir
              </button>
            </div>
          </>
        )}

        {mode === "bag" && (
          <div>
            <div style={{ color: "#aaa", fontSize: 13, marginBottom: 10 }}>Elige un objeto:</div>
            {player.bolsa.map((obj, i) => (
              <button
                key={i}
                style={{ ...styles.secondaryBtn, display: "block", width: "100%", marginBottom: 6, textAlign: "left" }}
                disabled={busy || obj.cantidad <= 0}
                onClick={() => doAction(() => gameApi.useItem(i))}
              >
                {obj.nombre}  x{obj.cantidad}  —  {obj.descripcion}
              </button>
            ))}
            <button style={{ ...styles.secondaryBtn, marginTop: 6 }} onClick={() => setMode("main")}>Atras</button>
          </div>
        )}

        {mode === "spheres" && (
          <div>
            <div style={{ color: "#aaa", fontSize: 13, marginBottom: 10 }}>Lanzar esfera:</div>
            {player.esferas.slice(0, 5).map((esf, i) => (
              <button
                key={i}
                style={{ ...styles.secondaryBtn, display: "block", width: "100%", marginBottom: 6 }}
                disabled={busy}
                onClick={() => doAction(() => gameApi.throwSphere(esf.index))}
              >
                {esf.nombre}
              </button>
            ))}
            <button style={{ ...styles.secondaryBtn, marginTop: 6 }} onClick={() => setMode("main")}>Atras</button>
          </div>
        )}
      </div>

      {/* Battle log */}
      <div style={styles.log}>
        <div style={{ color: "#666", fontSize: 11, marginBottom: 6, letterSpacing: 1 }}>LOG DE BATALLA</div>
        {log.length === 0 && <div style={{ color: "#444", fontSize: 13 }}>La batalla comienza...</div>}
        {log.map((line, i) => (
          <div key={i} style={{ color: i === 0 ? "#e8e8e8" : "#777", fontSize: 13, marginBottom: 3, lineHeight: 1.4 }}>
            {line}
          </div>
        ))}
      </div>
    </div>
  );
}

const styles: Record<string, React.CSSProperties> = {
  screen: {
    minHeight: "100vh",
    background: "#0a0a14",
    display: "grid",
    gridTemplateColumns: "1fr 320px",
    gridTemplateRows: "auto auto auto 1fr",
    gap: 16,
    padding: 24,
    alignContent: "start",
  },
  panel: {
    gridColumn: "1",
  },
  turnBadge: {
    gridColumn: "1 / 3",
    textAlign: "center",
    color: "#4a6fa5",
    fontSize: 12,
    fontWeight: 700,
    letterSpacing: 2,
    padding: "4px 0",
    borderBottom: "1px solid #1a2a3a",
  },
  actions: {
    gridColumn: "1",
    background: "rgba(15,20,35,0.95)",
    border: "1px solid #1e3050",
    borderRadius: 12,
    padding: 16,
  },
  log: {
    gridColumn: "2",
    gridRow: "1 / 5",
    background: "rgba(10,10,20,0.9)",
    border: "1px solid #1a2030",
    borderRadius: 12,
    padding: 16,
    overflowY: "auto",
    maxHeight: "90vh",
  },
  secondaryBtn: {
    background: "rgba(20,30,50,0.9)",
    border: "1px solid #2a4060",
    borderRadius: 8,
    padding: "8px 16px",
    color: "#c0c0d0",
    cursor: "pointer",
    fontSize: 13,
    transition: "background 0.15s",
  },
};
