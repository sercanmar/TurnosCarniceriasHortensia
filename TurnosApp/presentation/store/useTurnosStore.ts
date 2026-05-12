import { create } from 'zustand';

interface TurnosState {
  miTurno: string;
  turnoActual: string;
  cancelarTurno: () => void;
}

export const useTurnosStore = create<TurnosState>((set) => ({
  miTurno: 'Sin turno',
  turnoActual: 'T-0',
  cancelarTurno: () => set({ miTurno: 'Sin turno' }),
}));