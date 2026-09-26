import type { Motorista, Parametro, Ponto, Roteiro, StatusRoteiro } from "../types/domain";

const API_URL = (import.meta.env.VITE_API_URL ?? "http://localhost:8080").replace(/\/$/, "");

export interface LoginResponse {
  token: string;
  perfil: string;
  nome: string;
}

export interface DashboardResponse {
  kpis: {
    tempoParadoTotalMinutos: number;
    custoEstimadoTotal: number;
    roteirosConcluidos: number;
    roteirosTotal: number;
    mediaParadaPorRoteiroMinutos: number;
  };
  tempoParadoPorDia: { data: string; minutos: number }[];
  roteiros: {
    id: string;
    motoristaNome: string;
    quantidadePontos: number;
    tempoTotalParadoMinutos: number;
    custoEstimado: number;
    status: string;
  }[];
}

export interface PontoCatalogo extends Ponto {
  dataHoraChegada: null;
  dataHoraSaida: null;
  tempoParadoMinutos: null;
  ordemNoRoteiro: number;
}

function statusDoBackend(status: string): StatusRoteiro {
  return status.toLowerCase() as StatusRoteiro;
}

function mapRoteiro(dto: any): Roteiro {
  return {
    id: dto.id,
    data: dto.data,
    motoristaId: dto.motoristaId,
    motoristaNome: dto.motoristaNome,
    pontos: dto.pontos.map((p: any) => ({
      id: p.id,
      endereco: p.endereco,
      latitude: p.latitude,
      longitude: p.longitude,
      dataHoraChegada: p.dataHoraChegada,
      dataHoraSaida: p.dataHoraSaida,
      tempoParadoMinutos: p.tempoParadoMinutos,
      ordemNoRoteiro: p.ordem,
    })),
    distanciaTotalKm: dto.distanciaTotalKm,
    tempoTotalParadoMinutos: dto.tempoTotalParadoMinutos,
    custoEstimado: dto.custoEstimado,
    status: statusDoBackend(dto.status),
  };
}

async function request<T>(path: string, options: RequestInit = {}): Promise<T> {
  const token = localStorage.getItem("rotaclara_token");
  const response = await fetch(`${API_URL}${path}`, {
    ...options,
    headers: {
      "Content-Type": "application/json",
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
      ...(options.headers ?? {}),
    },
  });

  if (response.status === 401) {
    localStorage.removeItem("rotaclara_token");
    localStorage.removeItem("rotaclara_usuario");
  }
  if (!response.ok) {
    const body = await response.json().catch(() => null);
    throw new Error(body?.mensagem ?? body?.message ?? `Erro ${response.status} na API`);
  }
  if (response.status === 204) return undefined as T;
  return response.json() as Promise<T>;
}

export const api = {
  login: (login: string, senha: string) =>
    request<LoginResponse>("/auth/login", {
      method: "POST",
      body: JSON.stringify({ login, senha }),
    }),
  listarMotoristas: async () => request<Motorista[]>("/motoristas"),
  criarMotorista: (dados: Omit<Motorista, "id">) =>
    request<Motorista>("/motoristas", { method: "POST", body: JSON.stringify(dados) }),
  listarPontos: () => request<any[]>("/pontos"),
  criarPonto: (dados: { endereco: string; latitude: number; longitude: number }) =>
    request<any>("/pontos", { method: "POST", body: JSON.stringify(dados) }),
  buscarParametro: () => request<Parametro>("/parametros"),
  atualizarParametro: (dados: Parametro) =>
    request<Parametro>("/parametros", { method: "PUT", body: JSON.stringify(dados) }),
  listarRoteiros: async (inicio: string, fim: string) =>
    (await request<any[]>(`/roteiros?inicio=${inicio}&fim=${fim}`)).map(mapRoteiro),
  criarRoteiro: async (dados: {
    data: string;
    motoristaId: string;
    distanciaTotalKm: number;
    pontos: { pontoId: string; ordem: number }[];
  }) => mapRoteiro(await request<any>("/roteiros", { method: "POST", body: JSON.stringify(dados) })),
  buscarRoteiro: async (id: string) => mapRoteiro(await request<any>(`/roteiros/${id}`)),
  registrarChegada: async (roteiroId: string, pontoId: string) =>
    mapRoteiro(await request<any>(`/roteiros/${roteiroId}/pontos/${pontoId}/chegada`, { method: "POST" })),
  registrarSaida: async (roteiroId: string, pontoId: string) =>
    mapRoteiro(await request<any>(`/roteiros/${roteiroId}/pontos/${pontoId}/saida`, { method: "POST" })),
  dashboard: (inicio: string, fim: string) =>
    request<DashboardResponse>(`/dashboard?inicio=${inicio}&fim=${fim}`),
};

export { API_URL };
