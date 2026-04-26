import { useEffect } from "react";

interface MessageBoxProps {
  message: string | null;
  onDismiss: () => void;
}

// Floating overlay that shows turn results, type effectiveness, and capture outcomes
export default function MessageBox({ message, onDismiss }: MessageBoxProps) {
  // Auto-dismiss after 3 seconds so the player doesn't have to click every time
  useEffect(() => {
    if (!message) return;
    const t = setTimeout(onDismiss, 3000);
    return () => clearTimeout(t);
  }, [message, onDismiss]);

  if (!message) return null;

  return (
    <div
      onClick={onDismiss}
      style={{
        position: "fixed",
        bottom: 24,
        left: "50%",
        transform: "translateX(-50%)",
        background: "rgba(10,10,20,0.97)",
        border: "2px solid #4a6fa5",
        borderRadius: 12,
        padding: "14px 24px",
        maxWidth: 520,
        width: "90vw",
        color: "#e8e8e8",
        fontSize: 15,
        lineHeight: 1.6,
        whiteSpace: "pre-line",
        cursor: "pointer",
        zIndex: 100,
        boxShadow: "0 4px 24px rgba(0,0,0,0.6)",
      }}
    >
      {message}
      <div style={{ fontSize: 11, color: "#666", marginTop: 6, textAlign: "right" }}>
        clic para cerrar
      </div>
    </div>
  );
}
