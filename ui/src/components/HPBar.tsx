import type { TipoElemento } from "../types/game";

// Color codes for each element type, used as badges throughout the UI
export const TYPE_COLORS: Record<TipoElemento, string> = {
  FUEGO: "#e25822",
  AGUA: "#1a78c2",
  PLANTA: "#3d9e3d",
  ELECTRICO: "#d4a017",
  TIERRA: "#9b6b3a",
  HIELO: "#5db8c2",
  NORMAL: "#7d7d7d",
};

interface HPBarProps {
  nombre: string;
  hp: number;
  hpMax: number;
  nivel: number;
  tipo: TipoElemento;
  isPlayer: boolean;
}

// Renders the creature panel with name, type badge, level, and animated HP bar
export default function HPBar({ nombre, hp, hpMax, nivel, tipo, isPlayer }: HPBarProps) {
  const pct = Math.max(0, (hp / hpMax) * 100);
  const barColor = pct > 50 ? "#4caf50" : pct > 20 ? "#f5a623" : "#e53935";

  return (
    <div
      style={{
        background: isPlayer ? "rgba(30,40,60,0.95)" : "rgba(40,20,20,0.95)",
        border: `2px solid ${isPlayer ? "#3a5a8a" : "#6a2020"}`,
        borderRadius: 10,
        padding: "12px 16px",
        minWidth: 280,
        flex: 1,
      }}
    >
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: 6 }}>
        <span style={{ fontWeight: 700, fontSize: 18, color: "#f0f0f0" }}>{nombre}</span>
        <div style={{ display: "flex", gap: 8, alignItems: "center" }}>
          <span
            style={{
              background: TYPE_COLORS[tipo],
              color: "#fff",
              borderRadius: 6,
              padding: "2px 8px",
              fontSize: 11,
              fontWeight: 700,
              letterSpacing: 1,
            }}
          >
            {tipo}
          </span>
          <span style={{ color: "#aaa", fontSize: 13 }}>Nv.{nivel}</span>
        </div>
      </div>

      <div style={{ display: "flex", alignItems: "center", gap: 10 }}>
        <div
          style={{
            flex: 1,
            height: 14,
            background: "#1a1a2e",
            borderRadius: 7,
            overflow: "hidden",
            border: "1px solid #333",
          }}
        >
          <div
            style={{
              width: `${pct}%`,
              height: "100%",
              background: barColor,
              borderRadius: 7,
              transition: "width 0.4s ease, background-color 0.4s ease",
            }}
          />
        </div>
        <span style={{ color: "#ccc", fontSize: 13, minWidth: 70, textAlign: "right" }}>
          {hp} / {hpMax}
        </span>
      </div>
    </div>
  );
}
