// ============================================================================
// regras.js — Regras de negócio puras do FarmáciaFinder
// ----------------------------------------------------------------------------
// Componente responsável por:
//   - Filtros de busca (tipo de estabelecimento, categoria, raio ao centro)
//   - Consulta espacial (operadores combinados com os filtros)
//   - Ordenação dos resultados por proximidade ao centro de referência
// ----------------------------------------------------------------------------
// Este módulo NÃO acessa DOM nem rede — é 100% testável via Node.
// ============================================================================

import { calcularDistancia, avaliarOperadorEspacial } from './geometria.js';

export const TIPOS_ESTABELECIMENTO = ['hospital', 'farmacia', 'laboratorio'];

export const ROTULOS_TIPOS = {
  hospital: 'Hospital',
  farmacia: 'Farmácia',
  laboratorio: 'Laboratório'
};

// ---------------------------------------------------------------------------
// Normalização: converte uma feature do WFS (ou registro do fallback) em um
// objeto Estabelecimento padronizado.
// ---------------------------------------------------------------------------
export function normalizarEstabelecimento(dados, tipo, origem = 'api') {
  const props = dados.properties || dados;
  const geom = dados.geometry || null;

  let lat = props.lat;
  let lng = props.lng;
  if (!lat && !lng && geom && geom.type === 'Point') {
    lng = geom.coordinates[0];
    lat = geom.coordinates[1];
  }

  return {
    id: String(props.id ?? props.ID_EQ_SAUDE ?? props.ID_ATIV_ECON_ESTABELECIMENTO ?? props.ID ?? Math.random().toString(36).slice(2)),
    nome: props.nome ?? props.NOME ?? props.NOME_FANTASIA ?? 'Sem nome',
    tipo,
    categoria: props.categoria ?? props.CATEGORIA ?? categorizarPorCnae(props.DESCRICAO_CNAE_PRINCIPAL, tipo) ?? 'outro',
    lat: Number(lat),
    lng: Number(lng),
    endereco: montarEndereco(props),
    telefone: props.TELEFONE ?? props.telefone ?? '',
    origem
  };
}

function categorizarPorCnae(descricao, tipo) {
  if (!descricao) return null;
  const d = String(descricao).toLowerCase();
  if (tipo === 'farmacia') {
    if (d.includes('com manipulacao') || d.includes('manipulacao de formul'))
      return 'manipulação';
    return 'drogaria';
  }
  if (tipo === 'laboratorio') {
    if (d.includes('anatomia')) return 'anatomia patológica';
    return 'análises clínicas';
  }
  return null;
}

function montarEndereco(props) {
  const logradouro = props.LOGRADOURO ?? props.NOME_LOGRADOURO ?? props.logradouro ?? '';
  const numero = props.NUMERO_IMOVEL ?? props.numero ?? '';
  const bairro = props.NOME_BAIRRO_POPULAR ?? props.NOME_BAIRRO ?? props.bairro ?? '';
  const partes = [logradouro, numero ? `, ${numero}` : '', bairro ? ` - ${bairro}` : ''].join('').trim();
  return partes || 'Endereço não disponível';
}

// ---------------------------------------------------------------------------
// Categorias disponíveis para o filtro (regra: permitir filtrar por categoria).
// ---------------------------------------------------------------------------
export function extrairCategorias(estabelecimentos) {
  const categorias = new Set();
  estabelecimentos.forEach((e) => {
    if (e.categoria) categorias.add(e.categoria);
  });
  return [...categorias].sort((a, b) => a.localeCompare(b, 'pt-BR'));
}

// ---------------------------------------------------------------------------
// Filtro por tipo de estabelecimento e categoria.
// ---------------------------------------------------------------------------
export function aplicarFiltros(estabelecimentos, filtros) {
  const tipo = (filtros.tipo || 'todos').toLowerCase();
  const categoria = (filtros.categoria || 'todos').toLowerCase();

  return estabelecimentos.filter((e) => {
    if (tipo !== 'todos' && e.tipo !== tipo) return false;
    if (categoria !== 'todos' && String(e.categoria || '').toLowerCase() !== categoria) return false;
    return true;
  });
}

// ---------------------------------------------------------------------------
// Filtro espacial por raio (distância ao centro de referência).
// Regra: farmácias/laboratórios dentro do raio ao redor do centro.
// ---------------------------------------------------------------------------
export function dentroDoRaio(estabelecimento, centro, raio) {
  return calcularDistancia(centro.lat, centro.lng, estabelecimento.lat, estabelecimento.lng) <= raio;
}

// ---------------------------------------------------------------------------
// Executa a consulta completa:
//   1. Filtra por tipo e categoria
//   2. Filtra por raio ao centro de referência
//   3. Aplica o operador espacial (se houver geometria desenhada)
//   4. Ordena por proximidade (regra: ordem crescente de distância)
// Retorna [{ estabelecimento, distancia }, ...]
// ---------------------------------------------------------------------------
export function executarConsulta({ estabelecimentos, centro, filtros, consulta }) {
  const raio = Number(filtros.raio) || Infinity;
  const operador = (consulta && consulta.operador) || 'estacontido';
  const geometria = (consulta && consulta.geometria) || null;
  const raioInfluencia = (consulta && consulta.raioInfluencia) || 0;

  const resultados = [];

  aplicarFiltros(estabelecimentos, filtros).forEach((estabelecimento) => {
    if (!centro) {
      resultados.push({ estabelecimento, distancia: null });
      return;
    }

    // Regra: o ponto de referência (o próprio hospital/centro) não é resultado.
    if (centro.id && estabelecimento.id === centro.id) return;

    const distancia = calcularDistancia(centro.lat, centro.lng, estabelecimento.lat, estabelecimento.lng);
    if (!(distancia <= raio)) return;

    if (!avaliarOperadorEspacial(estabelecimento, geometria, operador, raioInfluencia)) return;

    resultados.push({ estabelecimento, distancia });
  });

  if (centro) {
    resultados.sort((a, b) => a.distancia - b.distancia);
  }

  return resultados;
}

// ---------------------------------------------------------------------------
// Estatísticas por tipo para exibição.
// ---------------------------------------------------------------------------
export function contarPorTipo(estabelecimentos) {
  const contagem = { hospital: 0, farmacia: 0, laboratorio: 0 };
  estabelecimentos.forEach((e) => {
    if (contagem[e.tipo] !== undefined) contagem[e.tipo] += 1;
  });
  return contagem;
}
