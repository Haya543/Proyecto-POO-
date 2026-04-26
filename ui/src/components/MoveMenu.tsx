import type { MovimientoInfo } from "../types/game";
import { TYPE_COLORS } from "./HPBar";

interface MoveMenuProps {
  movimientos: (MovimientoInfo | null)[];
  disabled: boolean;
  onSelect: (index: number) => void;
}

// Displays up to 4 move buttons. Greys out moves with 0 PP.
export default function MoveMenu({ movimientos, disabled, onSelect }: MoveMenuProps) {
  return (
    <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: 8 }}>
      {movimientos.map((mov, i) => {
        if (!mov) {
          return (
            <div
              key={i}
              style={{
                background: "#1a1a2e",
                border: "1px solid #2a2a3e",
                borderRadius: 8,
                padding: "10px 14px",
                color: "#444",
                fontSize: 13,
              }}
            >
              ---
            </div>
          );
        }

        const noPP = mov.ppActual <= 0;
        const isDisabled = disabled || noPP;

        return (
          <button
            key={i}
            disabled={isDisabled}
            onClick={() => onSelect(i)}
            style={{
              background: isDisabled ? "#1a1a2e" : "rgba(30,50,80,0.9)",
              border: `1px solid ${isDisabled ? "#2a2a3e" : "#3a6a9a"}`,
              borderRadius: 8,
              padding: "10px 14px",
              color: isDisabled ? "#444" : "#e8e8e8",
              cursor: isDisabled ? "not-allowed" : "pointer",
              textAlign: "left",
              transition: "background 0.15s",
            }}
            onMouseEnter={e => {
              if (!isDisabled) (e.currentTarget as HTMLButtonElement).style.background = "rgba(50,80,120,0.95)";
            }}
            onMouseLeave={e => {
              if (!isDisabled) (e.currentTarget as HTMLButtonElement).style.background = "rgba(30,50,80,0.9)";
            }}
          >
            <div style={{ fontWeight: 700, fontSize: 14, marginBottom: 4 }}>{mov.nombre}</div>
            <div style={{ display: "flex", gap: 6, alignItems: "center" }}>
              <span
                style={{
                  background: TYPE_COLORS[mov.tipo],
                  color: "#fff",
                  borderRadius: 4,
                  padding: "1px 6px",
                  fontSize: 10,
                  fontWeight: 700,
                }}
              >
                {mov.tipo}
              </span>
              <span style={{ color: "#999", fontSize: 11 }}>Pwr:{mov.poder}</span>
              <span style={{ color: noPP ? "#e53935" : "#aaa", fontSize: 11, marginLeft: "auto" }}>
                {mov.ppActual}/{mov.ppMax} PP
              </span>
            </div>
          </button>
        );
      })}
    </div>
  );
}
