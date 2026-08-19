import {
  aplicarFiltros,
  executarConsulta,
  extrairCategorias,
  normalizarEstabelecimento,
  contarPorTipo
} from '../frontend/js/regras.js';
import { teste, igual, verdadeiro, falso } from './harness.js';

// ---------------------------------------------------------------------------
// Conjunto sintético de dados para os testes de filtros e ordenação
// ---------------------------------------------------------------------------
const ESTABELECIMENTOS = [
  { id: 'h1', nome: 'Hospital A', tipo: 'hospital', categoria: 'público', lat: -19.9200, lng: -43.9400 },
  { id: 'f1', nome: 'Farmácia Centro', tipo: 'farmacia', categoria: 'rede', lat: -19.9205, lng: -43.9400 },
  { id: 'f2', nome: 'Farmácia Santa', tipo: 'farmacia', categoria: 'independente', lat: -19.9250, lng: -43.9400 },
  { id: 'l1', nome: 'Lab Vida', tipo: 'laboratorio', categoria: 'análises clínicas', lat: -19.9210, lng: -43.9380 },
  { id: 'l2', nome: 'Lab Anatomia', tipo: 'laboratorio', categoria: 'anatomia patológica', lat: -19.9400, lng: -43.9400 }
];

const CENTRO = { id: 'h1', nome: 'Hospital A', lat: -19.9200, lng: -43.9400 };

// Distância esperada de cada estabelecimento ao centro (metros):
//   f1: 0.0005° lat ≈ 55,7 m
//   l1: diagonal ≈ 237 m
//   f2: 0.005°  lat ≈ 556,6 m
//   l2: 0.02°   lat ≈ 2.226 m

// ---------------------------------------------------------------------------
console.log('\n[regras] Filtro por tipo e categoria');
// ---------------------------------------------------------------------------
teste('filtrar por tipo "farmacia" retorna apenas farmácias', () => {
  const resultado = aplicarFiltros(ESTABELECIMENTOS, { tipo: 'farmacia', categoria: 'todos' });
  igual(2, resultado.length);
  verdadeiro(resultado.every((e) => e.tipo === 'farmacia'));
});

teste('filtrar por categoria "independente" retorna apenas a farmácia independente', () => {
  const resultado = aplicarFiltros(ESTABELECIMENTOS, { tipo: 'todos', categoria: 'independente' });
  igual(1, resultado.length);
  igual('f2', resultado[0].id);
});

teste('filtrar por tipo + categoria combinados', () => {
  const resultado = aplicarFiltros(ESTABELECIMENTOS, { tipo: 'laboratorio', categoria: 'análises clínicas' });
  igual(1, resultado.length);
  igual('l1', resultado[0].id);
});

teste('filtro "todos" não remove nenhum elemento', () => {
  igual(ESTABELECIMENTOS.length, aplicarFiltros(ESTABELECIMENTOS, { tipo: 'todos', categoria: 'todos' }).length);
});

teste('extrairCategorias retorna lista única e ordenada', () => {
  const categorias = extrairCategorias(ESTABELECIMENTOS);
  igual(5, categorias.length);
  igual(categorias[0], categorias.slice().sort((a, b) => a.localeCompare(b, 'pt-BR'))[0]);
});

// ---------------------------------------------------------------------------
console.log('\n[regras] Ordenação por proximidade (regra das estórias)');
// ---------------------------------------------------------------------------
teste('resultados são ordenados por distância crescente (mais próximo primeiro)', () => {
  const resultados = executarConsulta({
    estabelecimentos: ESTABELECIMENTOS,
    centro: CENTRO,
    filtros: { tipo: 'todos', categoria: 'todos', raio: 3000 },
    consulta: { operador: 'estacontido', raioInfluencia: 0, geometria: null }
  });

  igual(4, resultados.length); // exclui o próprio hospital
  const ids = resultados.map((r) => r.estabelecimento.id);
  igual('f1', ids[0], 'o mais próximo vem primeiro');
  igual('l1', ids[1]);
  igual('f2', ids[2]);
  igual('l2', ids[3]);

  for (let i = 1; i < resultados.length; i++) {
    verdadeiro(resultados[i].distancia >= resultados[i - 1].distancia, 'ordem não decrescente');
  }
});

teste('raio de busca elimina resultados além do raio', () => {
  const resultados = executarConsulta({
    estabelecimentos: ESTABELECIMENTOS,
    centro: CENTRO,
    filtros: { tipo: 'todos', categoria: 'todos', raio: 600 },
    consulta: { operador: 'estacontido', raioInfluencia: 0, geometria: null }
  });
  igual(3, resultados.length); // l2 (2.226 m) fica de fora
});

