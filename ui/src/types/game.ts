// TypeScript types that mirror every Java DTO the backend sends

export type Fase = "INICIO" | "MAPA" | "BATALLA" | "JUEGO_TERMINADO";

export type TipoElemento =
  | "FUEGO"
  | "AGUA"
  | "PLANTA"
  | "ELECTRICO"
  | "TIERRA"
  | "HIELO"
  | "NORMAL";

export interface MovimientoInfo {
  nombre: string;
  tipo: TipoElemento;
  categoria: string;
  poder: number;
  precision: number;
  ppActual: number;
  ppMax: number;
}

export interface CriaturaInfo {
  nombre: string;
  tipo: TipoElemento;
  nivel: number;
  hp: number;
  hpMax: number;
  ataque: number;
  defensa: number;
  velocidad: number;
  estaViva: boolean;
  expTotal: number;
  expSiguienteNivel: number;
  movimientos: (MovimientoInfo | null)[];
}

export interface ObjetoInfo {
  nombre: string;
  descripcion: string;
  cantidad: number;
}

export interface EsferaInfo {
  index: number;
  nombre: string;
}

export interface PlayerInfo {
  nombre: string;
  dinero: number;
  medallas: number;
  equipo: CriaturaInfo[];
  capturadas: CriaturaInfo[];
  bolsa: ObjetoInfo[];
  esferas: EsferaInfo[];
}

export interface BattleState {
  turno: number;
  esEncuentroSalvaje: boolean;
  activa: boolean;
  ganador: string | null;
  criaturaJugador: CriaturaInfo | null;
  criaturaRival: CriaturaInfo | null;
  nombreNpc?: string;
}

export interface GameState {
  fase: Fase;
  mensaje: string | null;
  jugador: PlayerInfo | null;
  batalla: BattleState | null;
  puedesCurar: boolean;
  zona: number;
  siguienteNpcIndex: number;
}

export interface NpcInfo {
  nombre: string;
  dialogo: string;
  dificultad: string;
  recompensa: number;
  derrotado: boolean;
}
