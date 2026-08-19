// ============================================================================
// dados.js — Fonte de dados do FarmáciaFinder
// ----------------------------------------------------------------------------
//  1. Tenta carregar dados reais da API WFS da PBH através do proxy local
//     (/api/pbh?tipo=...), que contorna o bloqueio de CORS do navegador.
//  2. Se o proxy não estiver disponível, tenta a API direta.
//  3. Se ambas falharem, usa um dataset de demonstração embutido (fallback)
//     para que a aplicação continue funcionando.
// ============================================================================

import { normalizarEstabelecimento } from './regras.js';

const CAMADAS = ['hospitais', 'farmacias', 'laboratorios'];

const CAMINHO_PROXY = '/api/pbh?tipo=';

// ---------------------------------------------------------------------------
// Dataset de demonstração (usado somente se a API da PBH estiver indisponível).
// Coordenadas aproximadas da região de Belo Horizonte.
// ---------------------------------------------------------------------------
const DADOS_FALLBACK = {
  hospitais: [
    ['Santa Casa de Belo Horizonte', -19.9210, -43.9335, 'privado'],
    ['Hospital das Clínicas da UFMG', -19.8692, -43.9706, 'universitário'],
    ['Hospital Odilon Behrens', -19.8905, -43.9380, 'público'],
    ['Hospital Felício Rocho', -19.9333, -43.9337, 'privado'],
    ['Hospital Evangélico de BH', -19.9236, -43.9403, 'privado'],
    ['Hospital Lifecenter', -19.9390, -43.9280, 'privado'],
    ['Hospital Júlia Kubitschek (FHEMIG)', -19.8615, -43.9820, 'público'],
    ['Hospital Infantil João Paulo II', -19.8620, -43.9825, 'público'],
    ['Hospital São Francisco de Assis', -19.9305, -43.9505, 'privado'],
    ['Hospital Vera Cruz', -19.9188, -43.9490, 'privado'],
    ['Hospital Santa Isabel', -19.9330, -43.9400, 'privado'],
    ['Hospital São Lucas', -19.9230, -43.9460, 'privado']
  ],
  farmacias: [
    ['Drogaria Pacheco – Centro', -19.9205, -43.9382, 'rede'],
    ['Drogaria Araújo – Centro', -19.9198, -43.9410, 'rede'],
    ['Drogaria São Paulo – Centro', -19.9210, -43.9405, 'rede'],
    ['Drogasil – Av. Amazonas', -19.9168, -43.9412, 'rede'],
    ['Droga Raia – Av. Afonso Pena', -19.9213, -43.9370, 'rede'],
    ['Farmácia Popular – Centro', -19.9209, -43.9418, 'popular'],
    ['Drogaria BH – Lourdes', -19.9300, -43.9470, 'independente'],
    ['Drogaria Almeida – Floresta', -19.9180, -43.9210, 'independente'],
    ['Drogaria Santa Efigênia', -19.9240, -43.9355, 'independente'],
    ['Farmácia da Santa Casa', -19.9216, -43.9340, 'hospitalar'],
    ['Drogaria Nossa Senhora de Lourdes', -19.9295, -43.9465, 'independente'],
    ['Droga Fácil – Barro Preto', -19.9335, -43.9370, 'rede'],
    ['Drogaria Minas – Funcionários', -19.9260, -43.9390, 'independente'],
    ['Farmácia Mineira – Centro', -19.9185, -43.9385, 'independente'],
    ['Drogaria Central – Centro', -19.9192, -43.9397, 'rede'],
    ['Drogaria Sol Nascente – Santa Efigênia', -19.9250, -43.9330, 'independente'],
    ['Drogaria Estrela – Savassi', -19.9350, -43.9360, 'rede'],
    ['Farmácia Universitária – Pampulha', -19.8700, -43.9680, 'popular']
  ],
  laboratorios: [
    ['Hermes Pardini – Av. do Contorno', -19.9255, -43.9415, 'análises clínicas'],
    ['LabMac – Savassi', -19.9365, -43.9300, 'análises clínicas'],
    ['Sabin – Centro', -19.9215, -43.9362, 'análises clínicas'],
    ['Lab Bioclin – Lourdes', -19.9290, -43.9480, 'análises clínicas'],
    ['Laboratório Análise Clínica – Santa Efigênia', -19.9230, -43.9330, 'análises clínicas'],
    ['Biolab – Barro Preto', -19.9330, -43.9385, 'análises clínicas'],
    ['Laboratório São Lucas – Floresta', -19.9185, -43.9200, 'análises clínicas'],
    ['CITO Diagnóstico – Funcionários', -19.9270, -43.9400, 'anatomia patológica'],
    ['Laboratório de Anatomia Patológica – Centro', -19.9200, -43.9370, 'anatomia patológica'],
    ['LABMINAS – Pampulha', -19.8710, -43.9690, 'análises clínicas']
  ]
};