teste('nenhum estabelecimento além do raio é retornado', () => {
  const raio = 600;
  const resultados = executarConsulta({
    estabelecimentos: ESTABELECIMENTOS,
    centro: CENTRO,
    filtros: { tipo: 'todos', categoria: 'todos', raio },
    consulta: { operador: 'estacontido', raioInfluencia: 0, geometria: null }
  });
  verdadeiro(resultados.every((r) => r.distancia <= raio));
});

// ---------------------------------------------------------------------------
console.log('\n[regras] Consulta espacial combinada com filtros');
// ---------------------------------------------------------------------------
teste('círculo de 300 m + estacontido retorna os dois mais próximos', () => {
  const geometria = { tipo: 'circulo', centro: CENTRO, raio: 300 };
  const resultados = executarConsulta({
    estabelecimentos: ESTABELECIMENTOS,
    centro: CENTRO,
    filtros: { tipo: 'farmacia', categoria: 'todos', raio: 5000 },
    consulta: { operador: 'estacontido', raioInfluencia: 0, geometria }
  });
  igual(1, resultados.length); // apenas f1 (~55 m dentro do círculo de 300 m)
  igual('f1', resultados[0].estabelecimento.id);
});

teste('operador intercepta é mais abrangente que estacontido', () => {
  const geometria = { tipo: 'circulo', centro: CENTRO, raio: 300 };
  const filtrosBase = { tipo: 'todos', categoria: 'todos', raio: 5000 };
  const dentro = executarConsulta({ estabelecimentos: ESTABELECIMENTOS, centro: CENTRO, filtros: filtrosBase, consulta: { operador: 'estacontido', raioInfluencia: 0, geometria } });
  const intercepta = executarConsulta({ estabelecimentos: ESTABELECIMENTOS, centro: CENTRO, filtros: filtrosBase, consulta: { operador: 'intercepta', raioInfluencia: 250, geometria } });
  verdadeiro(intercepta.length >= dentro.length);
});

teste('operador contem exige que o buffer caiba na geometria', () => {
  const geometria = { tipo: 'circulo', centro: CENTRO, raio: 400 };
  const filtrosBase = { tipo: 'todos', categoria: 'todos', raio: 5000 };
  const contem = executarConsulta({ estabelecimentos: ESTABELECIMENTOS, centro: CENTRO, filtros: filtrosBase, consulta: { operador: 'contem', raioInfluencia: 200, geometria } });
  // f1 (55 m): 55 + 200 = 255 ≤ 400 → atende; f2 (556 m): não
  igual(1, contem.length);
  igual('f1', contem[0].estabelecimento.id);
});

// ---------------------------------------------------------------------------
console.log('\n[regras] Normalização e estatísticas');
// ---------------------------------------------------------------------------
teste('normalizarEstabelecimento extrai geometria de feature WFS', () => {
  const feature = {
    properties: { ID_EQ_SAUDE: 42, NOME: 'HOSPITAL TESTE', CATEGORIA: 'HOSPITAL', LOGRADOURO: 'RUA A', NUMERO_IMOVEL: 100 },
    geometry: { type: 'Point', coordinates: [-43.94, -19.92] }
  };
  const est = normalizarEstabelecimento(feature, 'hospital');
  igual('42', est.id);
  igual('HOSPITAL TESTE', est.nome);
  igual('hospital', est.tipo);
  igual(-19.92, est.lat);
  igual(-43.94, est.lng);
  verdadeiro(est.endereco.includes('RUA A'));
});

teste('normalizarEstabelecimento categoriza farmácia pelo CNAE', () => {
  const feature = {
    properties: {
      id: 'x1',
      nome: 'DROGARIA TESTE',
      DESCRICAO_CNAE_PRINCIPAL: 'COMERCIO VAREJISTA DE PRODUTOS FARMACEUTICOS, SEM MANIPULACAO',
      lat: -19.92,
      lng: -43.94
    }
  };
  const est = normalizarEstabelecimento(feature, 'farmacia');
  igual('drogaria', est.categoria);
});

teste('contarPorTipo soma cada tipo corretamente', () => {
  const contagem = contarPorTipo(ESTABELECIMENTOS);
  igual(1, contagem.hospital);
  igual(2, contagem.farmacia);
  igual(2, contagem.laboratorio);
});
