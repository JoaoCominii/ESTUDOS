/**
 * Tipos de domínio do MVP RotaClara, refletindo o "Modelo de Dados"
 * da especificação de requisitos (seção 8) e as regras de negócio
 * RN01–RN07.
 */

export type PerfilUsuario = "motorista" | "gerente" | "administrador";

export interface Motorista {
  id: string;
  nome: string;
  telefone: string;
  documento: string;
  veiculo: string;
  /** Rendimento do veículo em km por litro — usado no cálculo de custo (RN07). */
  rendimentoKmLitro: number;
}

export interface GerenteCoordenador {
  id: string;
  nome: string;
  telefone: string;
  email: string;
}

/**
 * Um ponto do roteiro. `tempoParadoMinutos` é sempre calculado
 * (nunca digitado), conforme RN02; no ponto de partida (ordemNoRoteiro === 1)
 * ele é sempre `0`, conforme RN01.
 */
export interface Ponto {
  id: string;
  endereco: string;
  latitude: number;
  longitude: number;
  dataHoraChegada: string | null; // ISO 8601, null enquanto o ponto não foi visitado
  dataHoraSaida: string | null; // ISO 8601, null enquanto o motorista não registrou saída
  tempoParadoMinutos: number | null; // null até o cálculo (RN02)
  ordemNoRoteiro: number; // sequencial, 1 = partida (RN06)
}

export type StatusRoteiro = "planejado" | "em_andamento" | "concluido";

export interface Roteiro {
  id: string;
  data: string; // ISO date (YYYY-MM-DD) — um roteiro é de uma única data (RN05)
  motoristaId: string; // um roteiro pertence a um único motorista (RN05)
  pontos: Ponto[]; // ordenados por `ordemNoRoteiro` (RN06)
  distanciaTotalKm: number;
  tempoTotalParadoMinutos: number; // soma dos pontos, exceto a partida (RN03)
  custoEstimado: number; // calculado a partir de Parametro (RN07)
  status: StatusRoteiro;
}

/**
 * Parâmetros de custo e jornada — únicos no sistema (singleton lógico),
 * alteráveis sem mudança de código (critério de aceitação do MVP).
 */
export interface Parametro {
  valorCombustivelPorLitro: number;
  custoPorKm: number;
  jornadaPadraoHoras: number; // RN04
  limiteAlertaParadaMinutos: number;
}

export interface KpiPeriodo {
  tempoParadoTotalMinutos: number;
  custoEstimadoTotal: number;
  roteirosConcluidos: number;
  roteirosTotal: number;
  mediaParadaPorRoteiroMinutos: number;
}
