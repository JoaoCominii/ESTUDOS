// ============================================================================
// app.js — Orquestração principal do FarmáciaFinder
// ----------------------------------------------------------------------------
// Inicializa os componentes, carrega os dados e conecta filtros, consulta
// espacial, mapa, resultados e compartilhamento em tempo real.
// ============================================================================

import { carregarDados } from './dados.js';
import { executarConsulta, contarPorTipo, extrairCategorias } from './regras.js';
import * as filtros from './filtros.js';
import * as consultaEspacial from './consultaEspacial.js';
import * as mapa from './mapa.js';
import * as resultados from './resultados.js';
import { iniciarCompartilhamento, enviarEstado } from './compartilhamento.js';

const estado = {
  estabelecimentos: [],
  fonte: 'api',
  centro: null,
  filtros: { tipo: 'todos', categoria: 'todos', raio: 1000 },
  consulta: { operador: 'estacontido', raioInfluencia: 200, geometria: null },
  resultados: []
};

let escolhendoCentro = false;
let aplicandoRemoto = false;

async function iniciar() {
  filtros.criarFiltros({ onChange: () => executarBusca() });

  consultaEspacial.criarConsultaEspacial({
    onDesenhar: () => mapa.habilitarDesenhoPoligono(),
    onAplicar: () => executarBusca(),
    onCancelar: () => cancelarConsultaEspacial()
  });

  mapa.criarMapa({
    onMover: () => moverMapa(),
    onEscolherCentro: escolherNovoCentro,
    onPoligonoDesenhado: (geometria) => {
      consultaEspacial.setGeometria(geometria);
      executarBusca();
    }
  });

  resultados.criarResultados({
    onMudarModo: () => executarBusca(),
    onSelecionar: (estabelecimento) => mapa.voarPara(estabelecimento)
  });

  filtros.botaoEscolherCentro(() => {
    escolhendoCentro = !escolhendoCentro;
    mapa.ativarEscolhaDeCentro(escolhendoCentro);
    filtros.ativarModoEscolherCentro(escolhendoCentro);
    if (escolhendoCentro) {
      document.getElementById('resultado-info').innerHTML =
        '<small>Clique em qualquer ponto do mapa para usá-lo como novo centro de referência.</small>';
    }
  });

  const dados = await carregarDados();
  estado.estabelecimentos = dados.estabelecimentos;
  estado.fonte = dados.fonte;

  document.getElementById('fonte-dados').textContent =
    estado.fonte === 'fallback'
      ? 'Dados de demonstração (API PBH indisponível)'
      : 'Dados reais da API WFS da PBH';

  filtros.preencherCategorias(extrairCategorias(estado.estabelecimentos));

  const hospitais = estado.estabelecimentos.filter((e) => e.tipo === 'hospital');
  filtros.preencherCentros(hospitais);

  // Regra 1: o ponto de referência inicial é o hospital.
  if (hospitais.length) {
    definirCentro(hospitais[0]);
  }

  filtros.atualizarInfoCentro(estado.centro, false);
  resultados.exibir({
    resultados: [],
    estatisticas: contarPorTipo(estado.estabelecimentos),
    centro: estado.centro,
    filtros: estado.filtros,
    consulta: estado.consulta,
    fonte: estado.fonte
  });

  executarBusca();

  iniciarCompartilhamento({
    onEstadoRecebido: aplicarEstadoRemoto,
    onUsuarios: () => {},
    obterEstado: () => montarEstadoCompartilhado()
  });
}

// ---------------------------------------------------------------------------
// Definição do centro de referência
// ---------------------------------------------------------------------------
function definirCentro(hospital) {
  estado.centro = hospital
    ? { id: hospital.id, nome: hospital.nome, lat: hospital.lat, lng: hospital.lng }
    : null;
  if (hospital) filtros.setFiltros({ centroId: hospital.id });
  filtros.atualizarInfoCentro(estado.centro, false);
}

function escolherNovoCentro(lat, lng) {
  estado.centro = {
    id: '__custom__',
    nome: `Ponto em ${lat.toFixed(5)}, ${lng.toFixed(5)}`,
    lat,
    lng
  };
  filtros.setFiltros({ centroId: '__custom__' });
  filtros.atualizarInfoCentro(estado.centro, true);

  escolhendoCentro = false;
  mapa.ativarEscolhaDeCentro(false);
  filtros.ativarModoEscolherCentro(false);

  executarBusca();
}