function montarFallback() {
  const lista = [];
  for (const tipo of CAMADAS) {
    DADOS_FALLBACK[tipo].forEach(([nome, lat, lng, categoria]) => {
      lista.push(normalizarEstabelecimento({
        properties: { id: `fb-${tipo}-${lista.length}`, nome, lat, lng, categoria }
      }, tipo === 'hospitais' ? 'hospital' : tipo.slice(0, -1), 'fallback'));
    });
  }
  return lista;
}

// ---------------------------------------------------------------------------
// Busca os dados de um tipo pela API da PBH.
//   tipo: 'hospitais' | 'farmacias' | 'laboratorios'
// ---------------------------------------------------------------------------
async function buscarDoProxy(tipo) {
  const resp = await fetch(`${CAMINHO_PROXY}${tipo}`);
  if (!resp.ok) throw new Error(`Proxy retornou ${resp.status}`);
  const json = await resp.json();
  if (json.erro) throw new Error(json.erro);
  return json.features || [];
}

async function buscarDireto(tipo) {
  const mapTipo = { hospitais: 'hospitais', farmacias: 'farmacias', laboratorios: 'laboratorios' };
  const tipoApi = mapTipo[tipo];
  const urls = {
    hospitais: 'http://bhmap.pbh.gov.br/v2/api/idebhgeo/wfs?service=WFS&version=1.0.0&request=GetFeature&typeName=ide_bhgeo:HOSPITAIS&srsName=EPSG:4326&outputFormat=application/json&propertyName=ID_EQ_SAUDE,NOME,CATEGORIA,TIPO_LOGRADOURO,LOGRADOURO,NUMERO_IMOVEL,TELEFONE,NOME_BAIRRO_POPULAR,GEOMETRIA',
    farmacias: `http://bhmap.pbh.gov.br/v2/api/idebhgeo/wfs?service=WFS&version=1.0.0&request=GetFeature&typeName=ide_bhgeo:ATIVIDADE_ECONOMICA&srsName=EPSG:4326&outputFormat=application/json&CQL_FILTER=CNAE_PRINCIPAL%20LIKE%20%274771%25%27&propertyName=NOME,CNAE_PRINCIPAL,DESCRICAO_CNAE_PRINCIPAL,NOME_BAIRRO,NOME_LOGRADOURO,NUMERO_IMOVEL,GEOMETRIA`,
    laboratorios: `http://bhmap.pbh.gov.br/v2/api/idebhgeo/wfs?service=WFS&version=1.0.0&request=GetFeature&typeName=ide_bhgeo:ATIVIDADE_ECONOMICA&srsName=EPSG:4326&outputFormat=application/json&CQL_FILTER=CNAE_PRINCIPAL%20LIKE%20%27864%25%27&propertyName=NOME,CNAE_PRINCIPAL,DESCRICAO_CNAE_PRINCIPAL,NOME_BAIRRO,NOME_LOGRADOURO,NUMERO_IMOVEL,GEOMETRIA`
  };
  const resp = await fetch(urls[tipoApi]);
  if (!resp.ok) throw new Error(`API retornou ${resp.status}`);
  const json = await resp.json();
  return json.features || [];
}

async function buscarTipo(tipo) {
  try {
    const features = await buscarDoProxy(tipo);
    if (features.length) return features;
  } catch (e) {
    console.warn(`[dados] Proxy indisponível para ${tipo}: ${e.message}`);
  }
  try {
    return await buscarDireto(tipo);
  } catch (e) {
    console.warn(`[dados] API direta indisponível para ${tipo}: ${e.message}`);
    return [];
  }
}

// ---------------------------------------------------------------------------
// Carrega todos os estabelecimentos.
// Retorna { estabelecimentos, fonte } onde fonte é 'api' ou 'fallback'.
// ---------------------------------------------------------------------------
export async function carregarDados() {
  const estabelecimentos = [];
  const carregados = { hospitais: [], farmacias: [], laboratorios: [] };

  await Promise.all(CAMADAS.map(async (tipo) => {
    const features = await buscarTipo(tipo);
    const tipoEntidade = tipo === 'hospitais' ? 'hospital' : tipo.slice(0, -1);
    carregados[tipo] = features.map((f) => normalizarEstabelecimento(f, tipoEntidade, 'api'));
  }));

  const totalApi = Object.values(carregados).reduce((soma, arr) => soma + arr.length, 0);

  if (totalApi > 0) {
    Object.values(carregados).forEach((arr) => estabelecimentos.push(...arr));
    return { estabelecimentos, fonte: 'api' };
  }

  return { estabelecimentos: montarFallback(), fonte: 'fallback' };
}
