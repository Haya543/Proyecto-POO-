import { useState } from "react";
import { gameApi } from "../api/gameApi";
import type { GameState } from "../types/game";
import { TYPE_COLORS } from "./HPBar";

interface TitleScreenProps {
  onStart: (state: GameState) => void;
}

const STARTERS = [
  {
    index: 0,
    nombre: "Ignis",
    tipo: "FUEGO" as const,
    desc: "Criatura de fuego. Poderosa y agresiva. Acumula calor para potenciar sus ataques.",
    stats: "HP:39  ATK:52  DEF:43  VEL:45",
  },
  {
    index: 1,
    nombre: "Aqua",
    tipo: "AGUA" as const,
    desc: "Criatura de agua. Alta defensa. Absorbe golpes para reducir el proximo dano.",
    stats: "HP:44  ATK:48  DEF:65  VEL:43",
  },
  {
    index: 2,
    nombre: "Herba",
    tipo: "PLANTA" as const,
    desc: "Criatura de planta. Stats balanceados. Se regenera cada 3 turnos.",
    stats: "HP:45  ATK:49  DEF:49  VEL:45",
  },
];

// Title screen with name input and starter selection
export default function TitleScreen({ onStart }: TitleScreenProps) {
  const [nombre, setNombre] = useState("");
  const [selected, setSelected] = useState<number | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [step, setStep] = useState<"title" | "name" | "starter">("title");

  async function handleStart() {
    if (!nombre.trim()) { setError("Ingresa tu nombre!"); return; }
    if (selected === null) { setError("Elige una criatura!"); return; }
    setLoading(true);
    try {
      const state = await gameApi.newGame(nombre.trim(), selected);
      onStart(state);
    } catch {
      setError("No se pudo conectar con el servidor. Asegurate de que el backend este corriendo.");
      setLoading(false);
    }
  }

  if (step === "title") {
    return (
      <div style={styles.fullscreen}>
        <pre style={{ color: "#00d4ff", fontFamily: "monospace", fontSize: 14, lineHeight: 1.3, textAlign: "center", marginBottom: 32 }}>
{`
  ██████╗██████╗ ██╗ █████╗ ████████╗██╗   ██╗██████╗  █████╗ ███████╗
 ██╔════╝██╔══██╗██║██╔══██╗╚══██╔══╝██║   ██║██╔══██╗██╔══██╗██╔════╝
 ██║     ██████╔╝██║███████║   ██║   ██║   ██║██████╔╝███████║███████╗
 ██║     ██╔══██╗██║██╔══██║   ██║   ██║   ██║██╔══██╗██╔══██║╚════██║
 ╚██████╗██║  ██║██║██║  ██║   ██║   ╚██████╔╝██║  ██║██║  ██║███████║
  ╚═════╝╚═╝  ╚═╝╚═╝╚═╝  ╚═╝   ╚═╝    ╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝
              D E   C O M B A T E
`}
        </pre>
        <p style={{ color: "#aaa", marginBottom: 40, fontSize: 15 }}>Un juego de batallas por turnos</p>
        <button style={styles.primaryBtn} onClick={() => setStep("name")}>
          COMENZAR AVENTURA
        </button>
      </div>
    );
  }

  if (step === "name") {
    return (
      <div style={styles.fullscreen}>
        <h2 style={{ color: "#00d4ff", marginBottom: 24 }}>Como te llamas?</h2>
        <input
          autoFocus
          style={styles.input}
          placeholder="Tu nombre..."
          value={nombre}
          onChange={e => setNombre(e.target.value)}
          onKeyDown={e => e.key === "Enter" && nombre.trim() && setStep("starter")}
          maxLength={20}
        />
        {error && <p style={{ color: "#e53935", margin: "8px 0" }}>{error}</p>}
        <button
          style={{ ...styles.primaryBtn, marginTop: 20 }}
          disabled={!nombre.trim()}
          onClick={() => { setError(""); setStep("starter"); }}
        >
          SIGUIENTE
        </button>
      </div>
    );
  }

  return (
    <div style={styles.fullscreen}>
      <h2 style={{ color: "#00d4ff", marginBottom: 8 }}>Hola, {nombre}!</h2>
      <p style={{ color: "#aaa", marginBottom: 28 }}>Elige tu criatura inicial:</p>

      <div style={{ display: "flex", gap: 16, flexWrap: "wrap", justifyContent: "center", marginBottom: 32 }}>
        {STARTERS.map(s => (
          <div
            key={s.index}
            onClick={() => setSelected(s.index)}
            style={{
              ...styles.card,
              border: selected === s.index
                ? `2px solid ${TYPE_COLORS[s.tipo]}`
                : "2px solid #2a2a3e",
              boxShadow: selected === s.index ? `0 0 20px ${TYPE_COLORS[s.tipo]}66` : "none",
            }}
          >
            <div
              style={{
                width: 80,
                height: 80,
                borderRadius: "50%",
                background: `radial-gradient(circle, ${TYPE_COLORS[s.tipo]}88, ${TYPE_COLORS[s.tipo]}22)`,
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
                fontSize: 36,
                marginBottom: 12,
              }}
            >
              {s.tipo === "FUEGO" ? "🔥" : s.tipo === "AGUA" ? "💧" : "🌿"}
            </div>
            <div style={{ fontWeight: 700, fontSize: 18, color: "#f0f0f0", marginBottom: 4 }}>{s.nombre}</div>
            <span
              style={{
                background: TYPE_COLORS[s.tipo],
                color: "#fff",
                borderRadius: 6,
                padding: "2px 10px",
                fontSize: 11,
                fontWeight: 700,
                marginBottom: 10,
                display: "inline-block",
              }}
            >
              {s.tipo}
            </span>
            <p style={{ color: "#aaa", fontSize: 12, lineHeight: 1.5, marginBottom: 10 }}>{s.desc}</p>
            <code style={{ color: "#666", fontSize: 11 }}>{s.stats}</code>
          </div>
        ))}
      </div>

      {error && <p style={{ color: "#e53935", marginBottom: 12 }}>{error}</p>}

      <button
        style={{ ...styles.primaryBtn, opacity: selected === null || loading ? 0.5 : 1 }}
        disabled={selected === null || loading}
        onClick={handleStart}
      >
        {loading ? "Iniciando..." : "EMPEZAR JUEGO"}
      </button>
    </div>
  );
}

const styles: Record<string, React.CSSProperties> = {
  fullscreen: {
    minHeight: "100vh",
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    justifyContent: "center",
    background: "#0a0a14",
    padding: 24,
  },
  primaryBtn: {
    background: "linear-gradient(135deg, #1a5a8a, #0d3a5a)",
    color: "#fff",
    border: "2px solid #3a8ab8",
    borderRadius: 10,
    padding: "14px 36px",
    fontSize: 16,
    fontWeight: 700,
    cursor: "pointer",
    letterSpacing: 2,
    transition: "all 0.15s",
  },
  input: {
    background: "#111122",
    border: "2px solid #3a5a8a",
    borderRadius: 8,
    padding: "12px 16px",
    color: "#f0f0f0",
    fontSize: 18,
    width: 300,
    outline: "none",
  },
  card: {
    background: "#111122",
    borderRadius: 12,
    padding: "20px 18px",
    width: 200,
    cursor: "pointer",
    textAlign: "center",
    transition: "all 0.15s",
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
  },
};
