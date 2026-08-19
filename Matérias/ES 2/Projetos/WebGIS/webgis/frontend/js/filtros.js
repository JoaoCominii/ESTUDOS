// ============================================================================
// filtros.js — Componente "Filtros de busca"
// ----------------------------------------------------------------------------
// Responsável por configurar os filtros de busca:
//   - Centro de referência (inicialmente um hospital; o usuário pode mudar)
//   - Raio de busca (predefinido ou definido pelo usuário)
//   - Tipo de estabelecimento (todos | hospital | farmácia | laboratório)
//   - Categoria (ex.: rede, independente, análises clínicas, ...)
// Exposições: obterFiltros(), setFiltros(), preencherCentros(), ...
// ============================================================================

const IDs = {
  centro: 'centro-select',
  centroInfo: 'centro-info',
  raioPreset: 'raio-preset',
  raioValor: 'raio-valor',
  tipo: 'tipo-estabelecimento',
  categoria: 'categoria-estabelecimento',
  usarCentroMapa: 'btn-usar-centro-mapa'
};

const PRESETS_RAIO = [200, 500, 1000, 2000, 5000, 10000];

let aoMudar = () => {};

function selecionar(id) {
  return document.getElementById(id);
}

function sincronizarRaio() {
  const preset = Number(selecionar(IDs.raioPreset).value);
  const valor = Number(selecionar(IDs.raioValor).value);
  if (!Number.isNaN(preset) && preset > 0 && valor !== preset) {
    selecionar(IDs.raioValor).value = preset;
  }
}

function emitirMudanca() {
  aoMudar();
}

export function criarFiltros({ onChange }) {
  aoMudar = onChange;

  selecionar(IDs.raioPreset).addEventListener('change', () => {
    sincronizarRaio();
    emitirMudanca();
  });

  selecionar(IDs.raioValor).addEventListener('change', () => emitirMudanca());
  selecionar(IDs.raioValor).addEventListener('input', () => emitirMudanca());

  selecionar(IDs.tipo).addEventListener('change', () => emitirMudanca());
  selecionar(IDs.categoria).addEventListener('change', () => emitirMudanca());
  selecionar(IDs.centro).addEventListener('change', () => emitirMudanca());
}

export function obterFiltros() {
  return {
    tipo: selecionar(IDs.tipo).value,
    categoria: selecionar(IDs.categoria).value,
    raio: Number(selecionar(IDs.raioValor).value) || 1000,
    centroId: selecionar(IDs.centro).value
  };
}

export function setFiltros(filtros) {
  if (filtros.tipo) selecionar(IDs.tipo).value = filtros.tipo;
  if (filtros.categoria) selecionar(IDs.categoria).value = filtros.categoria;
  if (filtros.raio) {
    selecionar(IDs.raioValor).value = filtros.raio;
    if (PRESETS_RAIO.includes(filtros.raio)) {
      selecionar(IDs.raioPreset).value = String(filtros.raio);
    }
  }
  if (filtros.centroId) selecionar(IDs.centro).value = filtros.centroId;
}

export function preencherCentros(hospitais) {
  const select = selecionar(IDs.centro);
  select.innerHTML = '';
  hospitais.forEach((h) => {
    const opcao = document.createElement('option');
    opcao.value = h.id;
    opcao.textContent = h.nome;
    select.appendChild(opcao);
  });

  const opcaoCustom = document.createElement('option');
  opcaoCustom.value = '__custom__';
  opcaoCustom.textContent = '📍 Centro personalizado (clique no mapa)';
  select.appendChild(opcaoCustom);

  if (!hospitais.length) {
    const opcao = document.createElement('option');
    opcao.value = '';
    opcao.textContent = 'Nenhum hospital encontrado';
    select.appendChild(opcao);
  }
}

export function preencherCategorias(categorias) {
  const select = selecionar(IDs.categoria);
  const atual = select.value;
  select.innerHTML = '<option value="todos">Todas as categorias</option>';
  categorias.forEach((c) => {
    const opcao = document.createElement('option');
    opcao.value = c;
    opcao.textContent = c;
    select.appendChild(opcao);
  });
  if (atual) select.value = atual;
}

export function atualizarInfoCentro(centro, mudancaPendente) {
  const div = selecionar(IDs.centroInfo);
  if (!centro) {
    div.innerHTML = '<em>Selecione um hospital de referência.</em>';
    return;
  }
  div.innerHTML = `
    <strong>${centro.nome}</strong><br>
    <small>Lat ${centro.lat.toFixed(5)}, Lng ${centro.lng.toFixed(5)}</small>
    ${mudancaPendente ? '<br><small class="aviso">Centro definido por clique no mapa.</small>' : ''}
  `;
}

export function ativarModoEscolherCentro(ativo) {
  selecionar(IDs.usarCentroMapa).classList.toggle('ativo', ativo);
}

export function botaoEscolherCentro(callback) {
  selecionar(IDs.usarCentroMapa).addEventListener('click', callback);
}