function hospitalPorId(id) {
  return estado.estabelecimentos.find((e) => e.id === id && e.tipo === 'hospital') || null;
}

// ---------------------------------------------------------------------------
// Busca / consulta
// ---------------------------------------------------------------------------
function executarBusca() {
  const f = filtros.obterFiltros();
  estado.filtros = { tipo: f.tipo, categoria: f.categoria, raio: f.raio };

  if (f.centroId && f.centroId !== '__custom__') {
    const hospital = hospitalPorId(f.centroId);
    if (hospital && (!estado.centro || estado.centro.id !== hospital.id)) {
      definirCentro(hospital);
    }
  }

  estado.consulta = consultaEspacial.obterConsulta();
  const resultadosCalculados = executarConsulta({
    estabelecimentos: estado.estabelecimentos,
    centro: estado.centro,
    filtros: estado.filtros,
    consulta: estado.consulta
  });
  estado.resultados = resultadosCalculados;

  const modo = resultados.obterModo();
  const mostrarMapa = modo === 'mapa' || modo === 'ambos';

  resultados.exibir({
    resultados: resultadosCalculados,
    estatisticas: contarPorTipo(estado.estabelecimentos),
    centro: estado.centro,
    filtros: estado.filtros,
    consulta: estado.consulta,
    fonte: estado.fonte
  });

  mapa.renderizarCentro(estado.centro, estado.filtros.raio);
  mapa.renderizarResultados(mostrarMapa ? resultadosCalculados : []);

  if (!aplicandoRemoto) {
    enviarEstado(montarEstadoCompartilhado());
  }
}

// Cancelar a consulta espacial: remove o polígono do mapa e a geometria,
// voltando a buscar apenas pelo raio ao redor do centro.
function cancelarConsultaEspacial() {
  consultaEspacial.limparGeometria();
  mapa.limparPoligono();
  executarBusca();
}

// ---------------------------------------------------------------------------
// Compartilhamento em tempo real
// ---------------------------------------------------------------------------
function montarEstadoCompartilhado() {
  return {
    visao: mapa.obterVisao(),
    centro: estado.centro,
    filtros: estado.filtros,
    consulta: {
      operador: estado.consulta.operador,
      raioInfluencia: estado.consulta.raioInfluencia,
      geometria: estado.consulta.geometria
    },
    modo: resultados.obterModo(),
    data: Date.now()
  };
}

function moverMapa() {
  if (aplicandoRemoto) return;
  enviarEstado(montarEstadoCompartilhado());
}

function aplicarEstadoRemoto(estadoRemoto) {
  if (!estadoRemoto || aplicandoRemoto) return;

  aplicandoRemoto = true;
  try {
    if (estadoRemoto.filtros) {
      filtros.setFiltros(estadoRemoto.filtros);
      estado.filtros = { ...estado.filtros, ...estadoRemoto.filtros };
    }

    if (estadoRemoto.centro) {
      estado.centro = estadoRemoto.centro;
      filtros.setFiltros({
        centroId: estadoRemoto.centro.id === '__custom__' ? '__custom__' : estadoRemoto.centro.id
      });
      filtros.atualizarInfoCentro(estado.centro, estadoRemoto.centro.id === '__custom__');
    }

    if (estadoRemoto.consulta) {
      consultaEspacial.setOperador(estadoRemoto.consulta.operador);
      consultaEspacial.setRaioInfluencia(estadoRemoto.consulta.raioInfluencia);
      consultaEspacial.setGeometria(estadoRemoto.consulta.geometria || null);
      estado.consulta = {
        operador: estadoRemoto.consulta.operador,
        raioInfluencia: estadoRemoto.consulta.raioInfluencia,
        geometria: estadoRemoto.consulta.geometria || null
      };
    }

    if (estadoRemoto.modo) resultados.setModo(estadoRemoto.modo);

    if (estadoRemoto.visao) {
      mapa.definirVisao(estadoRemoto.visao, estadoRemoto.visao.zoom);
    }

    executarBusca();
  } finally {
    aplicandoRemoto = false;
  }
}

window.addEventListener('DOMContentLoaded', () => {
  console.log('FarmáciaFinder iniciando...');
  iniciar().catch((erro) => {
    console.error('Erro ao iniciar a aplicação:', erro);
    const div = document.getElementById('resultado-info');
    if (div) div.innerHTML = '<strong>❌ Erro ao iniciar.</strong> Verifique o console.';
  });
});
