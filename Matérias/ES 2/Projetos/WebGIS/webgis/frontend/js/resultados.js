// ============================================================================
// resultados.js — Componente "Resultados"
// ----------------------------------------------------------------------------
// Responsável por exibir os resultados da busca. O usuário escolhe se deseja
// ver os resultados como lista, no mapa, ou ambos (lista + mapa).
// A lista é apresentada em ordem de proximidade ao centro de referência.
// ============================================================================

import { ROTULOS_TIPOS } from './regras.js';

const IDs = {
  modo: 'modo-exibicao',
  lista: 'lista-estabelecimentos',
  estatisticas: 'estatisticas',
  info: 'resultado-info'
};

function selecionar(id) {
  return document.getElementById(id);
}

let aoSelecionar = () => {};

export function criarResultados({ onMudarModo, onSelecionar }) {
  aoSelecionar = onSelecionar;
  document.querySelectorAll(`input[name="${IDs.modo}"]`).forEach((radio) => {
    radio.addEventListener('change', () => onMudarModo(radio.value));
  });
}

export function obterModo() {
  const marcado = document.querySelector(`input[name="${IDs.modo}"]:checked`);
  return marcado ? marcado.value : 'ambos';
}

export function setModo(modo) {
  const radio = document.querySelector(`input[name="${IDs.modo}"][value="${modo}"]`);
  if (radio) radio.checked = true;
}

export function exibir({ resultados, estatisticas, centro, filtros, consulta, fonte }) {
  const modo = obterModo();
  const mostrarLista = modo === 'lista' || modo === 'ambos';
  const mostrarMapa = modo === 'mapa' || modo === 'ambos';

  atualizarEstatisticas(estatisticas, fonte);
  atualizarInfo(resultados.length, centro, filtros, consulta);
  renderizarLista(resultados, mostrarLista);

  return { mostrarLista, mostrarMapa };
}

function atualizarEstatisticas(estatisticas, fonte) {
  const div = selecionar(IDs.estatisticas);
  if (!estatisticas) {
    div.innerHTML = '';
    return;
  }
  const rotuloFonte = fonte === 'fallback'
    ? 'Dados de demonstração (API indisponível)'
    : 'Dados reais da API da PBH';
  div.innerHTML = `
    <small>
      <strong>📊 Estabelecimentos carregados:</strong><br>
      🏥 Hospitais: ${estatisticas.hospital} |
      💊 Farmácias: ${estatisticas.farmacia} |
      🔬 Laboratórios: ${estatisticas.laboratorio}<br>
      <em>${rotuloFonte}</em>
    </small>
  `;
}

function atualizarInfo(quantidade, centro, filtros, consulta) {
  const div = selecionar(IDs.info);
  if (!centro) {
    div.innerHTML = '<small>Defina um centro de referência para buscar.</small>';
    return;
  }

  const linhas = [
    `<strong>✅ ${quantidade} resultado(s)</strong>`,
    `Centro: ${centro.nome}`,
    `Raio de busca: ${filtros.raio} m`,
    `Tipo: ${filtros.tipo === 'todos' ? 'todos' : ROTULOS_TIPOS[filtros.tipo] || filtros.tipo}`,
    `Categoria: ${filtros.categoria === 'todos' ? 'todas' : filtros.categoria}`
  ];

  if (consulta && consulta.operador) {
    const rotulos = {
      estacontido: 'Está contido',
      contem: 'Contém',
      intercepta: 'Intercepta'
    };
    const geometria = consulta.geometria
      ? (consulta.geometria.tipo === 'poligono' ? 'polígono desenhado' : 'círculo')
      : 'círculo do raio';
    linhas.push(`Operador: ${rotulos[consulta.operador]} (${geometria})`);
  }

  div.innerHTML = linhas.join('<br>');
}

function renderizarLista(resultados, mostrarLista) {
  const lista = selecionar(IDs.lista);
  if (!mostrarLista) {
    lista.innerHTML = '';
    return;
  }

  if (!resultados.length) {
    lista.innerHTML = '<li class="aviso">Nenhum estabelecimento encontrado com os filtros atuais.</li>';
    return;
  }

  lista.innerHTML = '';
  resultados.forEach(({ estabelecimento, distancia }) => {
    const item = document.createElement('li');
    item.className = `item-resultado tipo-${estabelecimento.tipo}`;

    const icone = estabelecimento.tipo === 'farmacia' ? '💊' : '🔬';
    const rotulo = ROTULOS_TIPOS[estabelecimento.tipo] || estabelecimento.tipo;
    const distanciaTxt = distancia === null ? '—' : `${Math.round(distancia)} m`;

    item.innerHTML = `
      <strong>${icone} ${estabelecimento.nome}</strong>
      <span class="detalhes">
        ${rotulo} · ${estabelecimento.categoria || 'sem categoria'}<br>
        📍 ${estabelecimento.endereco || 'Endereço não disponível'}<br>
        📏 <span class="distancia">${distanciaTxt}</span> do centro
      </span>
    `;

    item.addEventListener('click', () => aoSelecionar(estabelecimento));
    lista.appendChild(item);
  });
}

export function mostrarVazio() {
  selecionar(IDs.lista).innerHTML = '<li class="aviso">Faça uma busca para ver os resultados.</li>';
  selecionar(IDs.info).innerHTML = '';
}
