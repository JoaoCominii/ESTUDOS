import type {
  GerenteCoordenador,
  Motorista,
  Parametro,
  Ponto,
  Roteiro,
} from "../types/domain";
import {
  calcularTempoParadoPonto,
  calcularTempoTotalParadoRoteiro,
  calcularCustoRoteiro,
} from "../domain/regras";

/**
 * Dados de exemplo, usados enquanto o back-end (Spring Boot + PostgreSQL)
 * não está integrado. Toda a UI consome estes dados através das funções
 * abaixo, para que a troca por chamadas HTTP reais no futuro fique
 * restrita a este arquivo.
 */

export const parametro: Parametro = {
  valorCombustivelPorLitro: 6.19,
  custoPorKm: 0.42,
  jornadaPadraoHoras: 8,
  limiteAlertaParadaMinutos: 30,
};

export const motoristas: Motorista[] = [
  {
    id: "mot-1",
    nome: "Carlos Mendes",
    telefone: "(31) 99999-0001",
    documento: "111.111.111-11",
    veiculo: "Moto Honda CG 160",
    rendimentoKmLitro: 35,
  },
  {
    id: "mot-2",
    nome: "Renata Alves",
    telefone: "(31) 99999-0002",
    documento: "222.222.222-22",
    veiculo: "Fiorino",
    rendimentoKmLitro: 11,
  },
  {
    id: "mot-3",
    nome: "João Pires",
    telefone: "(31) 99999-0003",
    documento: "333.333.333-33",
    veiculo: "Moto Honda CG 160",
    rendimentoKmLitro: 33,
  },
];

export const gerentes: GerenteCoordenador[] = [
  {
    id: "ger-1",
    nome: "Ana Souza",
    telefone: "(31) 98888-0000",
    email: "ana.souza@rotaclara.app",
  },
];

function pontosRoteiroA(): Ponto[] {
  return [
    {
      id: "pt-a1",
      endereco: "Seg. Família (partida)",
      latitude: -19.9245,
      longitude: -43.9352,
      dataHoraChegada: "2026-09-23T08:00:00-03:00",
      dataHoraSaida: "2026-09-23T08:00:00-03:00",
      tempoParadoMinutos: 0,
      ordemNoRoteiro: 1,
    },
    {
      id: "pt-a2",
      endereco: "Rua Peru, 55",
      latitude: -19.942,
      longitude: -43.9378,
      dataHoraChegada: "2026-09-23T08:22:00-03:00",
      dataHoraSaida: "2026-09-23T08:37:00-03:00",
      tempoParadoMinutos: 15,
      ordemNoRoteiro: 2,
    },
    {
      id: "pt-a3",
      endereco: "Rua X, 5",
      latitude: -19.9187,
      longitude: -43.9401,
      dataHoraChegada: "2026-09-23T08:51:00-03:00",
      dataHoraSaida: "2026-09-23T09:01:00-03:00",
      tempoParadoMinutos: 10,
      ordemNoRoteiro: 3,
    },
    {
      id: "pt-a4",
      endereco: "Av. João César, 340",
      latitude: -19.9033,
      longitude: -43.9455,
      dataHoraChegada: "2026-09-23T09:20:00-03:00",
      dataHoraSaida: null, // ainda em aberto — em andamento
      tempoParadoMinutos: null,
      ordemNoRoteiro: 4,
    },
  ];
}

export const roteiros: Roteiro[] = [
  {
    id: "rot-a",
    data: "2026-09-23",
    motoristaId: "mot-1",
    pontos: pontosRoteiroA(),
    distanciaTotalKm: 18.4,
    tempoTotalParadoMinutos: 0,
    custoEstimado: 0,
    status: "em_andamento",
  },
  {
    id: "rot-b",
    data: "2026-09-23",
    motoristaId: "mot-2",
    pontos: [],
    distanciaTotalKm: 24.1,
    tempoTotalParadoMinutos: 41,
    custoEstimado: 0,
    status: "concluido",
  },
  {
    id: "rot-c",
    data: "2026-09-23",
    motoristaId: "mot-3",
    pontos: [],
    distanciaTotalKm: 21.7,
    tempoTotalParadoMinutos: 45,
    custoEstimado: 0,
    status: "concluido",
  },
];

// Deriva tempo total parado e custo estimado a partir das regras de
// negócio, em vez de deixar os números "soltos" nos dados de exemplo.
roteiros.forEach((roteiro) => {
  if (roteiro.pontos.length > 0) {
    roteiro.tempoTotalParadoMinutos = calcularTempoTotalParadoRoteiro(roteiro);
  }
  roteiro.custoEstimado = calcularCustoRoteiro(roteiro, parametro);
});

export function nomeMotorista(motoristaId: string): string {
  return motoristas.find((m) => m.id === motoristaId)?.nome ?? "—";
}

export function tempoParadoFormatado(minutos: number | null): string {
  if (minutos === null) return "em aberto";
  if (minutos < 60) return `${minutos}min`;
  const h = Math.floor(minutos / 60);
  const m = minutos % 60;
  return m === 0 ? `${h}h` : `${h}h ${m}min`;
}

export function horaFormatada(iso: string | null): string {
  if (!iso) return "—";
  return new Date(iso).toLocaleTimeString("pt-BR", {
    hour: "2-digit",
    minute: "2-digit",
  });
}

export { calcularTempoParadoPonto };
