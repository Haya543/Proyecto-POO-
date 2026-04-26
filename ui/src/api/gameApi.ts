// All HTTP calls to the Java backend. Returns typed responses.

import type { GameState, NpcInfo } from "../types/game";

const BASE = "http://localhost:7070/api";

async function post<T>(path: string, body?: object): Promise<T> {
  const res = await fetch(`${BASE}${path}`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: body ? JSON.stringify(body) : undefined,
  });
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

async function get<T>(path: string): Promise<T> {
  const res = await fetch(`${BASE}${path}`);
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

export const gameApi = {
  newGame: (nombre: string, starterIndex: number) =>
    post<GameState>("/game/new", { nombre, starterIndex }),

  getState: () => get<GameState>("/game/state"),

  explore: () => post<GameState>("/game/explore"),

  heal: () => post<GameState>("/game/heal"),

  challengeNpc: () => post<GameState>("/game/challenge-npc"),

  getNpcs: () => get<NpcInfo[]>("/game/npcs"),

  // Battle actions
  attack: (index: number) =>
    post<GameState>("/battle/action", { tipo: "ATACAR", index }),

  useItem: (index: number) =>
    post<GameState>("/battle/action", { tipo: "OBJETO", index }),

  throwSphere: (index: number) =>
    post<GameState>("/battle/action", { tipo: "ESFERA", index }),

  flee: () => post<GameState>("/battle/action", { tipo: "HUIR", index: 0 }),
};
