// ============================================================================
// consultaEspacial.js — Componente "Consulta Espacial"
// ----------------------------------------------------------------------------
// Responsável por configurar e executar consultas espaciais com os operadores:
//   - está contido (within)      → ponto dentro da geometria
//   - contém (contains)          → geometria contém ponto + buffer
//   - intercepta (intersects)    → buffer do ponto toca/intercepta geometria
// O usuário desenha um polígono no mapa (via componente Mapa) e o resultado
// é combinado com os filtros de busca.
// ============================================================================

import { OPERADORES_ESPACIAIS } from './geometria.js';

const IDs = {
  operador: 'operador-espacial',
  raioInfluencia: 'raio-influencia',
  desenhar: 'btn-desenhar-poligono',
  aplicar: 'btn-aplicar-consulta',
  cancelar: 'btn-cancelar-consulta',
  status: 'consulta-status'
};

function selecionar(id) {
  return document.getElementById(id);
}

let geometriaAtual = null;
let aoAplicar = () => {};
let aoCancelar = () => {};

export function criarConsultaEspacial({ onDesenhar, onAplicar, onCancelar }) {
  aoAplicar = onAplicar;
  aoCancelar = onCancelar;

  const selectOperador = selecionar(IDs.operador);
  selectOperador.innerHTML = '';
  Object.values(OPERADORES_ESPACIAIS).forEach((op) => {
    const opcao = document.createElement('option');
    opcao.value = op.id;
    opcao.textContent = op.rotulo;
    opcao.title = op.descricao;
    selectOperador.appendChild(opcao);
  });

  selecionar(IDs.desenhar).addEventListener('click', () => onDesenhar());
  selecionar(IDs.aplicar).addEventListener('click', () => aoAplicar());
  selecionar(IDs.cancelar).addEventListener('click', () => aoCancelar());

  atualizarStatus();
}

export function obterConsulta() {
  return {
    operador: selecionar(IDs.operador).value,
    raioInfluencia: Number(selecionar(IDs.raioInfluencia).value) || 0,
    geometria: geometriaAtual
  };
}

export function setGeometria(geometria) {
  geometriaAtual = geometria;
  atualizarStatus();
}

export function getGeometria() {
  return geometriaAtual;
}

export function limparGeometria() {
  geometriaAtual = null;
  atualizarStatus();
}

export function setOperador(operador) {
  if (OPERADORES_ESPACIAIS[operador]) selecionar(IDs.operador).value = operador;
}

export function setRaioInfluencia(raio) {
  if (typeof raio === 'number') selecionar(IDs.raioInfluencia).value = raio;
}

function atualizarStatus() {
  const div = selecionar(IDs.status);
  if (!geometriaAtual) {
    div.innerHTML = '<small>Nenhuma área desenhada. Desenhe um polígono para usar os operadores.</small>';
    return;
  }
  const n = geometriaAtual.vertices ? geometriaAtual.vertices.length : 0;
  const rotuloOperador = OPERADORES_ESPACIAIS[selecionar(IDs.operador).value]?.rotulo || '';
  div.innerHTML = `
    <small>
      <strong>Área desenhada:</strong> ${n} vértices<br>
      <strong>Operador:</strong> ${rotuloOperador}
    </small>
  `;
}
