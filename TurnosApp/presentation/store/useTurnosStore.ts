import { create } from 'zustand';

interface TurnosState {
  miTurno: string;
  turnoActual: string;
  pedirTurno: () => void;
  cancelarTurno: () => void;
}

export const useTurnosStore = create<TurnosState>((set) => ({
  miTurno: 'Sin turno',
  turnoActual: 'T-0',
  pedirTurno: () => set({ miTurno: 'T-12', turnoActual: 'T-9' }),
  

  cancelarTurno: () => set({ miTurno: 'Sin turno' }), 
}));